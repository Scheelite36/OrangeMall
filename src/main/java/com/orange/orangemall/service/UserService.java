package com.orange.orangemall.service;

import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.model.pojo.User;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    public User getUser(Integer id);

    public void register(String userName,String password) throws OrangeMallException;

    User login(String userName, String password) throws NoSuchAlgorithmException, OrangeMallException;

    void updateInformation(User user) throws OrangeMallException;

    boolean checkAdminRole(User user);
}
