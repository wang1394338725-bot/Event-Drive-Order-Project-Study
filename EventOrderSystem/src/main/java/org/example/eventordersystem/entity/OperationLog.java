package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志表（对应 operation_log 表，管理员操作留痕：商品上下架、入库、账号创建等）
 */
@Data
@TableName("operation_log")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 操作人 id（映射 admin.id，不建外键） */
    private Long adminId;

    /** 操作类型（如 PRODUCT_UP 上架 / PRODUCT_DOWN 下架 / STOCK_IN 入库） */
    private String opType;

    /** 操作内容描述 */
    private String opContent;

    /** 操作时间 */
    private LocalDateTime createTime;
}
