package com.orange.orangemall.common;

import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    private T data;
    private final static int OK_CODE = 1000;
    private final static String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }

    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    public static <T> ApiRestResponse<T> success(T data) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> ApiRestResponse<T> error(OrangeMallExceptionEnum ex) {
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    public static <T> ApiRestResponse<T> error(Integer code, String message) {
        return new ApiRestResponse<>(code,message);
    }
}
