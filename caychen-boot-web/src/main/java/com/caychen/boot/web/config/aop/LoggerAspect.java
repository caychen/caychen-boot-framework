package com.caychen.boot.web.config.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caychen.boot.common.annotations.NoLog;
import com.caychen.boot.common.constant.CommonConstant;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.common.response.R;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.sql.SQLSyntaxErrorException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Caychen on 2019-10-17。
 */
@Aspect
@Slf4j
@Component
@Order(CommonConstant.ASPECT_ORDER_FOR_LOGGER)
public class LoggerAspect {

    // 抽取公共的切入点表达式
    @Pointcut("execution(* com.caychen..*.controller..*.*(..))")
    public void logging() {
    }

    @Around(value = "logging()")
    public Object loggerAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        long start = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();

        Class<?> targetClass = pjp.getTarget().getClass();
        //简易类名
        String targetClassName = targetClass.getSimpleName();
        //方法名
        String methodName = pjp.getSignature().getName();

        String ipAddr = getRemoteHostFromOriginIp(request);
        String url = request.getRequestURI();

        sb.append("请求源IP: [" + ipAddr + "], 请求URL: [" + StringUtils.upperCase(request.getMethod()) + " " + url + "], ");
        String prefix = sb.toString();

        StringBuilder sb1 = new StringBuilder(prefix);
        sb1.append(targetClassName + "#" + methodName + "运行开始 start ===> 参数列表：");
        Object[] args = pjp.getArgs();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] argNames = methodSignature.getParameterNames(); // 参数名
        Map<String, Object> paramMap = getParameterMapFromRequest(args, argNames);

        String paramJson = null;
        Boolean isSuccess = null;
        String failReason = null;
        String result = null;
        //使用try-catch避免序列号参数失败，影响后续调用
        try {
            if (paramMap.size() > 0) {
                paramJson = JSON.toJSONString(paramMap);
                sb1.append(paramJson);
            } else {
                sb1.append("无参");
            }
        } catch (Exception e) {
            log.error("序列化参数有误，不影响后续调用： ", e);
            sb1.append("序列化参数有误");
        }

        try {
            //开始日志输出
            log.info(sb1.toString());

            Object o = pjp.proceed();

            if (o != null && !(o instanceof ResponseEntity)) {
                try {
                    if (!(o instanceof String)) {
                        result = JSON.toJSONString(o);
                        JSONObject jsonObject = JSON.parseObject(result);
                        Integer code = jsonObject.getInteger("code");
                        if (Objects.equals(code.toString(), R.SUCCESS_CODE)) {
                            isSuccess = true;
                        } else {
                            isSuccess = false;
                            failReason = jsonObject.get("message").toString();
                            failReason = failReason.substring(0, 500);
                        }
                    } else if (o instanceof String) {
                        isSuccess = true;
                    }
                } catch (Exception e) {
                }
            } else {
                isSuccess = true;
            }

            return o;//1、feign调用的服务返回正常包装的Response（包括正常或者异常）;2、自己内部正常返回

        } catch (Exception e) {
            isSuccess = false;
            failReason = StringUtils.substring(e.getMessage(), 0, 500);

            //捕获异常，封装
            log.error(targetClassName + "#" + methodName + "捕获异常：", e);

            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                if (businessException.getError() != null) {
                    return R.error(businessException.getError(), businessException.getExtMsg());
                } else {
                    return R.error(ErrorEnum.UNKNOWN_ERROR, businessException.getMessage());
                }
            } else if (e instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
                //方法参数不合法
                return R.error(ErrorEnum.INVALID_PARAMETER_ERROR, methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            } else if (e instanceof NullPointerException) {
                //空指针异常
                return R.error(ErrorEnum.NULL_POINTER_ERROR);
            } else if (e instanceof ValidationException) {
                //校验异常
                return R.error(ErrorEnum.INVALID_PARAMETER_ERROR, e.getMessage());
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                //请求方式不支持
                return R.error(ErrorEnum.REQUEST_METHOD_NOT_SUPPORT_ERROR, e.getMessage());
            } else if (e instanceof SQLSyntaxErrorException) {
                //sql执行异常
                return R.error(ErrorEnum.SQL_SYNTAX_ERROR, e.getMessage());
            } else {
                //其他异常
                return R.error(ErrorEnum.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } finally {
            long end = System.currentTimeMillis();
            //结束日志输出
            log.info(prefix + targetClassName + "#" + methodName + "运行结束[" + (isSuccess ? "成功" : "失败") + "] end ===> 耗时：[" + (end - start) + "]ms...");

            //类上注解
            boolean presentNoLogAnnotation = targetClass.isAnnotationPresent(NoLog.class);

            //方法注解
            Method method = methodSignature.getMethod();
            boolean methodNoLogAnnotationPresent = method.isAnnotationPresent(NoLog.class);

            if (!presentNoLogAnnotation && !methodNoLogAnnotationPresent) {
                //todo：发送到日志平台
            }
        }

    }

    private Map<String, Object> getParameterMapFromRequest(Object[] args, String[] argNames) {
        Map<String, Object> paramMap = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            //todo: 逐步改进参数列表中不需要序列号的参数
            if (!(args[i] instanceof ExtendedServletRequestDataBinder
                    || args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof HttpSession
                    || args[i] instanceof MultipartFile
                    || args[i] instanceof MultipartFile[]
                    || args[i] instanceof BindingResult)) {
                paramMap.put(argNames[i], args[i]);
            }
        }
        return paramMap;
    }

    /**
     * 获取目标主机的ip
     *
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private String getRemoteHostFromOriginIp(HttpServletRequest request) {
        String ip = request.getHeader("originIp");
        return ip;
    }

}
