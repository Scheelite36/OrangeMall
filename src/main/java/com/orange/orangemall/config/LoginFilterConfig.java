package com.orange.orangemall.config;

import com.orange.orangemall.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description 登陆过滤器的配置
 **/
@Configuration
public class LoginFilterConfig {
    @Bean
    public LoginFilter loginFilter(){
        return new LoginFilter();
    }
    @Bean(name = "loginFilterConf")
    public FilterRegistrationBean loginFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(loginFilter());
        filterRegistrationBean.addUrlPatterns("/user/updateInformation/*");
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        filterRegistrationBean.setName("loginFilterConf");
        return filterRegistrationBean;
    }
}
