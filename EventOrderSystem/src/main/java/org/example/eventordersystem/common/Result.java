package org.example.eventordersystem.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结构（记录 #001 Q11 拍板方向：messages + 状态码）
 *
 * @param <T> 数据体类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码（200 成功，非 200 失败；业务错误码按需扩展） */
    private int code;

    /** 提示信息 */
    private String message;

    /** 数据体 */
    private T data;

    private Result() {
    }

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功（无数据） */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /** 成功（带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /** 失败（默认错误码） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /** 失败（自定义错误码） */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
