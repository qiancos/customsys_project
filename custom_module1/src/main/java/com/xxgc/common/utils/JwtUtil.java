package com.xxgc.common.utils;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;



@Component
public class JwtUtil {
    //有效期
    private static final long JWT_EXPIRE= 60*60*1000L; //半个小时

    //令牌密钥，加密字符
    private static final String JWT_KEY="123456";
    //创建token字符串
    public String createToken(Object data){
        //当时时间
        long currentTime=System.currentTimeMillis();
        //过期时间
        long expTime =currentTime+JWT_EXPIRE;
        //JwtBuilder对象用来构建jwt
        /*
        * setId设置jti(jwt,Id):是JWT的唯一标识，根据业务需要，这个可以设置
        * 为一个不重复的值，主要用来作为一次性的token，从而回避重要攻击
        *
        * setSubject(subject):sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个
          json格式的字符串。
            setIssuer设置发布人
            setIssuedAtjwt的签发时间
            signWith设置签名使用的签名算法和签名使用的密匙
            setExpiration设置超时时间
        * */

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID()+"")
                .setSubject(JSON.toJSONString(data))
                .setIssuer("system")
                .setIssuedAt(new Date(currentTime))
                .signWith(SignatureAlgorithm.HS256,encodeSecret(JWT_KEY))
                .setExpiration(new Date(expTime));
        return builder.compact();
    }
    //对密钥匙加密
    /*
    * secretKeySpec用于生成和管理加密密钥的类，它接受一个byte数组，用来构建密钥
    * 然后指定一种加密算法（如AES，DES）
    * */

    private SecretKey encodeSecret(String key) {
        //base64加密进行加密
        byte[] encode = Base64.getEncoder().encode(key.getBytes());
        SecretKeySpec aes = new SecretKeySpec(encode,0,encode.length,"AES");
        return aes;
    }
    public Claims parseToken(String token){
        Claims body=Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    public <T> T parseToken(String  token,Class<T> clazz){
        Claims body=Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return JSON.parseObject(body.getSubject(),clazz);
    }
}
