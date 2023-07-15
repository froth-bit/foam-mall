package com.fmwyc.gateway.oauth2Config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

//进行访问 放行 或者 拦截
@Component
public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    //部分url需要直接放行，如：用户注册之类
    private Set<String> permitAll = new ConcurrentSkipListSet<>();//跳表
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AccessManager() {
        permitAll.add("/**/test/**");
    }

    //最终决定权
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        //checkPath
        ServerWebExchange exchange = authorizationContext.getExchange();
        String requestUrl = exchange.getRequest().getURI().getPath();
        return authentication.map(auth -> {
            if (checkPath(requestUrl)) {
                return new AuthorizationDecision(true);
            }
            if (auth instanceof OAuth2Authentication) {
                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) auth;
                String clientId = oAuth2Authentication.getOAuth2Request().getClientId();
                if (StringUtils.isNoneEmpty(clientId)){
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        });
    }

    private boolean checkPath(String requestUrl) {
        return permitAll.stream()
                .anyMatch(p -> antPathMatcher.match(p, requestUrl));
    }

}
