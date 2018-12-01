package com.lilianghui.shiro.spring.starter.helper;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public final class TokenManager {
//    /**
//     * 数据请求返回码
//     */
//    /**
//     * 成功
//     */
//    public static final int RESCODE_SUCCESS = 1000; //成功
//    public static final int RESCODE_SUCCESS_DATA = 1001; //成功(有返回数据)
//    public static final int RESCODE_NOEXIST = 1004; //查询结果为空
//    /**
//     * 失败
//     */
//    public static final int RESCODE_EXCEPTION = 1002; //请求抛出异常
//    public static final int RESCODE_EXCEPTION_DATA = 1008; //异常带数据
//    public static final int RESCODE_NOLOGIN = 1003; //未登陆状态
//    public static final int RESCODE_NOAUTH = 1005; //无操作权限
//    public static final int RESCODE_LOGINEXPIRE = 1009; //登录过期
//    /**
//     * token（暂时没有刷新自动token机制，通过重新登录获取）
//     */
//    public static final int RESCODE_REFTOKEN_MSG = 1006; //刷新TOKEN(有返回数据)
//    public static final int RESCODE_REFTOKEN = 1007; //刷新TOKEN

//    private static final int JWT_ERRCODE_EXPIRE = 4001; //Token过期
//    private static final int JWT_ERRCODE_FAIL = 4002; //验证不通过
//    private static final Map<Integer, String> JWT_ERRCODE_MAP = new HashMap<>();
//
//    static {
//        JWT_ERRCODE_MAP.put(JWT_ERRCODE_EXPIRE, "Token过期");
//        JWT_ERRCODE_MAP.put(JWT_ERRCODE_FAIL, "Token不合法,验证不通过");
//    }


    private SecretKey SECRET_KEY = null;

    /**
     * jwt
     */

    private String jwtId; //jwtid

    public TokenManager() {
        init(Long.toString(System.currentTimeMillis()), Helper.getUUID().generate());
    }

    public TokenManager(String jwtId, String jwtSecert) {
        init(jwtId, jwtSecert);
    }

    private void init(String jwtId, String jwtSecert) {
        this.jwtId = jwtId;
        byte[] encodedKey = Base64.decodeBase64(jwtSecert);
        SECRET_KEY = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 签发JWT
     *
     * @param audience
     * @param subject
     * @param ttlMillis 过期时间
     * @return
     * @throws Exception
     */
    public String createJWT(String audience, String subject) {
        return createJWT(audience, subject, System.currentTimeMillis(), -1);
    }

    /**
     * 签发JWT
     *
     * @param audience
     * @param subject
     * @param ttlMillis 过期时间
     * @return
     * @throws Exception
     */
    public String createJWT(String audience, String subject, long ttlMillis) {
        return createJWT(audience, subject, System.currentTimeMillis(), ttlMillis);
    }

    public String createJWT(String audience, String subject, long nowMillis, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setId(jwtId).setSubject(subject)//主题，也差不多是个人的一些信息
                .setAudience(audience) //个人签名
                .setIssuedAt(now). //创建时间
                signWith(signatureAlgorithm, SECRET_KEY); //估计是第三段密钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            //系统时间之前的token都是不可以被承认的
            builder.setExpiration(expDate).setNotBefore(now);
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public Result validateJWT(String jwtStr) {
        Result checkResult = new Result();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            checkResult.setStatus(JwtTokenStatus.JWT_SUCCESS);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setStatus(JwtTokenStatus.JWT_ERRCODE_EXPIRE);
        } catch (SignatureException e) {
            checkResult.setStatus(JwtTokenStatus.JWT_ERRCODE_FAIL);
        } catch (Exception e) {
            checkResult.setStatus(JwtTokenStatus.JWT_ERRCODE_FAIL);
        }
        return checkResult;
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }

    public static class Result {

        private JwtTokenStatus status;

        private Claims claims;

        public JwtTokenStatus getStatus() {
            return status;
        }

        public void setStatus(JwtTokenStatus status) {
            this.status = status;
        }

        public Claims getClaims() {
            return claims;
        }

        public void setClaims(Claims claims) {
            this.claims = claims;
        }

        public boolean isSuccess() {
            return status == JwtTokenStatus.JWT_SUCCESS;
        }
    }

    public static enum JwtTokenStatus {
        JWT_SUCCESS(0, ""), JWT_ERRCODE_EXPIRE(4001, "Token过期"), JWT_ERRCODE_FAIL(4002, "Token不合法,验证不通过"), JWT_DONT_EXIST(4003, "Token不存在"), JWT_DONT_AUTHORITY(4004, "没有权限");

        JwtTokenStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
