package com.orange.orangemall.config;

import com.orange.orangemall.common.OrangeMallConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description
 **/
@Configuration
public class OrangeMallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 添加静态资源映射
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+ OrangeMallConstant.FILE_UPLOAD_DIR);
    }
}
