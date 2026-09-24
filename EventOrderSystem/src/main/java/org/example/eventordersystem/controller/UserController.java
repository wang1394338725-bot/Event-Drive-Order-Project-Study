package org.example.eventordersystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.eventordersystem.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口（骨架阶段：仅分层挂载，业务方法按模块开发时填充）
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
}
