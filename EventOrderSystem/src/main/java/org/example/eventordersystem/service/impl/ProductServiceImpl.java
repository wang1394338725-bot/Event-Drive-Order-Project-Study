package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.Product;
import org.example.eventordersystem.mapper.ProductMapper;
import org.example.eventordersystem.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * 商品表 Service 实现（继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
