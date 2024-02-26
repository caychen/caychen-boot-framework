package com.caychen.boot.web.config.aop;

import com.caychen.boot.common.constant.CommonConstant;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.response.R;
import com.caychen.boot.common.utils.common.IpUtils;
import com.caychen.boot.common.utils.random.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Caychen on 2019-10-17。
 */
@Aspect
@Slf4j
//@Component
@Order(CommonConstant.ASPECT_ORDER_FOR_LOGGER)
public class SimpleLoggerAspect {

    // 抽取公共的切入点表达式
    @Pointcut("execution(* com..*.controller..*.*(..))")
    public void logging() {
    }

    @Around(value = "logging()")
    public Object loggerAround(ProceedingJoinPoint pjp) throws Throwable {
        //MDC 链路追踪
        MDC.put("traceId", UUIDUtil.getUUIDWithoutReplace());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("请求路径 : " + request.getRequestURI());
        log.info("请求方式 : " + request.getMethod());
        log.info("ip地址 : " + IpUtils.getIPAddress(request));
        log.info("req参数 : " + Arrays.toString(pjp.getArgs()));

        Object o = null;
        try {
            o = pjp.proceed();
            return o;
        } catch (Exception e) {
            log.error("服务器异常", e);
            return R.error(ErrorEnum.INTERNAL_SERVER_ERROR);
        } finally {
            MDC.remove("traceId");
            log.info("resp参数 : " + o);
        }

    }

}
