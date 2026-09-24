-- ============================================================
-- EventOrderSystem 数据库初始化脚本
-- 数据库：event_order（由 compose.yaml 的 MYSQL_DATABASE 自动创建，容器宿主端口 3307）
-- 依据：QUESTIONS.md 记录 #005（数据库设计决策，2026-09-24 三轮问答收敛并终审通过）
--
-- 公共约定（记录 #005 拍板）：
--   1. 主键统一自增 id；表间只存 id 映射，不建外键约束
--   2. 删除策略：物理删
--   3. 金额字段一律 DECIMAL；流水表只插不改，不设 update_time
--   4. 字段下划线命名；引擎 InnoDB；字符集 utf8mb4
--
-- 变更记录（约束 14：小规模修改在此注释区说明；大规模修改新建 init_xxx.sql）：
--   2026-09-24 初始建表 8 张：user / product / admin / orders / local_message
--              / balance_flow / stock_flow / operation_log
-- ============================================================

-- ------------------------------------------------------------
-- 1. 用户表
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  username    VARCHAR(32)     NOT NULL                COMMENT '昵称，仅展示用，不唯一',
  phone       VARCHAR(20)     NOT NULL                COMMENT '手机号，唯一登录凭证',
  email       VARCHAR(64)     NULL     DEFAULT NULL   COMMENT '邮箱，可选绑定，用于密码找回提示（不可登录）',
  password    VARCHAR(64)     NOT NULL                COMMENT '登录密码（BCrypt 哈希，非明文）',
  balance     DECIMAL(10,2)   NOT NULL DEFAULT 0.00   COMMENT '账户余额（元）',
  create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_phone (phone),
  UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ------------------------------------------------------------
-- 2. 商品表（库存直接放本表；"最后一次入库时间"由 stock_flow 查最新入库流水得出，不冗余存储）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  name          VARCHAR(128)    NOT NULL                COMMENT '商品名称',
  price         DECIMAL(10,2)   NOT NULL                COMMENT '售价（元），DECIMAL 防浮点误差',
  stock         INT             NOT NULL DEFAULT 0      COMMENT '库存数量',
  merchant_name VARCHAR(64)     NOT NULL DEFAULT ''     COMMENT '商家名称（字符串，当前无商家体系）',
  status        TINYINT         NOT NULL DEFAULT 1      COMMENT '上下架状态：1上架 0下架',
  description   VARCHAR(512)    NULL     DEFAULT NULL   COMMENT '商品描述',
  image_url     VARCHAR(255)    NULL     DEFAULT NULL   COMMENT '商品图片（本地 url）',
  create_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（首次入库时间）',
  update_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ------------------------------------------------------------
