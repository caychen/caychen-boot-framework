package com.caychen.boot.oss.controller;

import com.caychen.boot.common.response.R;
import com.caychen.boot.oss.service.IOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 15:40
 * @Description:
 */
@Slf4j
@RequestMapping("/v1/oss/file")
@RestController
@Api(tags = "OSS文件服务上传接口")
public class DefaultOssController {

    @Autowired
    private IOssService ossService;

    /**
     * 不能以斜杠开头，否则会报InvalidObjectName
     */
    @Value("${file.upload.path-prefix}")
    private String prefix;

    @ApiOperation("默认的OSS文件服务上传接口")
    @PostMapping("/upload")
    public R<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return R.ok(ossService.fileUpload(prefix, file));
    }
}
