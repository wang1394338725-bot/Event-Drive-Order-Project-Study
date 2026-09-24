package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.LocalMessage;
import org.example.eventordersystem.mapper.LocalMessageMapper;
import org.example.eventordersystem.service.LocalMessageService;
import org.springframework.stereotype.Service;

/**
 * 本地消息表 Service 实现（继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class LocalMessageServiceImpl extends ServiceImpl<LocalMessageMapper, LocalMessage> implements LocalMessageService {
}
