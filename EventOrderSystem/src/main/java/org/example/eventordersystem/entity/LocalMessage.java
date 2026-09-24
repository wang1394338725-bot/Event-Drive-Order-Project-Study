package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 本地消息表（对应 local_message 表，决策 A 核心：与本地事务同库同事务写入，后台任务扫描重发）
 */
@Data
@TableName("local_message")
public class LocalMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务订单号（关联 orders.order_no，不建外键） */
    private String orderNo;

    /** 消息类型（如 ORDER_PAID=订单支付成功） */
    private String messageType;

    /** 消息内容（JSON 格式消息体，骨架阶段按 String 存取） */
    private String payload;

    /** 状态：0待发送 1已发送 2已确认 3失败 */
    private Integer status;

    /** 已重试次数 */
    private Integer retryCount;

    /** 下次重试时间（重试调度依据） */
    private LocalDateTime nextRetryTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
