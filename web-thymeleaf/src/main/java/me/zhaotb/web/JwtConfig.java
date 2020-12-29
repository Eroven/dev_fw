package me.zhaotb.web;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean(@Qualifier("jwtFilter") Filter jwtFilter, @Value("${jwt.pattern:}")String pattern){
        if (StringUtils.isBlank(pattern)) return null;

        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(jwtFilter);
        for (String pat : pattern.split(",")) {
            filter.addUrlPatterns(pat.trim());
        }

        return filter;
    }

    @Bean("jwtFilter")
    public Filter jwtFilter(){
        return new JwtFilter();
    }

    private class JwtFilter extends GenericFilterBean {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            String token = req.getHeader("Authorization");
            JwtParser parser = Jwts.parser();
            if (StringUtils.isBlank(token) || !parser.isSigned(token)) {
                ((HttpServletResponse)response).sendError(401, "未授权");
                return;
            }
            try {
                token = token.split(" ")[1];
                parser.setSigningKey(secret);
                Jwt jwt = parser.parse(token);
                Claims claims = (Claims)jwt.getBody();
                log.debug("claims: {}", claims);
                if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
                    ((HttpServletResponse)response).sendError(401, "授权过期");
                    return;
                }
                String user = claims.getSubject();
                req.setAttribute("userCode", user);
            } catch (Exception e){
                ((HttpServletResponse)response).sendError(401, "无效TOKEN");
                logger.error(token, e);
                return;
            }

            chain.doFilter(request, response);
        }
    }

}
