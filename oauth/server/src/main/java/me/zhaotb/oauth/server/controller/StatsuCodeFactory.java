package me.zhaotb.oauth.server.controller;

/**
 * @author zhaotangbo
 * @date 2018/11/24
 */
public class StatsuCodeFactory {

   public static StatusCode produce(String code, String status, String comment){
       return new StatusCode(code, status, comment);
   }

   public static StatusCode produce(String code, String status){
       return new StatusCode(code, status);
   }

    /**
     * 正常状态码
     */
   public static StatusCode produce200(){
       return produce("200", "OK");
   }

    /**
     * 未授权状态码
     */
   public static StatusCode produce401(){
       return produce("401", "Unauthorized");
   }
}
