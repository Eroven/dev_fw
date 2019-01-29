package me.zhotb.oauth.common;

public final class AuthConst {

    /**
     * 简化模式
     */
    public static final String IMPLICIT = "token";

    /**
     * 密码模式
     */
    public static final String RESOURCE_OWNER_PASSWORD_CREDENTIALS = "password";

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "clientcredentials";

    /**
     * 客户端请求类型
     */
    public final class GrantType {
        /**
         * 授权码请求
         */
        public static final String AUTHORIZATION_CODE = "authorization_code";

        /**
         * 刷新授权码请求
         */
        public static final String REFRESH_TOKEN = "refresh_token";

    }

    /**
     * 服务端响应类型
     */
    public final class ResponseType {
        /**
         * 访问码类型
         */
        public static final String RESPONSE_CODE = "code";
    }

    /**
     * 指令类型
     */
    public final class TokenType {
        public static final String BEARER = "bearer";

        public static final String MAC = "mac";
    }
}
