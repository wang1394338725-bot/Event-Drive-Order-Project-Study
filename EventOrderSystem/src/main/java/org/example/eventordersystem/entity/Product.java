package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表（对应 product 表，库存直接放本表）
 */
@Data
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品名称 */
    private String name;

    /** 售价（元），DECIMAL 防浮点误差 */
    private BigDecimal price;

    /** 库存数量 */
    private Integer stock;

    /** 商家名称（字符串，当前无商家体系） */
    private String merchantName;

    /** 上下架状态：1上架 0下架 */
    private Integer status;

    /** 商品描述 */
    private String description;

    /** 商品图片（本地 url） */
    private String imageUrl;

    /** 创建时间（首次入库时间） */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
