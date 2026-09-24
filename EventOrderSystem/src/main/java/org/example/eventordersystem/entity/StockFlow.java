package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存流水表（对应 stock_flow 表，只插不改，无 update_time；商品"最后一次入库时间"= 本表该商品最新一条入库记录）
 */
@Data
@TableName("stock_flow")
public class StockFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品 id（映射 product.id，不建外键） */
    private Long productId;

    /** 变动类型：1下单扣减 2取消回补 3超时回滚回补 4入库 */
    private Integer changeType;

    /** 变动数量（恒为正数，方向由 change_type 表达） */
    private Integer quantity;

    /** 变动前库存（对账核对用） */
    private Integer beforeStock;

    /** 变动后库存（对账核对用） */
    private Integer afterStock;

    /** 关联订单号（入库类为空） */
    private String orderNo;

    /** 创建时间 */
    private LocalDateTime createTime;
}
