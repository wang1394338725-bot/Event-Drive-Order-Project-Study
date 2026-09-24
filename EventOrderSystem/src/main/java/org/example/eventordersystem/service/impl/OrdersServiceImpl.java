package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.Orders;
import org.example.eventordersystem.mapper.OrdersMapper;
import org.example.eventordersystem.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * 订单表 Service 实现（继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
