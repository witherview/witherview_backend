package com.witherview.account.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import util.SecurityConstant;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // static 리소스는 따로 authentication을 적용하지 않음.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
        web.ignoring().antMatchers(
                "/swagger-resources/**", "/v2/api-docs",
                "/webjars/**", "/swagger-ui.html", "/swagger/**", "/profiles/**");
        // todo: 로컬이미지 접근을 위해 일시적으로 풀어둠. aws s3로 저장할 경우 profiles/ 경로는 제외.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic().disable() // rest api 이므로 기본설정은 전부 disable;
//            .cors()
//        .and()
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()
        .and()
            // 세션이 자동으로 유지되면, 헤더 값이 자동으로 지정된다. http 세션을 생성하지 않도록 설정 변경.
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
             .authorizeRequests()
             .antMatchers(HttpMethod.POST, "/register").permitAll()
             .antMatchers(HttpMethod.POST, "/login").permitAll()
             .antMatchers(HttpMethod.POST, "/api/myinfo/password-reset").permitAll()
             .antMatchers(HttpMethod.GET, "/api/myinfo/password-reset**").permitAll()
             .antMatchers(HttpMethod.PATCH, "/api/myinfo/password-reset**").permitAll()
             .antMatchers(HttpMethod.GET, "/oauth/user/*").permitAll()
             .antMatchers(HttpMethod.POST, "/oauth/user").permitAll()
             .anyRequest().authenticated() // todo: 일단 모든 요청을 통과하도록 설정. 삭제 필요
        .and()
            .oauth2ResourceServer()
            .jwt() // oauth2에서 생성한 토큰의 validation check.
       ;
    }



//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
