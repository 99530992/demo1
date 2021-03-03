package com.han.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

public class JWTUtils {
    private static final long EXPIRE_TIME = 5*60*60*1000L;

    private static String SIGN = SaltUtils.getSalt(6);

    //创建token
    public static String createToken(HashMap<String,String> map){
        JWTCreator.Builder builder = JWT.create();

        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        builder.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME));

        String token = builder.sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    //解析token
    public static DecodedJWT decodedToken(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }
}
