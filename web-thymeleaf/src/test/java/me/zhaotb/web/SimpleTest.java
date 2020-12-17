package me.zhaotb.web;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Base64;
import java.util.Date;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
public class SimpleTest {

    String secret = "abcdefghijkmpl";

    @Test
    public void testCreateJwt(){

        String user = "admin2";
        String compact = Jwts.builder()
                .setSubject(user)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        System.out.println(compact);
    }

    @Test
    public void testParseJwt(){
        String txt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2MDgxNzY1NTl9.-1BctkhfR9SV-IO7f4V3n0Ren6XdI8wu5Bso9ks4YzA";
//        String txt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjIiLCJpYXQiOjE2MDgxNzYyMDZ9.";
        JwtParser parser = Jwts.parser();
        boolean signed = parser.isSigned(txt);
        System.out.println(signed);
        Jwt jwt = parser
                .setSigningKey(secret)
                .parse(txt);
        System.out.println(jwt);

    }

    @Test
    public void testDynamicArrayParameter(){
        String[] arr = "abc,cde,fff".split(",");
        print(arr);
    }
    void print(String...arr) {
        if (arr == null) System.out.println("input is null");
        else {
            for (String s : arr) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }
}
