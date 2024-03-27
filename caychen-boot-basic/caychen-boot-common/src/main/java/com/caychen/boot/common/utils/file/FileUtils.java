package com.caychen.boot.common.utils.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.caychen.boot.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Caychen
 * @Date: 2024/3/24 19:16
 * @Description:
 */
@Slf4j
public class FileUtils {

    /**
     * 读取文件
     *
     * @param path       文件路径
     * @param isInternal 是否是内部文件
     * @return 文件内容
     */
    public static String readFile(String path, Boolean isInternal) throws IOException {
        return isInternal ? readInternalFile(path) : readExternalFile(path);
    }

    /**
     * 读取外部文件
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    static String readExternalFile(String fileName) throws IOException {
        return FileUtils.read(new FileInputStream(fileName), StandardCharsets.UTF_8.name());
    }

    /**
     * 读取内部/类路径下的文件
     *
     * @param path 文件路径
     * @return 文件内容
     */
    static String readInternalFile(String path) throws IOException {
        return FileUtils.read(FileUtils.class.getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8.name());
    }

    private static String read(InputStream is) throws IOException {
        return read(is, StandardCharsets.UTF_8.name());
    }

    private static String read(InputStream is, String encoding) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(SystemConstant.LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件读取失败，", e);
        }
        return content.toString();
    }

    public static <T> T readJsonFile(String jsonFilePath, TypeReference<T> tr) throws FileNotFoundException {
        try (InputStream is = new FileInputStream(jsonFilePath)) {
            return JSON.parseObject(read(is), tr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
