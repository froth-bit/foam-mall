package com.fmwyc.gateway.oauth2Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccessManager accessManager;

    @Bean
    public SecurityWebFilterChain webFluxSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        ReactiveJdbcAuthManager reactiveJdbcAuthManager = new ReactiveJdbcAuthManager(dataSource);
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveJdbcAuthManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());

        serverHttpSecurity
                .httpBasic().disable()//响应式编程和rest不同
                .csrf().disable()
                .authorizeExchange().pathMatchers(HttpMethod.OPTIONS).permitAll() //OPTIONS,兼容ajax调用
                .anyExchange().access(accessManager)
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return serverHttpSecurity.build();
    }

}
