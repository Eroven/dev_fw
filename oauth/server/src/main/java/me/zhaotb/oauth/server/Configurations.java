package me.zhaotb.oauth.server;

import me.zhotb.oauth.common.BinderProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
@SpringBootApplication
@Configuration
public class Configurations extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(processor());
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(oAuthInterceptor()).addPathPatterns("/rest/**").excludePathPatterns("/rest/error/**");
    }

    @Bean
    public BinderProcessor processor() {
        return new BinderProcessor(true);
    }

    @Bean
    public RestInterceptor oAuthInterceptor() {
        return new RestInterceptor();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Configurations.class, args);
    }
}
