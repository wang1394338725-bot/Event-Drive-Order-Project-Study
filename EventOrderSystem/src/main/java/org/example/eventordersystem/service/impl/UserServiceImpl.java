package org.example.eventordersystem.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.example.eventordersystem.entity.User;
import org.example.eventordersystem.mapper.UserMapper;
import org.example.eventordersystem.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表 Service 实现（继承 MP ServiceImpl 获得通用服务实现，业务方法按需扩展）
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
