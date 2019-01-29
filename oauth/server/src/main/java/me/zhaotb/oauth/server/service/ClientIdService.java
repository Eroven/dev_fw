package me.zhaotb.oauth.server.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClientIdService {

    private Set<String> clientIds = new HashSet<>();

    public ClientIdService() {
        Collections.addAll(clientIds,"abc123","cde456");
    }

    public boolean isvalidClientId(String clientId){
        return clientIds.contains(clientId);
    }

}
