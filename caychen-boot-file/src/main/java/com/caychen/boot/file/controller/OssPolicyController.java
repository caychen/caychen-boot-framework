package com.caychen.boot.file.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.caychen.boot.common.constant.SymbolConstant;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.common.response.R;
import com.caychen.boot.file.config.OssProperties;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Caychen
 * @Date: 2021/9/22 16:34
 * @Description: 签名直传服务以及上传回调服务
 */
@Slf4j
@RequestMapping("/v1")
@RestController
public class OssPolicyController {

    @Autowired(required = false)
    private OssProperties ossProperties;

    @Autowired(required = false)
    private OSS ossClient;

    @Value("${file.upload.path-prefix}")
    private String prefix;

    /**
     * 签名直传服务以及上传回调服务
     *
     * @return
     */
    @GetMapping("/oss/policy")
    public R<Map<String, String>> policy() {
        if (StringUtils.startsWith(prefix, SymbolConstant.SLASH)) {
            throw new BusinessException(ErrorEnum.FILE_UPLOAD_ERROR, "prefix不能以斜杠开头");
        }

        if (ossClient == null) {
            throw new RuntimeException("当前文件服务器类型不支持该Policy方式上传");
        }

        // host的格式为 bucketname.endpoint
        String host = "https://" + ossProperties.getBucket() + "." + ossProperties.getEndpoint();
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String callbackUrl = ossProperties.getCallbackUrl();

        // 用户上传文件时指定的前缀。
        String dir = prefix + "/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8.name());
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = Maps.newLinkedHashMap();
            respMap.put("accessid", ossProperties.getKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            if (StringUtils.isNotBlank(callbackUrl)) {
                JSONObject jasonCallback = new JSONObject();
                jasonCallback.put("callbackUrl", callbackUrl);
                jasonCallback.put("callbackBody",
                        "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
                jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
                String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
                respMap.put("callback", base64CallbackBody);
            }

            return R.ok(respMap);
        } catch (Exception e) {
            log.error("获取OSS的Policy异常：", e);
        }
        return null;
    }
}
