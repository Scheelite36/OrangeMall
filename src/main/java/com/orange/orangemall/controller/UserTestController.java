package com.orange.orangemall.controller;

import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Deprecated
//@RequestMapping("/user")
public class UserTestController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id){
        return userService.getUser(id);
    }

}
