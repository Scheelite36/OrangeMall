package com.orange.orangemall.controller;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.model.request.LoginParamReq;
import com.orange.orangemall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * @author Scheelite
 * @date 2021/11/13
 * @email jwei.gan@qq.com
 * @description
 **/
@RestController
@Api(tags = "管理员用户模块")
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("管理员登陆")
    public ApiRestResponse adminLogin(@Valid LoginParamReq loginParamReq, HttpSession session) throws OrangeMallException, NoSuchAlgorithmException {
        User user = userService.login(loginParamReq.getUserName(), loginParamReq.getPassword());
        if (!user.getRole().equals(2)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.ADMIN_VERIFY_FAIL);
        }
        user.setPassword(null);
        session.setAttribute(OrangeMallConstant.MALL_USER, user);
        return ApiRestResponse.success(user);
    }
}
