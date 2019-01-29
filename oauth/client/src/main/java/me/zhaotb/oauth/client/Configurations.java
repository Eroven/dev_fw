package me.zhaotb.oauth.client;

import me.zhotb.oauth.common.BinderProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
@Configuration
public class Configurations extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(processor());
    }

    @Bean
    public BinderProcessor processor(){
        return new BinderProcessor(true);
    }
}
