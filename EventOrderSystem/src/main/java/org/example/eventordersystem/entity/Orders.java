package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表（对应 orders 表，order 为 MySQL 保留字故表名 orders；商品信息存快照）
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增（内部用） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务订单号（时间戳+随机串，对外展示、MQ 幂等判重） */
    private String orderNo;

    /** 购买人 id（映射 user.id，不建外键） */
    private Long userId;

    /** 商品 id（映射 product.id，不建外键） */
    private Long productId;

    /** 商品名称快照（防商品改名/删除后无法溯源） */
    private String productName;

    /** 购买数量快照（即商品库存减少量） */
    private Integer quantity;

    /** 预付金额（下单时单价×数量，优惠前） */
    private BigDecimal prepaidAmount;

    /** 实付金额（支付成功后写入，预留优惠券扣减） */
    private BigDecimal actualAmount;

    /** 订单状态：0待支付 1处理中 2成功 3失败 4支付超时 5待交付 6已取消 */
    private Integer status;

    /** 支付时间（退款对账、状态追溯用） */
    private LocalDateTime payTime;

    /** 下单时间（1-3 分钟支付倒计时起点） */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
