package com.orange.orangemall.filter;

import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.model.pojo.User;
import com.orange.orangemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

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
 * @description 管理员校验
 **/
public class AdminFilter implements Filter {

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 登陆校验之后进行管理员校验
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
            // 结束方法
            return;
        }
        // 管理员校验
        if (!userService.checkAdminRole(currentUser)) {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    +"\"status\": 10008,\n"
                    +"\"msg\": \"需要管理员权限\",\n"
                    +"\"data\": null\n"
                    +"}");
            out.flush();
            out.close();
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
