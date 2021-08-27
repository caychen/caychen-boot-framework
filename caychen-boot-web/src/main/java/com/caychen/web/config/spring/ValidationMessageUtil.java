package com.caychen.web.config.spring;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: Caychen
 * @Date: 2021/8/26 19:59
 * @Description:
 */
public class ValidationMessageUtil {

    /**
     * 提取所有的属性错误信息
     *
     * @param errors
     * @return
     */
    public static Map<String, List<String>> extractFieldErrors(List<FieldError> errors) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        errors.forEach((field) ->
                fields.computeIfAbsent(field.getField(), (k) -> new ArrayList<>()).add(field.getDefaultMessage()));
        return fields;
    }

    /**
     * 提取第一个属性错误信息
     *
     * @Param
     * @Return
     **/
    public static Pair extractFirstFieldError(List<FieldError> errors) {
        Optional<FieldError> first = errors.stream().findFirst();
        return first.map(fieldError -> Pair.of(fieldError.getField(), fieldError.getDefaultMessage()))
                .orElse(null);
    }

    /**
     * 提取所有的属性错误信息
     *
     * @Param
     * @Return
     **/
    public static Map<String, List<String>> extractFieldErrors(Set<ConstraintViolation<?>> constraintViolations) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        constraintViolations.forEach((violation) ->
                fields.computeIfAbsent(violation.getPropertyPath().toString(), (k) ->
                        new ArrayList<>()).add(violation.getMessage()));
        return fields;
    }

    /**
     * 提取第一个属性错误
     *
     * @Param
     * @Return
     **/
    public static Pair extractFirstFieldError(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream().findFirst().map(violation ->
                Pair.of(violation.getPropertyPath().toString(), violation.getMessage())
        ).orElse(null);
    }
}
