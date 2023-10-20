package com.caychen.boot.common.utils;

import com.caychen.boot.common.constant.CommonConstant;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 19:03
 * @Describe:
 */
public final class MD5 {

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String encrypt(String strSrc) {
        try {
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexDigits[b >>> 4 & 0xf];
                chars[k++] = hexDigits[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorEnum.ENCRYPT_ERROR, e.getMessage());
        }
    }

    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(MD5.class.getName()
                    + "初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配
     *
     * @param password
     *            要校验的字符串
     * @param md5PwdStr
     *            已知的md5校验码
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配
     *
     * @param password
     *            要校验的字符串
     * @param userPassword
     *            数据库密码
     * @return
     */
    public boolean checkPasswordAndUserPwd(String password, String userPassword) {
        String s = getMD5String(password);
        String md5PwdStr = getMD5String(userPassword);
        return s.equals(md5PwdStr);
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return new MD5().bufferToHex(messagedigest.digest());
    }

    /**
     * JDK1.4中不支持以MappedByteBuffer类型为参数update方法，并且网上有讨论要慎用MappedByteBuffer，
     * 原因是当使用 FileChannel.map 方法时，MappedByteBuffer 已经在系统内占用了一个句柄， 而使用
     * FileChannel.close 方法是无法释放这个句柄的，且FileChannel有没有提供类似 unmap 的方法，
     * 因此会出现无法删除文件的情况。
     *
     * 不推荐使用
     *
     * @param bytes
     * @return
     * @throws IOException
     */

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 获取加密后的字符串
     *
     * @param str
     * @return
     */
    public static String stringMD5(String str) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = str.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    /**
     * 数据加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * [生成签名：若含有sign_type字段则必须和signType参数保持一致]
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(CommonConstant.FIELD_SIGN)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();
    }

    /**
     * @Description TODO 生成包含url请求参数map
     * @Date <p/>2020/6/18 上午10:49
     * @author <p/>LY
     * @param  * @param o
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> createGetParamMap(Object o) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String fieldName = field.getName();
            // 获取属性值
            Object fieldValue = getFieldValue(fieldName, o);
            if(fieldValue != null){
                map.put(fieldName, fieldValue.toString());
            }
        }
        return map;
    }
    /**
     * @Description TODO 获取指定属性的属性值
     * @Date <p/>2020/6/18 上午10:49
     * @author <p/>LY
     * @param  * @param fieldName
     * @param o
     * @return java.lang.Object
     */
    public static Object getFieldValue(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = o.getClass().getMethod(getter, new Class[] {});
        Object value = method.invoke(o, new Object[] {});
        return value;
    }
    /**
     * @Description TODO 调用商城生成签名
     * @Date <p/>2020/6/18 上午10:49
     * @author <p/>LY
     * @param  * @param params
     * @return java.lang.String
     */
    public static String sign(Map<String,String> params){
        List<String> keys=new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String string="";
        for(String s:keys){
            string+=params.get(s);
        }
        String sign="";
        try {
            sign = toHexValue(encryptMD5(string.getBytes(Charset.forName("utf-8"))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5 error");
        }
        return sign;
    }

    private static String toHexValue(byte[] messageDigest) {
        if (messageDigest == null){
            return "";
        }

        StringBuilder hexValue = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            int val = 0xFF & aMessageDigest;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private static byte[] encryptMD5(byte[] data)throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

}
