package me.zhaotb.oauth.server.controller;

import com.alibaba.fastjson.JSONObject;
import me.zhaotb.oauth.server.User;
import me.zhaotb.oauth.server.service.ClientIdService;
import me.zhaotb.oauth.server.service.LogInService;
import me.zhaotb.oauth.server.service.OAuthManagerService;
import me.zhotb.oauth.common.AuthConst;
import me.zhotb.oauth.common.AuthInfo;
import me.zhotb.oauth.common.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

@Controller
@RequestMapping("authorization")
public class AuthorizationCenterController {

    private final ClientIdService clientIdService;
    private final LogInService logInService;
    private final OAuthManagerService oAuthManagerService;
    
    @Autowired
    public AuthorizationCenterController(ClientIdService clientIdService, LogInService logInService, OAuthManagerService oAuthManagerService) {
        this.clientIdService = clientIdService;
        this.logInService = logInService;
        this.oAuthManagerService = oAuthManagerService;
    }

    /*
     * 获取授权码
     */
    @RequestMapping("authorize")
    public String toAuthorize(HttpSession session, AuthInfo authInfo){
        if (!AuthConst.ResponseType.RESPONSE_CODE.equalsIgnoreCase(authInfo.getResponseType())){
            return "error : 不支持授权模式 : " + authInfo.getResponseType();
        }
//        if (!clientIdService.isvalidClientId(authInfo.getClientId())){
//            return "error: 无效ClientId :" + authInfo.getClientId() ;
//        }
        session.setAttribute("authInfo", authInfo);
        return "login.html";
    }

    @ResponseBody
    @RequestMapping("scope")
    public String scoe(HttpSession session){
        Object obj = session.getAttribute("authInfo");
        if (obj instanceof  AuthInfo){
            AuthInfo authInfo = (AuthInfo) obj;
            return authInfo.getScope();
        }
        return "all";
    }

    @RequestMapping("login")
    public void login(@RequestParam("user") String user, @RequestParam("password") String password, HttpSession session, HttpServletResponse response){
        User unKnowUser = new User(user, password);
        User validUser = logInService.login(unKnowUser);
        if (validUser != null){
            Object obj = session.getAttribute("authInfo");
            if (obj instanceof  AuthInfo){
                AuthInfo authInfo = (AuthInfo) obj;
                HashSet<String> scopes = new HashSet<>();
                String scope = authInfo.getScope();
                String scopeStr = scope.trim();
                String[] split = scopeStr.split(";");
                Collections.addAll(scopes, split);
                validUser.setScopes(scopes);
                String code = oAuthManagerService.giveCode(authInfo, validUser);
                StringBuilder uri = new StringBuilder(authInfo.getRedirectUri());
                uri.append("?code=").append(code);
                if (authInfo.getState() != null) {
                    uri.append("&state=").append(authInfo.getState());
                }
                try {
                    response.sendRedirect(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("token")
    public void getToken(AuthInfo authInfo,HttpServletResponse response){
        try {
            TokenObject token = oAuthManagerService.getToken(authInfo);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            String content = JSONObject.toJSONString(token);
            out.write(content.getBytes("utf-8"));
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

}
