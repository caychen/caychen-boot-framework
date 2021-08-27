package com.caychen.file.abstracts;

import com.caychen.common.constant.DateConstant;
import com.caychen.common.constant.SymbolConstant;
import com.caychen.common.enums.ErrorEnum;
import com.caychen.common.exception.BusinessException;
import com.caychen.common.utils.DateUtil;
import com.caychen.common.utils.StringUtils;
import com.caychen.file.IFileService;
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
        if (StringUtils.startsWith(prefix, SymbolConstant.SLASH)) {
            throw new BusinessException(ErrorEnum.FILE_UPLOAD_ERROR, "prefix不能以斜杠开头");
        }

        //获取文件名称
        String fileName = file.getOriginalFilename();

        String uuid = StringUtils.getUUIDWithReplaceBySpace();

        String datePath = DateUtil.formatDate(new Date(), DateConstant.YYYY_MM_DD_SLASH);
        String path = prefix + SymbolConstant.SLASH + datePath;
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
}
