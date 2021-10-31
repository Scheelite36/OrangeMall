package com.orange.orangemall.exception;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * @author Scheelite
 * @date 2021/10/3
 * @email jwei.gan@qq.com
 * @description 用于处理统一异常的类，让返回给前端的内容统一
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private final  static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiRestResponse systemException(Exception e) {
        log.error("Default Exception: ",e);
        return ApiRestResponse.error(OrangeMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(OrangeMallException.class)
    @ResponseBody
    public ApiRestResponse orangeMallException(OrangeMallException orangeMallException) {
        log.error("OrangeMallException: ",orangeMallException);
        return ApiRestResponse.error(orangeMallException.getCode(), orangeMallException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse methodANVE(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException: ",e);
        // 获取异常列表中的所有异常信息组合返回
        String megs = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()).toString();
        if (!StringUtils.hasText(megs)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(OrangeMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),megs);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiRestResponse bindException(BindException e){
        // 获取异常列表中的所有异常信息返回
        String msgs = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()).toString();
        if (!StringUtils.hasText(msgs)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(OrangeMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),msgs);
    }
}
