package com.orange.orangemall.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Scheelite
 * @date 2021/10/24
 * @email jwei.gan@qq.com
 * @description
 **/
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IntegerEnumValidator.class)
@Deprecated
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "must be any of {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
