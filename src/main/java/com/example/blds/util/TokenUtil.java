package com.example.blds.util;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    public String createToken (JSONObject userSession){
        String tokenId = UUID.randomUUID().toString().replace("-","");
        redisTemplate.boundValueOps(tokenId).set(userSession.toString(),15000,TimeUnit.SECONDS);
        return tokenId;
    }

    public String checkToken (String token) {

        String userSession = (String) redisTemplate.boundValueOps (token).get ();
        if (userSession == null) {
            return "token无效";
        }
        redisTemplate.boundValueOps (token).expire (15000, TimeUnit.SECONDS);
        return userSession;
    }

    public void deleteToken (String token) {
            redisTemplate.delete (token);
    }

}
