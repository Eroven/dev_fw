package me.zhaotb.web.config;


import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.slf4j.Slf4j;
import me.zhaotb.web.dto.account.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @date 2020/12/17
 */
@Slf4j
@Configuration
public class JwtFilterConfig {

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean(@Qualifier("jwtFilter") Filter jwtFilter) {
        if (jwtConfig.getPatterns() == null || jwtConfig.getPatterns().size() < 1) {
            return null;
        }
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(jwtFilter);
        for (String pat : jwtConfig.getPatterns()) {
            filter.addUrlPatterns(pat.trim());
        }
        return filter;
    }

    @Bean("jwtFilter")
    public Filter jwtFilter() {
        return new JwtFilter();
    }

    private class JwtFilter extends GenericFilterBean {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            String token = req.getHeader("Authorization");
            JwtParser parser = Jwts.parser();
            if (StringUtils.isBlank(token) || !parser.isSigned(token)) {
                ((HttpServletResponse) response).sendError(401, "未授权");
                return;
            }
            try {
                token = token.split(" ")[1];
                parser.setSigningKey(jwtConfig.getSecret());
                parser.parse(token);
                String payload = TextCodec.BASE64URL.decodeToString(token.split("\\.")[1]);
                log.debug("payload: {}", payload);
                UserDto dto = JSONObject.parseObject(payload, UserDto.class);
                if (dto.getExpiration() < System.currentTimeMillis()) {
                    ((HttpServletResponse) response).sendError(401, "授权过期");
                    return;
                }
                req.setAttribute("userDto", dto);
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(401, "无效TOKEN");
                logger.error(token, e);
                return;
            }

            chain.doFilter(request, response);
        }
    }

}
