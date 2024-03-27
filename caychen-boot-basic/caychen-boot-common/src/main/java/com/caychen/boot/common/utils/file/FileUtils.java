package com.caychen.boot.common.utils.file;

import com.caychen.boot.common.constant.SystemConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
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
    public static String readExternalFile(String fileName) throws IOException {
        return FileUtils.read(new FileInputStream(fileName), StandardCharsets.UTF_8.name());
    }


    /**
     * 读取内部/类路径下的文件
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String readInternalFile(String path) throws IOException {
        return FileUtils.read(FileUtils.class.getClassLoader().getResourceAsStream(path), StandardCharsets.UTF_8.name());
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

    public static <T> T readJsonFileToObject(String jsonPath) {
        try (Reader reader = new FileReader(jsonPath)) {
            // 将 JSON 文件内容转换为指定类型的对象
            Type type = new TypeToken<T>() {}.getType();
            T t = new Gson().fromJson(reader, type);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("读取json文件异常，", e);
        }
        return null;
    }

}
