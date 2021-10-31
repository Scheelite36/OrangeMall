package com.orange.orangemall.filter;


import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.utils.UserUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description 登陆校验
 **/
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 给返回内容设置字符格式
        servletResponse.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(OrangeMallConstant.MALL_USER);
        if (currentUser == null){
            PrintWriter out  = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    +"\"status\": 10005,\n"
                    +"\"msg\": \"需要登陆\",\n"
                    +"\"data\": null\n"
                    +"}");
            out.flush();
            out.close();
        }
        // 将用户保存到线程安全的用户工具对象中
        UserUtils.setLoginUser(currentUser);
        // 释放filter
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            // 销毁线程用户对象
            UserUtils.removeUser();
        }
    }
}
