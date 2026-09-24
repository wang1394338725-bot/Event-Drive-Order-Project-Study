package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.OperationLog;
import org.example.eventordersystem.mapper.OperationLogMapper;
import org.example.eventordersystem.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志表 Service 实现（只插不改；继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
