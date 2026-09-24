package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.BalanceFlow;
import org.example.eventordersystem.mapper.BalanceFlowMapper;
import org.example.eventordersystem.service.BalanceFlowService;
import org.springframework.stereotype.Service;

/**
 * 余额流水表 Service 实现（只插不改；继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class BalanceFlowServiceImpl extends ServiceImpl<BalanceFlowMapper, BalanceFlow> implements BalanceFlowService {
}
