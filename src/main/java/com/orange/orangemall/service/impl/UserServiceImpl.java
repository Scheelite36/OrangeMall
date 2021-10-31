package com.orange.orangemall.service.impl;

import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.model.dao.UserMapper;
import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.service.UserService;
import com.orange.orangemall.utils.MD5EncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void register(String userName,String password) throws OrangeMallException {
        User rest = userMapper.selectByName(userName);
        if(rest!=null){
            throw new OrangeMallException(OrangeMallExceptionEnum.NAME_EXISTED);
        }
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        int count = userMapper.insertSelective(user);
        if (count != 1) {
            throw new OrangeMallException(OrangeMallExceptionEnum.INSERT_FAIL);
        }
    }

    @Override
    public User login(String userName, String password) throws NoSuchAlgorithmException, OrangeMallException {
        User user = userMapper.selectLogin(userName, MD5EncodeUtils.getMD5Str(password));
        if (user == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_MATCH);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws OrangeMallException {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count != 1) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    @Override
    public boolean checkAdminRole(User user){
        // 校验是否为管理员
        return user.getRole().equals(2);
    }
}
