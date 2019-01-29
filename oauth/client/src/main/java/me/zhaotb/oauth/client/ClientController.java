package me.zhaotb.oauth.client;

import com.alibaba.fastjson.JSON;
import me.zhaotb.framework.util.ZipUtil;
import me.zhotb.oauth.common.AuthConst;
import me.zhotb.oauth.common.AuthInfo;
import me.zhotb.oauth.common.StringUtils;
import me.zhotb.oauth.common.TokenObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
@RequestMapping("login")
public class ClientController {

    private final OAuthManagerService oAuthManagerService;

    @Autowired
    public ClientController(OAuthManagerService oAuthManagerService) {
        this.oAuthManagerService = oAuthManagerService;
    }

    @RequestMapping("other/{type}")
    public void otherLogin(@PathVariable(name = "type")String type, HttpServletResponse response){
        OAuthInfo info = oAuthManagerService.getOAuthInfo(type);
        try {
            String uri = info.getAuthUri() + "?grantType=code&clientId=" + info.getClientId() +
                    "&redirectUri=" + DomainUtils.domainContext() +
                    "login/oauth/cb" +
                    "&state=" + type;
            response.sendRedirect(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("oauth/cb")
    public void oauthCallBack(AuthInfo authInfo, TokenObject tokenObject, HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (StringUtils.isBlank(authInfo.getTokenType())){// 授权码回调的情况
            OAuthInfo oAuthInfo = oAuthManagerService.getOAuthInfo(authInfo.getState());
            String tokenUri = oAuthInfo.getTokenUri();
            tokenUri += "?grant_type=" + AuthConst.GrantType.AUTHORIZATION_CODE + "&code=" + authInfo.getCode()
                    + "&redirect_uri=" + DomainUtils.domainContext() + "/login/oauth/cb";
            URI uri = URI.create(tokenUri);
            try {
                URLConnection conn = uri.toURL().openConnection();
                String charset = conn.getHeaderField("charset");
                InputStream inputStream = conn.getInputStream();
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = inputStream.read(buffer)) > 0){
//
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

        }
    }

    @ResponseBody
    @RequestMapping("http")
    public Object http(HttpServletRequest request, HttpServletResponse response){
        String str = "我与父亲不相见已二年余了，我最不能忘记的是他的背影。那年冬天，祖母死了，父亲的差使也交卸了，正是祸不单行的日子，我从北京到徐州，打算跟着父亲奔丧回家。到徐州见着父亲，看见满院狼藉的东西，又想起祖母，不禁簌簌地流下眼泪。父亲说，“事已如此，不必难过，好在天无绝人之路！”";
        str += str;
        str += str;
        List<? extends Serializable> list = Arrays.asList("你好", 123, "abc", str);
        String s = JSON.toJSONString(list);
        System.out.println(s.getBytes().length  );
        String gzip = ZipUtil.gzip(s);
        System.out.println(gzip.getBytes().length);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        return list;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ClientController.class, args);
    }
}
