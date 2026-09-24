package org.example.eventordersystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.eventordersystem.entity.Orders;

/**
 * 订单表 Mapper（MP BaseMapper 提供通用 CRUD，自定义 SQL 需要时再扩展）
 */
public interface OrdersMapper extends BaseMapper<Orders> {
}
