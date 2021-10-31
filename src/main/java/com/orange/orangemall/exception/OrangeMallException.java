package com.orange.orangemall.exception;

import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrangeMallException extends RuntimeException {
    private final Integer code;
    private final String message;

    public OrangeMallException(OrangeMallExceptionEnum orangeMallExceptionEnum) {
        this(orangeMallExceptionEnum.getCode(), orangeMallExceptionEnum.getMsg());
    }
}
