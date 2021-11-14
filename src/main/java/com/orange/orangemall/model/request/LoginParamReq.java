package com.orange.orangemall.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description
 **/
@Data
public class LoginParamReq {

    @NotNull(message = "用户名不能为空")
    @Length(message = "用户名字符需在{min}到{max}个字符之间",min = 3,max = 20)
    String userName;

    @NotNull(message = "密码不能为空")
    @Length(message = "密码需在{min}到{max}个字符之间",min = 8,max = 16)
    String password;
}
