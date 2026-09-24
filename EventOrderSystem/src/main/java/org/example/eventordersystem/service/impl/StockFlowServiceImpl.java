package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.StockFlow;
import org.example.eventordersystem.mapper.StockFlowMapper;
import org.example.eventordersystem.service.StockFlowService;
import org.springframework.stereotype.Service;

/**
 * 库存流水表 Service 实现（只插不改；继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class StockFlowServiceImpl extends ServiceImpl<StockFlowMapper, StockFlow> implements StockFlowService {
}
