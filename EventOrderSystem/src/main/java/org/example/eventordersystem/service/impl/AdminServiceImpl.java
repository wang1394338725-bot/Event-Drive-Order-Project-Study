package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.Admin;
import org.example.eventordersystem.mapper.AdminMapper;
import org.example.eventordersystem.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * 管理员表 Service 实现（继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
