package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户表（对应 user 表）
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 昵称，仅展示用，不唯一 */
    private String username;

    /** 手机号，唯一登录凭证 */
    private String phone;

    /** 邮箱，可选绑定，用于密码找回提示（不可登录） */
    private String email;

    /** 登录密码（BCrypt 哈希，非明文） */
    private String password;

    /** 账户余额（元） */
    private BigDecimal balance;

    /** 注册时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
