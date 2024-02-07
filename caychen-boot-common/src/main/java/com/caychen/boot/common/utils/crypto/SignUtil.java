package com.caychen.boot.common.utils.crypto;

import com.caychen.boot.common.constant.SymbolConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lgs
 * @date 2021/3/11
 */
@Slf4j
public class SignUtil {

    /**
     * hmac签名
     * @param signMap 待签名字段集合
     * @param secretKey 签名秘钥
     * @return 签名
     */
    public static String sign(TreeMap<String, String> signMap, String secretKey) throws NoSuchAlgorithmException {
        if (MapUtils.isEmpty(signMap)){
            return "";
        }
        String params = generateSignString(signMap, secretKey);
        log.info("待签名字段串：" + params);
        //签名
        return digest(params, "sha-256");
    }

    /**
     * sha256加密
     * @param data 待加密数据
     * @return
     * @throws NoSuchAlgorithmException
     */
//    public static String sha256(String data) throws NoSuchAlgorithmException {
//        MessageDigest messageDigest = MessageDigest.getInstance(CommonConstants.SHA_256);
//        byte[] digest = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
//
//        return Base64.getEncoder().encodeToString(digest);
//    }

    /**
     * 使用指定哈希算法计算摘要信息
     *
     * @param content   内容
     * @param algorithm 哈希算法
     * @return 内容摘要
     */
    public static String digest(String content, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成签名字符串
     * 按照{key1}={value1}&{key2}={value2}&...&{macKey}且字母升序的方式拼接签名字符串，如:body=body&X-App-Id=1&123mac。
     * @param signMap 待签名字段集合
     * @param macKey 签名秘钥
     * @return 签名字符串
     */
    public static String generateSignString(TreeMap<String, String> signMap, String macKey) {
        if (MapUtils.isEmpty(signMap)){
            return "";
        }
        StringBuilder signStrBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : signMap.entrySet()){
            //过滤空值
            if (StringUtils.isEmpty(entry.getValue())){
                continue;
            }
            //拼接签名字段
            signStrBuilder.append(entry.getKey())
                    .append(SymbolConstant.EQUAL_SIGN)
                    .append(entry.getValue())
                    .append(SymbolConstant.AND);
        }
        return signStrBuilder.append(macKey).toString();
    }
}
