package com.orange.orangemall.config;

import com.orange.orangemall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description adminFilter配置
 **/
@Configuration
public class AdminFilterConfig{

    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }
    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(adminFilter());
//        filterRegistrationBean.addUrlPatterns("/user/admin/*");
        filterRegistrationBean.addUrlPatterns("/category/admin/*");
        filterRegistrationBean.addUrlPatterns("/cart/admin/*");
        filterRegistrationBean.addUrlPatterns("/product/admin/*");
        filterRegistrationBean.setName("adminFilterConf");
        return filterRegistrationBean;
    }
}
