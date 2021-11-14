package com.orange.orangemall.controller;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.model.request.LoginParamReq;
import com.orange.orangemall.service.UserService;
import com.orange.orangemall.utils.MD5EncodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation("注册")
    public ApiRestResponse register(@Valid LoginParamReq loginParamReq) throws OrangeMallException, NoSuchAlgorithmException {
        userService.register(loginParamReq.getUserName(), MD5EncodeUtils.getMD5Str(loginParamReq.getPassword()));
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("登陆")
    public ApiRestResponse login(@Valid LoginParamReq loginParamReq, HttpSession session) throws OrangeMallException, NoSuchAlgorithmException {
        User user = userService.login(loginParamReq.getUserName(), loginParamReq.getPassword());
        user.setPassword(null);
        session.setAttribute(OrangeMallConstant.MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    @PostMapping("/updateInformation")
    @ResponseBody
    @ApiOperation("更新个人信息")
    public ApiRestResponse updateInformation(HttpSession session,
                                             @RequestParam String signature) throws OrangeMallException {
        User currentUser = (User) session.getAttribute(OrangeMallConstant.MALL_USER);
        if (currentUser==null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/logout")
    @ResponseBody
    @ApiOperation("登出")
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(OrangeMallConstant.MALL_USER);
        return ApiRestResponse.success();
    }
}
