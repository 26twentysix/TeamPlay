package com.lilangel.teamplay.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.httpBasic().disable();
        security.authorizeRequests()
                .antMatchers("/**").access("hasIpAddress(\"127.0.0.1\") or hasIpAddress(\"::1\")");
    }
}
