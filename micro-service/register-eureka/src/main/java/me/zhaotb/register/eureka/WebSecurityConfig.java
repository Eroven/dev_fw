package me.zhaotb.register.eureka;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author zhaotangbo
 * @since 2020/11/30
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//由于spring-cloud-security使用了csrf，所以这里要禁用csrf才能正常使用eureka的注册服务
        super.configure(http);
    }
}
