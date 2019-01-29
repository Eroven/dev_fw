package me.zhaotb.oauth.server.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.zhaotb.oauth.server.User;
import me.zhotb.oauth.common.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 访问控制服务
 * @author zhaotangbo
 * @date 2018/11/23
 */
@Service
public class AccessHandlerService {

    private final Cache<String, User> cache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

    /**
     * 建立指令与用户的访问关系
     * @param accessToken 访问指令
     * @param user 用户
     * @return true :正常建立关系, false : 建立关系失败, accessToken重复
     */
    public boolean addUser(String accessToken, User user){
        User present = cache.getIfPresent(accessToken);
        if (present != null){
            return false;
        }
        cache.put(accessToken, user);
        return true;
    }

    public User accessUser(String accessToken){
        if (StringUtils.isEmpty(accessToken)){
            return null;
        }
        String[] split = accessToken.split("\\s+");
        String token = split[split.length - 1];
        return cache.getIfPresent(token);
    }

    public User remove(String accessToken){
        User user = cache.getIfPresent(accessToken);
        cache.invalidate(accessToken);
        return user;
    }

}
