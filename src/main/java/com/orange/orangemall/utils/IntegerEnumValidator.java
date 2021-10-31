package com.orange.orangemall.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Scheelite
 * @date 2021/10/24
 * @email jwei.gan@qq.com
 * @description 校验枚举的类
 **/
@Deprecated
public class IntegerEnumValidator implements ConstraintValidator<ValueOfEnum,Integer> {
    // 枚举中的值
    private List<Integer> enumValues;
    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
//       enumValues =  Stream.of(constraintAnnotation.enumClass().getEnumConstants()).map(Enum::valueOf).collect(Collectors.toList());

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return false;
    }
}
