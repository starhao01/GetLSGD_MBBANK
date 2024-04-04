package com.viettelpost.core.cfgs;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    Environment env;

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(new JSONObject()
                        .put("message", "Phiên đăng nhập hết hạn. Cần đăng nhập lại")
                        .put("error", true)
                        .put("data", new JSONObject().put("errorCode", 52))
                        .toString());
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v2/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/csrf").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/get-token").permitAll()
                .antMatchers(HttpMethod.GET, "/user/send-otp").permitAll()
                .antMatchers(HttpMethod.GET, "/user/check-otp").permitAll()
                .antMatchers(HttpMethod.POST, "/user/changePassword").permitAll()
                .antMatchers(HttpMethod.POST, "/user/changePasswordByOtp").permitAll()
                .antMatchers(HttpMethod.POST, "/user/changePassword").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/bank/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/bank/getCaptchaImage").permitAll()
                .antMatchers(HttpMethod.POST, "/api/bank/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/bank/generateRamdomString").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login-noi-bo").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
