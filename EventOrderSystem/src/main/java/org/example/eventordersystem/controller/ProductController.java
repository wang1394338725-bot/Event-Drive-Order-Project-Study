package org.example.eventordersystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.eventordersystem.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品接口（骨架阶段：仅分层挂载，业务方法按模块开发时填充）
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
}
