package com.caychen.boot.common.utils.common;


import com.caychen.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: Caychen
 * @Date: 2022/3/2 17:56
 * @Description:
 */
public class IpUtils {

    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String INVALID_IP = "0:0:0:0:0:0:0:1";

    /**
     * 获取用户真实IP地址
     *
     * @param request
     * @return
     */
    public static String getIPAddress(HttpServletRequest request) {
        // Nginx的反向代理标志
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (!isValid(ip)) {
            // Apache的反向代理标志
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (!isValid(ip)) {
            // WebLogic的反向代理标志
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (!isValid(ip)) {
            // 较少出现
            ip = request.getHeader(HTTP_CLIENT_IP);
        }
        if (!isValid(ip)) {
            ip = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (!isValid(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_HOST.equals(ip) || INVALID_IP.equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inetAddress.getHostAddress();
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }

    public static boolean isValid(String ip) {
        return !(StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip));
    }
}
