package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 余额流水表（对应 balance_flow 表，只插不改，无 update_time；before/after 双记供对账核对）
 */
@Data
@TableName("balance_flow")
public class BalanceFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 id（映射 user.id，不建外键） */
    private Long userId;

    /** 变动类型：1充值 2扣款 3退款 4回滚（回滚=系统异常补偿，与用户取消的退款区分） */
    private Integer changeType;

    /** 变动金额（恒为正数，方向由 change_type 表达） */
    private BigDecimal amount;

    /** 变动前余额（对账核对用） */
    private BigDecimal beforeBalance;

    /** 变动后余额（对账核对用） */
    private BigDecimal afterBalance;

    /** 关联订单号（充值类为空） */
    private String orderNo;

    /** 创建时间 */
    private LocalDateTime createTime;
}
