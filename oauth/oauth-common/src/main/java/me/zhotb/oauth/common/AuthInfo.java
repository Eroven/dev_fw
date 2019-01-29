package me.zhotb.oauth.common;

import java.io.Serializable;
import java.util.Objects;

public class AuthInfo implements Serializable {

    @Binder("grant_type")
    private String grantType;

    @Binder("response_type")
    private String responseType;

    @Binder("client_id")
    private String clientId;

    @Binder("client_secret")
    private String clientSecret;

    private String state;

    @Binder("redirect_uri")
    private String redirectUri;

    private String scope;

    private String code;

    @Binder("token_type")
    private String tokenType;

    @Binder("refresh_token")
    private String refreshToken;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthInfo)) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return Objects.equals(clientId, authInfo.clientId) &&
                Objects.equals(redirectUri, authInfo.redirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, redirectUri);
    }
}
