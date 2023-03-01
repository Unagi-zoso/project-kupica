package com.litaa.projectkupica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final Environment env;

    public SecurityConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            setLocalMode(http);
        return http.build();//.authorizeRequests().antMatchers("/").permitAll().and().build();
    }

    private boolean isLocalMode() {
        String profile = env.getActiveProfiles().length > 0? env.getActiveProfiles()[0] : "local";
        return profile.equals("local");
    }

    private void setLocalMode(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/**", "/me", "/h2-console/**", "/login/**", "/js/**", "/css/**", "/image/**", "/fonts/**", "/favicon.ico").permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable()
        ;
    }
}

