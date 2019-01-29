package me.zhaotb.oauth.server.service;

import me.zhaotb.oauth.server.User;
import me.zhotb.oauth.common.AuthConst;
import me.zhotb.oauth.common.AuthInfo;
import me.zhotb.oauth.common.StringUtils;
import me.zhotb.oauth.common.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OAuthManagerService {

    private final CodeGenService tokenGenService;

    private Map<AuthInfo, String> authMap = new ConcurrentHashMap<>();
    private Map<AuthInfo, String> refreshTokenMap = new ConcurrentHashMap<>();
    private Map<String, String> refreshToken2Token = new ConcurrentHashMap<>();

    private Map<String, User> token2User = new ConcurrentHashMap<>();

    private final AccessHandlerService accessHandlerService;

    @Autowired
    public OAuthManagerService(CodeGenService tokenGenService, AccessHandlerService accessHandlerService) {
        this.tokenGenService = tokenGenService;
        this.accessHandlerService = accessHandlerService;
    }

    /**
     * 给对应授权请求赋予授权码. <br>
     * 每个授权码与clientId & redirectUri 唯一绑定, 同一个clientId & redirectUri 再次发起请求会覆盖前一个.<br>
     * 一个授权码会绑定一个用户信息.<br>
     * @param user 指定用户
     * @return 授权码
     */
    public String giveCode(AuthInfo authInfo, User user){
        String code = tokenGenService.genUUID();
        authMap.put(authInfo, code);
        token2User.put(code, user);
        return code;
    }

    /**
     *
     * @param authInfo 请求授权信息
     * @return 返回token
     * @throws IllegalAccessException 当该请求未先获取访问码 或者 访问码失效,则抛出此异常
     */
    public TokenObject getToken(AuthInfo authInfo) throws IllegalAccessException {
        String grantType = authInfo.getGrantType();
        if (StringUtils.isEmpty(grantType)){
            throw new IllegalArgumentException("grant_type 不能为空");
        }
        if (AuthConst.GrantType.AUTHORIZATION_CODE.equals(grantType)) {
            return firstToken(authInfo);
        }else if (AuthConst.GrantType.REFRESH_TOKEN.equals(grantType)){
            return refreshToken(authInfo);
        }
        throw new IllegalArgumentException("未知grant_type : " + grantType);
    }

    private TokenObject firstToken(AuthInfo authInfo) throws IllegalAccessException {
        String code = authMap.get(authInfo);
        if (StringUtils.isEmpty(code) || !code.equals(authInfo.getCode())) {
            throw new IllegalAccessException("未知访问码: " + authInfo.getCode());
        }
        User user = token2User.get(code);
        String token = tokenGenService.genBearer();
        if(!accessHandlerService.addUser(token, user)){//授权码重复
            return firstToken(authInfo);
        }
        authMap.remove(authInfo);
        token2User.remove(code);

        String refreshToken = tokenGenService.genUUID();
        refreshToken2Token.put(refreshToken, token);
        TokenObject tokenObj = buildTokenObj(token, refreshToken);
        refreshTokenMap.put(authInfo, refreshToken);
        return tokenObj;
    }

    /**
     *
     * @param authInfo 请求授权信息
     * @return 授权指令对象
     * @throws IllegalAccessException 刷新码无效时抛出此异常
     */
    private TokenObject refreshToken(AuthInfo authInfo) throws IllegalAccessException {
        String refreshToken = refreshTokenMap.get(authInfo);
        if(StringUtils.isEmpty(refreshToken) || !refreshToken.equals(authInfo.getRefreshToken())){
            throw new IllegalAccessException("无效刷新指令: " + authInfo.getRefreshToken());
        }
        refreshTokenMap.remove(authInfo);

        String preToken = refreshToken2Token.remove(refreshToken);
        String token = tokenGenService.genBearer();
        User user = accessHandlerService.remove(preToken);
        if(!accessHandlerService.addUser(token, user)){
            return refreshToken(authInfo);
        }

        String nextRefreshToken = tokenGenService.genUUID();
        refreshToken2Token.put(nextRefreshToken, token);
        TokenObject tokenObject = buildTokenObj(token, nextRefreshToken);
        refreshTokenMap.put(authInfo, nextRefreshToken);
        return tokenObject;
    }

    private TokenObject buildTokenObj(String token, String refreshToken) {
        TokenObject tokenObj = new TokenObject();
        tokenObj.setTokenType(AuthConst.TokenType.BEARER);
        tokenObj.setCreateTime(new Date());
        tokenObj.setToken(token);
        tokenObj.setTokenEffectiveSeconds(60);
        tokenObj.setRefreshToken(refreshToken);
        tokenObj.setRefreshTokenEffectiveSeconds(2000000);
        return tokenObj;
    }
}
