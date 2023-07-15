package com.fmwyc.gateway.oauth2Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;

@Component
public class ReactiveJdbcAuthManager implements ReactiveAuthenticationManager {

    @Autowired
    private TokenStore tokenStore;

    public ReactiveJdbcAuthManager(DataSource dataSource) {
        this.tokenStore = new JdbcTokenStore(dataSource);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        //原生的oauth2进行访问时，需要在header里添加token，并以bearer开头
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap(accessToken -> {
                    //将token从db中取出
                    OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                    if (oAuth2AccessToken == null){
                        return Mono.error(new InvalidTokenException("Token not found in Token store !"));
                    }
                    if (oAuth2AccessToken.isExpired()){
                        return Mono.error(new InvalidTokenException("Token is expired"));
                    }
                    OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(accessToken);
                    if (oAuth2Authentication ==null){
                        return Mono.error(new InvalidTokenException("Invalid token"));
                    }
                    if (!oAuth2Authentication.isAuthenticated()){
                        return Mono.error(new InvalidTokenException("Token not Authenticated"));
                    }
                    return null;
                });
    }
}
