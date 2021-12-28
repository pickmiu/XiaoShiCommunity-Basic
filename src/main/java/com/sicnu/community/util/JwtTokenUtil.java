package com.sicnu.community.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

/**
 * jwt-token的工具类，用于签发token，验证token等
 * 
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class JwtTokenUtil implements Serializable {

    /**
     * 秘钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 过期时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 选用散列算法
     */
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    /**
     * 计算过期时间
     * 
     * @param createdDate
     * @return
     */
    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date createdDate = new Date();
        return Jwts.builder().setClaims(claims).setExpiration(calculateExpirationDate(createdDate)).setSubject(subject)
            .setIssuedAt(createdDate).signWith(signatureAlgorithm, secret).compact();
    }

    public String generateToken(String subject) {
        Date createdDate = new Date();
        return Jwts.builder().setExpiration(calculateExpirationDate(createdDate)).setSubject(subject)
                .setIssuedAt(createdDate).signWith(signatureAlgorithm, secret).compact();
    }

    public Claims parseToken(String token) throws ExpiredJwtException, SignatureException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean isCreateAfterLastPasswordReset(Claims claims, Date passwordChangeDate) {
        Date createDate = claims.getIssuedAt();
        // 如果签发的token未过期并且token在最后一次密码之后签发
        return createDate.after(passwordChangeDate);
    }

    public String refreshToken(String token) {
        Date createdDate = new Date();
        Claims claims = parseToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(calculateExpirationDate(createdDate));
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String refreshToken(Claims claims) {
        Date createdDate = new Date();
        claims.setIssuedAt(createdDate);
        claims.setExpiration(calculateExpirationDate(createdDate));
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

}
