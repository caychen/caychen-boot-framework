package com.caychen.boot.file.abstracts;

import com.caychen.boot.common.constant.DateConstant;
import com.caychen.boot.common.constant.SymbolConstant;
import com.caychen.boot.common.utils.DateUtil;
import com.caychen.boot.common.utils.StringUtils;
import com.caychen.boot.common.utils.UUIDUtil;
import com.caychen.boot.file.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 14:17
 * @Description:
 */
@Slf4j
public abstract class AbstractFileService implements IFileService {

    protected abstract String fileUpload(String fileKey, File file);

    public abstract String fileType();

    @Override
    public String uploadFile(String prefix, MultipartFile file) throws IOException {


        prefix = filterInvalidSymbol(prefix);

        //获取文件名称
        String fileName = file.getOriginalFilename();

        String uuid = UUIDUtil.getUUIDWithReplaceBySpace();

        String datePath = DateUtil.formatDate(new Date(), DateConstant.YYYY_MM_DD_SLASH);
        String path = prefix + SymbolConstant.SLASH + datePath;

        if (StringUtils.startsWith(path, SymbolConstant.SLASH)) {
            //防止prefix是个空串
            path = StringUtils.substring(path, 1);
        }

        log.info("文件路径为：[{}]", path);

        String tmpPath = path + SymbolConstant.SLASH + uuid;
        File uploadFile = File.createTempFile(tmpPath, fileName);
        file.transferTo(uploadFile);

        String filepath = path + SymbolConstant.SLASH + uuid + fileName;
        //抽象方法
        String url = this.fileUpload(filepath, uploadFile);

        //切割，把sign去掉
        if (url.indexOf(SymbolConstant.QUESTION) != -1) {
            url = url.split("[?]")[0];
        }

        //http替换成https
        url = url.replaceFirst("http://", "https://");

        if (uploadFile != null) {
            uploadFile.delete();
        }
        return url;
    }

    private String filterInvalidSymbol(String prefix) {
        //去除两边多余的空格
        prefix = StringUtils.trim(prefix);

        //去除两边多余的斜杠
        prefix = prefix
                .replaceAll("^/*", "")
                .replaceAll("/*$", "");

        //再去除两边多余的空格
        prefix = StringUtils.trim(prefix);

        return prefix;
    }
}
