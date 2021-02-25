package com.witherview.configuration;


import com.witherview.account.AccountService;
import com.witherview.account.filter.CustomAuthenticationFilter;
import com.witherview.account.filter.JwtAuthenticationFilter;
import com.witherview.constant.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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


@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // static 리소스는 따로 authentication을 적용하지 않음.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
        web.ignoring().antMatchers(
                "/swagger-resources/**", "/v2/api-docs",
                "/webjars/**", "/swagger-ui.html", "/swagger/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .httpBasic().disable() // rest api 이므로 기본설정은 전부 disable;
            .cors()
        .and()
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()
        .and()
            // 세션이 자동으로 유지되면, 헤더 값이 자동으로 지정된다. http 세션을 생성하지 않도록 설정 변경.
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
             .authorizeRequests()
             .antMatchers(HttpMethod.POST, SecurityConstant.SIGN_UP_URL).permitAll()
             .anyRequest().permitAll() // todo: 일단 모든 요청을 통과하도록 설정. 삭제 필요
        .and()
            .addFilter(new CustomAuthenticationFilter(authenticationManager())) // 로그인 url에만 적용되는 필터 (확인필요)
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
       ;
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
