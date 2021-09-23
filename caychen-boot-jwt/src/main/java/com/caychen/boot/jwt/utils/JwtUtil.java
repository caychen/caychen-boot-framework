package com.caychen.boot.jwt.utils;

import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 15:55
 * @Description:
 */
public class JwtUtil {

    /**
     * 生成jwt
     *
     * @param claims    自定义payload信息
     * @param alg       签名算法，可不填；如果不填，默认为HS256
     * @param keyId     jti，jwt的唯一标识，主要用来作为一次性token,从而回避重放攻击
     * @param jwtSecret 自定义的私人密钥secret
     * @param expire    jwt过期时间
     * @return Also
     * @see SignatureAlgorithm
     */
    public static String createJWT(Map<String, Object> claims,
                                   String alg,
                                   String keyId,
                                   String jwtSecret,
                                   Long expire) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        if (StringUtils.isNotBlank(alg)) {
            //如果不为空，则使用传进来的算法
            signatureAlgorithm = SignatureAlgorithm.forName(alg);
        }

        Date now = new Date(System.currentTimeMillis());

        SecretKey secretKey = generateKey(jwtSecret);

        //生成JWT的时间
        Long nowMillis = System.currentTimeMillis();
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(keyId)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey);
        if (expire >= 0) {
            long expMillis = nowMillis + expire;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 验证jwt
     */
    public static Claims verifyJwt(String token, String jwtSecret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generateKey(jwtSecret);
        Claims claims;
        try {
            //得到DefaultJwtParser
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    //解析token
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    /**
     * 解析jwt，解析时若过期会抛出ExpiredJwtException异常
     *
     * @param jwt token
     * @return jwt对象
     */
    public static Claims parseJwt(String jwt, String jwtSecret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generateKey(jwtSecret);

        //获取解析后的对象
        Claims claims = Jwts.parser()
                //设置签名秘钥，和生成的签名的秘钥一模一样
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generateKey(String jwtSecret) {
        if (StringUtils.isNotBlank(jwtSecret)) {
            byte[] encodedKey = Base64.getEncoder().encode(jwtSecret.getBytes(StandardCharsets.UTF_8));
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return key;
        } else {
            throw new BusinessException(ErrorEnum.SECRET_KEY_EMPTY_ERROR);
        }
    }

}
