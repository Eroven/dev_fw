package me.zhaotb.web;


import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author zhaotangbo
 * @since 2020/12/17
 */
@Configuration
public class ErrorConfig implements ErrorPageRegistrar {


    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {

        ErrorPage common = new ErrorPage("/error/common");//默认跳转的错误页面
        ErrorPage p401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");

        registry.addErrorPages(common, p401);
    }
}
