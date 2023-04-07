package com.caychen.boot.common.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Caychen
 * @Date: 2023/4/7 19:42
 * @Description:
 *      随机生成短链接工具类，通过短链接和长链接的映射关系，进行访问短链接的时候，重定向到长链接
 */
@Slf4j
public class ShortUrlGenUtil {

    /**
     * 生成短链接
     *
     * @param longUrl： 原始长链接
     * @return
     */
    public String genShortUrl(String longUrl) {
        //生成短链接 使用Murmurhash3转int在转为62进制正加a负加b最后长度为7的值
        HashFunction hf = Hashing.murmur3_128();
        int i = hf.newHasher().putString(longUrl, Charsets.UTF_8).hash().asInt();
        // 布隆过滤
        Boolean aBoolean = BloomFilterUtil.bloomFilterUrl(i);
        // 在使用布隆算法解决哈希冲突
        String surl = "";
        if (aBoolean) {
            // 存在返回短链接
            surl = createSurl(i);
        } else {
            // 不存在 进行保存
            BloomFilterUtil.bloomFilterUrlAdd(i);
            surl = createSurl(i);
        }
        return surl;
    }

    /**
     * 进行Murmurhash3 操作
     *
     * @param number
     * @return
     */
    private String createSurl(Integer number) {
        String src = "";
        // 判读是否大于零，并转为62进制
        if (number > 0) {
            src = BloomFilterUtil.encode(number, 6);
            src = src + "a";
        } else {
            String si = String.valueOf(number);
            String substring = si.substring(1);
            Integer integer = Integer.valueOf(substring);
            src = BloomFilterUtil.encode(integer, 6);
            src = src + "b";
        }
        log.info("生成的短链接为：[{}]", src);
        return src;
    }
}
