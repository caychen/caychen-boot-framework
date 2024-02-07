package com.caychen.boot.common.utils.common;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 压缩文件工具类
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    private ZipUtil() {
    }

    /**
     * 解压zip文件
     *
     * @param sourceFilename 压缩文件
     * @param targetDir      解压路径
     */
    public static void extract(String sourceFilename, String targetDir) throws IOException {
        extract(new File(sourceFilename), targetDir);
    }

    public static void extract(File sourceFile, String targetDir) throws IOException {
        long start = System.currentTimeMillis();
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("cannot find file " + sourceFile.getPath());
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(sourceFile, Charset.forName("GBK"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = targetDir + "/" + entry.getName();
                    createDirIfNotExist(dirPath);
                } else {
                    File targetFile = new File(targetDir + "/" + entry.getName());
                    createFileIfNotExist(targetFile);
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }
            }
            logger.info("extract done. cost: " + (System.currentTimeMillis() - start) + " ms");
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    logger.warn("close zipFile exception", e);
                }
            }
        }
    }

    private static void createDirIfNotExist(String path) {
        createDirIfNotExist(new File(path));
    }

    private static void createDirIfNotExist(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

    private static void createParentDirIfNotExist(File file) {
        createDirIfNotExist(file.getParentFile());
    }

    /**
     * 解压rar文件
     *
     * @param rarPath         压缩文件
     * @param destinationPath 解压路径
     */
    public static void extractRar(String rarPath, String destinationPath) throws IOException {
        try {
            Path rarFile = Paths.get(rarPath);
            if (Files.notExists(rarFile)) {
                throw new FileNotFoundException("cannot find file " + rarFile);
            }
            Files.createDirectories(Paths.get(destinationPath));
            Junrar.extract(rarPath, destinationPath);
        } catch (IOException | RarException e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e.getMessage(), e.getCause());
        }
    }
}
