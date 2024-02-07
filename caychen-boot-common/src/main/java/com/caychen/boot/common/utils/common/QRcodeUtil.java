package com.caychen.boot.common.utils.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.UUID;

/**
 * @description: 生成二维码
 **/
public class QRcodeUtil {

    public static String fileStorageUrl;


    protected static final Logger logger = LoggerFactory.getLogger(QRcodeUtil.class);

    /**
     * 二维码尺寸
     */
    public static final int QRCODE_SIZE = 300;

    public static void generateQRcodePic(String fileName, String content) {
        QRcodeUtil.generateQRcodePic(fileName, content, QRcodeUtil.QRCODE_SIZE, QRcodeUtil.QRCODE_SIZE);
    }

    public static void generateQRcodePic(String fileName, String content, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        // bug:二维码本地名称复杂度不够，一秒内并发上来，图片文件会被覆盖掉；
        // 修改二维码服务器文件名称复杂度，添加uuid；
        UUID uuid =  UUID.randomUUID();
        String uuidStr=uuid.toString();
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            Path file = new File(fileName).toPath();
            logger.info("fileAbsolutePath:{}",file.toAbsolutePath());
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", file);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}
