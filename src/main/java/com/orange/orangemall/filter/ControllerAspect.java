package com.orange.orangemall.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * aop拦截请求记录请求相应的信息
 */

@Component
@Aspect
public class ControllerAspect {
    private final Logger log = LoggerFactory.getLogger(ControllerAspect.class);
    @Around("execution(* com.orange.orangemall.controller.*.*(..))")
    public Object ControllerAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        try{
            long startTime = new Date().getTime();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            log.info("URL: "+httpServletRequest.getRequestURI());
            log.info("HTTP_METHOD: "+httpServletRequest.getMethod());
            log.info("IP: "+httpServletRequest.getRemoteAddr());
            log.info("CLASS_METHOD: "+proceedingJoinPoint.getSignature().getDeclaringTypeName()+"."+
                    proceedingJoinPoint.getSignature().getName());
            log.info("ARGS: "+ Arrays.toString(proceedingJoinPoint.getArgs()));
            Object res = proceedingJoinPoint.proceed();
            long endTime = new Date().getTime();
            long durationTime = endTime-startTime;
            log.info("RESPONSE: "+new ObjectMapper().writeValueAsString(res));
            log.info("DURATION: "+durationTime+"ms");
            return res;

        }catch (Throwable throwable) {
            throw throwable;
        }
    }
}
