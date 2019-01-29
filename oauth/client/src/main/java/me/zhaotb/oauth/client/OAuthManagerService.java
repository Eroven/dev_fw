package me.zhaotb.oauth.client;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OAuthManagerService {

    private HashMap<String, OAuthInfo> infos = new HashMap<>();

    public OAuthManagerService() {
        OAuthInfo info = new OAuthInfo();
        info.setClientId("abc123");
        info.setAuthUri("jq.server.com:9001/authorization/authorize");
        info.setTokenUri("jq.server.com:9001/authorization/token");
        infos.put("jq", info);
        info = new OAuthInfo();
        info.setClientId("abc1111");
        info.setAuthUri("jq.server.com:9001/authorization/authorize");
        info.setTokenUri("jq.server.com:9001/authorization/token");
        infos.put("jjq", info);
    }

    public OAuthInfo getOAuthInfo(String type){
        return infos.get(type);
    }

}
