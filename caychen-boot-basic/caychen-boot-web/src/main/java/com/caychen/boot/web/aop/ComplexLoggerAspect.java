package com.caychen.boot.web.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caychen.boot.common.annotations.NoLog;
import com.caychen.boot.common.constant.CommonConstant;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.common.model.LogBaseModel;
import com.caychen.boot.common.response.R;
import com.caychen.boot.common.utils.common.DateUtil;
import com.caychen.boot.common.utils.common.IpUtils;
import com.caychen.boot.common.utils.lang.StringUtils;
import com.caychen.boot.common.utils.random.UUIDUtil;
import com.caychen.boot.core.config.logger.LogHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Caychen on 2019-10-17。
 */
@Aspect
@Slf4j
//@Component
@Order(CommonConstant.ASPECT_ORDER_FOR_LOGGER)
public class ComplexLoggerAspect {

    @Autowired
    private LogHandler logHandler;

    // 抽取公共的切入点表达式
    @Pointcut("execution(* com..*.controller..*.*(..))")
    public void logging() {
    }

    @Around(value = "logging()")
    public Object loggerAround(ProceedingJoinPoint pjp) throws Throwable {
        //MDC 链路追踪
        MDC.put("traceId", UUIDUtil.getUUIDWithoutReplace());

        long start = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object target = pjp.getTarget();
        Class<?> targetClass = target.getClass();
        //简易类名
        String targetClassName = targetClass.getSimpleName();

        //方法名
        String methodName = pjp.getSignature().getName();
        String ipAddr = IpUtils.getIPAddress(request);
        String url = request.getRequestURI();

        Object[] args = pjp.getArgs();
        log.info("{}.{}() request:{}", target, methodName, args);

        Boolean isSuccess = null;
        String failReason = null;
        String result = null;

        Object o = null;
        try {
            o = pjp.proceed();
            //记录接口操作流水出口
            if (o != null && !(o instanceof ResponseEntity)) {
                try {
                    if (!(o instanceof String)) {
                        result = JSON.toJSONString(o);
                        JSONObject jsonObject = JSON.parseObject(result);
                        Object code = jsonObject.get("code");
                        if (Objects.equals(Integer.valueOf(code.toString()), R.SUCCESS_CODE)) {
                            isSuccess = true;
                        } else {
                            isSuccess = false;
                            failReason = jsonObject.get("description").toString();
                            failReason = failReason.substring(0, 500);
                        }
                    } else if (o instanceof String) {
                        isSuccess = true;
                        result = (String) o;
                    }
                } catch (Exception e) {
                }
            } else {
                isSuccess = true;
            }
            return o;

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
            MDC.remove("traceId");

            long end = System.currentTimeMillis();
            //结束日志输出
            log.info(targetClassName + "#" + methodName + "运行结束[" + (isSuccess ? "成功" : "失败") + "] end ===> 耗时：[" + (end - start) + "]ms...");

            //类上注解
            boolean presentNoLogAnnotation = targetClass.isAnnotationPresent(NoLog.class);

            //方法注解
            Method method = methodSignature.getMethod();
            boolean methodNoLogAnnotationPresent = method.isAnnotationPresent(NoLog.class);

            if (!presentNoLogAnnotation && !methodNoLogAnnotationPresent) {
                Object params = getParams(method, args);

                LogBaseModel baseLogModel = new LogBaseModel();
                baseLogModel.setIpAddr(ipAddr);
                baseLogModel.setRequestUrl(url);
                baseLogModel.setRequestDateTime(DateUtil.formatDate(start));
                baseLogModel.setRequestParam(JSON.toJSONString(params));
                baseLogModel.setResponseParam(result);
                baseLogModel.setResponseDateTime(DateUtil.formatDate(end));
                baseLogModel.setExecutionTime(end - start);
                baseLogModel.setIsSuccess(isSuccess);
                baseLogModel.setFailReason(failReason);
                baseLogModel.setRequestMethod(StringUtils.upperCase(request.getMethod()));

                logHandler.handleLog(baseLogModel);
            }
        }

    }

    private Object getParams(Method method, Object[] args) {
        List<Object> paramsList = Lists.newArrayList();
        //拿到当前方法的参数数组
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            //参数名
            String paramName = parameter.getName();

            //获取RequestBody注解修饰的参数
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                if (requestBody != null) {
                    paramsList.add(args[i]);
                }
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                //获取RequestParam注解修饰的参数
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                Map<String, Object> map = new HashMap<>();
                //获取的形参名称作为key
                if (StringUtils.isNotEmpty(requestParam.value())) {
                    paramName = requestParam.value();
                }
                map.put(paramName, args[i]);
                paramsList.add(map);
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                //获取PathVariable注解修饰的参数
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                Map<String, Object> map = new HashMap<>();
                //获取的形参名称作为key
                String name = pathVariable.name();
                if (StringUtils.isNotEmpty(name)) {
                    paramName = name;
                }
                map.put(paramName, args[i]);
                paramsList.add(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put(paramName, args[i]);
                paramsList.add(map);
            }
        }
        if (paramsList.size() == 0) {
            return null;
        } else if (paramsList.size() == 1) {
            return paramsList.get(0);
        } else {
            return paramsList;
        }
    }

}