-- 3. 管理员表（初始超管由 SQL 手动插入，created_by=0 表示 system；登录态走 Redis token，无 token 字段）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  name            VARCHAR(32)     NOT NULL                COMMENT '管理员名称',
  account         VARCHAR(32)     NOT NULL                COMMENT '管理员账号，登录凭证',
  password        VARCHAR(64)     NOT NULL                COMMENT '登录密码（BCrypt 哈希，非明文）',
  role            TINYINT         NOT NULL                COMMENT '角色：1=super 超管 2=normal 普通管理员',
  created_by      BIGINT UNSIGNED NOT NULL DEFAULT 0      COMMENT '创建人（创建者管理员 id；0=system，即初始超管）',
  last_login_time DATETIME        NULL     DEFAULT NULL   COMMENT '最近登录时间',
  create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_account (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ------------------------------------------------------------
-- 4. 订单表（order 为 MySQL 保留字，故表名 orders；商品信息存快照防源数据变更）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增（内部用）',
  order_no       VARCHAR(32)     NOT NULL                COMMENT '业务订单号（时间戳+随机串，对外展示、MQ 幂等判重）',
  user_id        BIGINT UNSIGNED NOT NULL                COMMENT '购买人 id（映射 user.id，不建外键）',
  product_id     BIGINT UNSIGNED NOT NULL                COMMENT '商品 id（映射 product.id，不建外键）',
  product_name   VARCHAR(128)    NOT NULL                COMMENT '商品名称快照（防商品改名/删除后无法溯源）',
  quantity       INT             NOT NULL                COMMENT '购买数量快照（即商品库存减少量）',
  prepaid_amount DECIMAL(10,2)   NOT NULL                COMMENT '预付金额（下单时单价×数量，优惠前）',
  actual_amount  DECIMAL(10,2)   NOT NULL DEFAULT 0.00   COMMENT '实付金额（支付成功后写入，预留优惠券扣减）',
  status         TINYINT         NOT NULL DEFAULT 0      COMMENT '订单状态：0待支付 1处理中 2成功 3失败 4支付超时 5待交付 6已取消',
  pay_time       DATETIME        NULL     DEFAULT NULL   COMMENT '支付时间（退款对账、状态追溯用）',
  create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间（1-3 分钟支付倒计时起点）',
  update_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_user_id (user_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ------------------------------------------------------------
-- 5. 本地消息表（决策 A 核心：与本地事务同库同事务写入，保证"落库+发消息"原子性；后台任务扫描重发）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `local_message`;
CREATE TABLE `local_message` (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  order_no        VARCHAR(32)     NOT NULL                COMMENT '业务订单号（关联 orders.order_no，不建外键）',
  message_type    VARCHAR(32)     NOT NULL                COMMENT '消息类型（如 ORDER_PAID=订单支付成功）',
  payload         JSON            NULL     DEFAULT NULL   COMMENT '消息内容（JSON 格式消息体）',
  status          TINYINT         NOT NULL DEFAULT 0      COMMENT '状态：0待发送 1已发送 2已确认 3失败',
  retry_count     INT             NOT NULL DEFAULT 0      COMMENT '已重试次数',
  next_retry_time DATETIME        NULL     DEFAULT NULL   COMMENT '下次重试时间（重试调度依据）',
  create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_status (status),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地消息表（最终一致性核心件）';

-- ------------------------------------------------------------
-- 6. 余额流水表（只插不改，无 update_time；before/after 双记供对账核对）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `balance_flow`;
CREATE TABLE `balance_flow` (
  id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  user_id        BIGINT UNSIGNED NOT NULL                COMMENT '用户 id（映射 user.id，不建外键）',
  change_type    TINYINT         NOT NULL                COMMENT '变动类型：1充值 2扣款 3退款 4回滚（回滚=系统异常补偿，与用户取消的退款区分）',
  amount         DECIMAL(10,2)   NOT NULL                COMMENT '变动金额（恒为正数，方向由 change_type 表达）',
  before_balance DECIMAL(10,2)   NOT NULL                COMMENT '变动前余额（对账核对用）',
  after_balance  DECIMAL(10,2)   NOT NULL                COMMENT '变动后余额（对账核对用）',
  order_no       VARCHAR(32)     NULL     DEFAULT NULL   COMMENT '关联订单号（充值类为空）',
  create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额流水表';

-- ------------------------------------------------------------
-- 7. 库存流水表（只插不改，无 update_time；商品"最后一次入库时间"= 本表该商品最新一条入库记录）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `stock_flow`;
CREATE TABLE `stock_flow` (
  id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  product_id   BIGINT UNSIGNED NOT NULL                COMMENT '商品 id（映射 product.id，不建外键）',
  change_type  TINYINT         NOT NULL                COMMENT '变动类型：1下单扣减 2取消回补 3超时回滚回补 4入库',
  quantity     INT             NOT NULL                COMMENT '变动数量（恒为正数，方向由 change_type 表达）',
  before_stock INT             NOT NULL                COMMENT '变动前库存（对账核对用）',
  after_stock  INT             NOT NULL                COMMENT '变动后库存（对账核对用）',
  order_no     VARCHAR(32)     NULL     DEFAULT NULL   COMMENT '关联订单号（入库类为空）',
  create_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_product_id (product_id),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水表';

-- ------------------------------------------------------------
-- 8. 操作日志表（管理员操作留痕：商品上下架、入库、账号创建等）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  admin_id    BIGINT UNSIGNED NOT NULL                COMMENT '操作人 id（映射 admin.id，不建外键）',
  op_type     VARCHAR(32)     NOT NULL                COMMENT '操作类型（如 PRODUCT_UP 上架 / PRODUCT_DOWN 下架 / STOCK_IN 入库）',
  op_content  VARCHAR(255)    NOT NULL                COMMENT '操作内容描述',
  create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (id),
  KEY idx_admin_id (admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';

-- ------------------------------------------------------------
-- 初始数据（初始超管：手动插入，created_by=0 表示 system；密码为 BCrypt 哈希占位，首次启动前替换）
-- ------------------------------------------------------------
-- INSERT INTO `admin` (name, account, password, role, created_by)
-- VALUES ('超级管理员', 'root', '{bcrypt哈希占位，生成后替换}', 1, 0);
