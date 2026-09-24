package org.example.eventordersystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.eventordersystem.entity.OperationLog;

/**
 * 操作日志表 Mapper（只插不改；MP BaseMapper 提供通用 CRUD，自定义 SQL 需要时再扩展）
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
