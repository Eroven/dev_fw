package me.zhaotb.oauth.server.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodeGenService {

    public String genUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String genBearer(){
        return genUUID();
    }
}
