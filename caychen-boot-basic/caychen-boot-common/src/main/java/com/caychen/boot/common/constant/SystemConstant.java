package com.caychen.boot.common.constant;

/**
 * @Author: Caychen
 * @Date: 2024/3/24 19:24
 * @Description:
 */
public class SystemConstant {

    /**
     * 系统换行符
     */
    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * 系统可用的处理器数量
     */
    public static int AVAILABLE_PROCESSOR = Runtime.getRuntime().availableProcessors();
    /**
     * 系统名称
     */
    String OS_NAME = System.getProperty("os.name");
    /**
     * 系统用户目录
     */
    String USER_HOME = System.getProperty("user.home");
}
