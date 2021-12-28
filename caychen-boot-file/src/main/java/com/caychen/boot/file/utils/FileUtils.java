package com.caychen.boot.file.utils;

import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * @Author: Caychen
 * @Date: 2021/12/24 14:01
 * @Description:
 */
public class FileUtils {

    public static MultipartFile convertTo(InputStream inputStream, String key) {
        DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file", MediaType.ALL_VALUE, true, key);

        try (OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(inputStream, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        MultipartFile multi = new CommonsMultipartFile(fileItem);
        return multi;
    }

    private static FileItem createFileItem(File file) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        FileItem item = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            FileItemFactory factory = new DiskFileItemFactory(16, null);
            String filename = file.getName();
            item = factory.createItem("file", "text/html", false, filename);

            try (OutputStream os = item.getOutputStream()) {
                while ((bytesRead = fis.read(buffer, 0, buffer.length)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
            return item;
        } catch (IOException e) {
            throw new BusinessException(ErrorEnum.FILE_UPLOAD_ERROR, e.getMessage());
        }

    }

    public static MultipartFile getMulFile(File file) throws IOException {
        FileItem fileItem = createFileItem(file);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }
}
