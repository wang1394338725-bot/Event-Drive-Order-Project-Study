package org.example.eventordersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员表（对应 admin 表，登录态走 Redis token，无 token 字段）
 */
@Data
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 管理员名称 */
    private String name;

    /** 管理员账号，登录凭证 */
    private String account;

    /** 登录密码（BCrypt 哈希，非明文） */
    private String password;

    /** 角色：1=super 超管 2=normal 普通管理员 */
    private Integer role;

    /** 创建人（创建者管理员 id；0=system，即初始超管） */
    private Long createdBy;

    /** 最近登录时间 */
    private LocalDateTime lastLoginTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
