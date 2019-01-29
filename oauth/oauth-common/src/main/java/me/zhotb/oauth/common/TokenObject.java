package me.zhotb.oauth.common;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 指令类, 包含了指令创建时间,值和生命时长
 */
public class TokenObject implements Serializable {

    @Binder("token_type")
    private String tokenType;

    @Binder("token_create_time")
    private Date CreateTime;

    @Binder("access_token")
    private String token;

    @Binder("expires_in")
    private long tokenEffectiveSeconds;

    @Binder("refresh_token")
    private String refreshToken;

    @Binder("refresh_token_expires_in")
    private long refreshTokenEffectiveSeconds;

    @JSONField(name = "token_create_time")
    public Date getCreateTime() {
        return CreateTime;
    }

    @JSONField(name = "token_type")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }

    @JSONField(name = "access_token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JSONField(name = "expires_in")
    public long getTokenEffectiveSeconds() {
        return tokenEffectiveSeconds;
    }

    public void setTokenEffectiveSeconds(long tokenEffectiveSeconds) {
        this.tokenEffectiveSeconds = tokenEffectiveSeconds;
    }

    @JSONField(name = "refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JSONField(name = "refresh_token_expires_in")
    public long getRefreshTokenEffectiveSeconds() {
        return refreshTokenEffectiveSeconds;
    }

    public void setRefreshTokenEffectiveSeconds(long refreshTokenEffectiveSeconds) {
        this.refreshTokenEffectiveSeconds = refreshTokenEffectiveSeconds;
    }
}
