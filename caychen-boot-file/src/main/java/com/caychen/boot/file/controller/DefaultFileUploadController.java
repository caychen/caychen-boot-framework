package com.caychen.boot.file.controller;

import com.caychen.boot.common.response.R;
import com.caychen.boot.file.support.DefaultFileServiceSupport;
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
 * @Date: 2021/9/23 15:14
 * @Description:
 */
@Slf4j
@RequestMapping("/v1/file")
@RestController
@Api(tags = "文件服务控制器")
public class DefaultFileUploadController {

    @Autowired
    private DefaultFileServiceSupport fileService;

    /**
     * 不能以斜杠开头，否则会报InvalidObjectName
     */
    @Value("${file.upload.path-prefix}")
    private String prefix;

    @ApiOperation("默认的文件上传接口")
    @PostMapping("/upload")
    public R<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return R.ok(fileService.uploadFile(prefix, file));
    }
}
