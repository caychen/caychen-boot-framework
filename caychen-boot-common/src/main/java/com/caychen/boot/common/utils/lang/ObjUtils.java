package com.caychen.boot.common.utils.lang;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjUtils {

    /**
     * 将简单（只包含存在基本类型及其封装类和string）bean类型转换为map，此方法只做第一层的处理，即：如果param中有符合类型的对象，只取出对象
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 判断是否为空bean，如果是直接返回
        if (bean == null) {
            return resultMap;
        }
        if (Map.class.isAssignableFrom(bean.getClass())) {
            resultMap = (Map<String, Object>) bean;
            return resultMap;
        }
        // 获取类的class
        Field[] fields = bean.getClass().getDeclaredFields();
        //获取所有父类中的所有Field，并合并到当前Field中
        Class superClass = bean.getClass().getSuperclass();
        while (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            fields = concat(superFields, fields);//concat时，注意数组顺序（父类在前，子类在后），为了下面转成Map后，如果存在子类含有和父类一样的属性的情况时，子类可以覆盖父类中对于属性的值
            superClass = superClass.getSuperclass();
        }
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();// 获取所有字段名称
            Object filedValue = null;
            try {
                int typeInt = fields[i].getModifiers();// 获取字段的类型
                // 获取字段的类型申明表，8静态，2私有，16final =26，类型26为静态常量，不做处理如最终serialVersionUID
                if (typeInt != 26) {
                    fields[i].setAccessible(true);// 设置访问权限
                    filedValue = fields[i].get(bean);// 获取所有字段的值
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            resultMap.put(fieldName, filedValue);
        }

        return resultMap;

    }

    /**
     * 数组合并
     *
     * @param first  数组1
     * @param second 数组2
     * @param <T>    泛型
     * @return 数组1+数组2
     */
    private static <T> T[] concat(T[] first, T[] second) {
        if (first == null || first.length == 0) {
            return second;
        }
        if (second == null || second.length == 0) {
            return first;
        }
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 根据Excel文件或者csv读取的内容，转换成对应的Bean
     *
     * @param infoList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertToObject(List<List<String>> infoList, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for (List<String> info : infoList) {
            try {
                T obj = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                if (info.size() != fields.length) {
                    return result;
                }
                for (int index = 0; index < fields.length; index++) {
                    if (StringUtils.isEmpty(info.get(index))) {
                        continue;
                    }
                    Field field = fields[index];
                    field.setAccessible(true);
                    String type = field.getType().getSimpleName();
                    switch (type) {
                        case "String":
                            field.set(obj, info.get(index));
                            break;
                        case "Integer":
                        case "int":
                            field.set(obj, Integer.valueOf(info.get(index)));
                            break;
                        case "Long":
                        case "long":
                            field.set(obj, Long.valueOf(info.get(index)));
                        case "Double":
                        case "double":
                            field.set(obj, Double.valueOf(info.get(index)));
                            break;
                        case "Short":
                        case "short":
                            field.set(obj, Short.valueOf(info.get(index)));
                            break;
                        case "Float":
                        case "float":
                            field.set(obj, Float.valueOf(info.get(index)));
                            break;
                        case "BigDecimal":
                            field.set(obj, new BigDecimal(info.get(index)));
                            break;
                        case "BigInteger":
                            field.set(obj, new BigInteger(info.get(index)));
                            break;
                    }

                    field.setAccessible(false);
                }
                result.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
