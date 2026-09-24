package org.example.eventordersystem.service;

import com.baomidou.mybatisplus.spring.service.IService;
import org.example.eventordersystem.entity.OperationLog;

/**
 * 操作日志表 Service 接口（只插不改；继承 MP IService 获得通用服务方法，业务方法按需扩展）
 */
public interface OperationLogService extends IService<OperationLog> {
}
