package com.caychen.boot.jwt.utils.abstracts;

import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.jwt.utils.JwtUtil;
import io.jsonwebtoken.Claims;

import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 17:44
 * @Description:
 */
public abstract class AbstractValidatorService {

    /**
     * 判断jwt是否过期，可以用于jwt的过期，或者后续的token刷新
     *
     * @param
     * @param jwtSecret
     * @return
     */
    public Boolean checkExpire(String token, String jwtSecret) {
        Claims claims = JwtUtil.verifyJwt(token, jwtSecret);
        if (claims == null) {
            throw new BusinessException(ErrorEnum.TOKEN_HAS_INVALID_ERROR);
        }

        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 校验token中的payload的具体参数是否有效
     *
     * @param token
     * @return
     */
    public Boolean checkValidation(String token, String jwtSecret) {
        Claims claims = JwtUtil.verifyJwt(token, jwtSecret);
        if (claims == null) {
            throw new BusinessException(ErrorEnum.TOKEN_HAS_INVALID_ERROR);
        }

        return this.doCheckClaims(claims);
    }

    /**
     * 具体校验token中的payload的具体参数是否有效
     *
     * @param claims
     * @return
     */
    protected abstract Boolean doCheckClaims(Claims claims);

}
