package com.pksprojects.minis3.config.security;

import com.pksprojects.minis3.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * Created by PKS on 4/8/17.
 */
@Configuration
public class OAuth2Config {

    private static final String API_RESOURCE_ID = "webapi";

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Value("${jwt.sign.key:0xb613679a0814d9ec772f95d778c35fc5ff1697c493715653c6c712144292c5ad}")
    private String key;

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter =  new JwtAccessTokenConverter();
        converter.setSigningKey((key != null) ? key :"0xb613679a0814d9ec772f95d778c35fc5ff1697c493715653c6c712144292c5ad");
        return converter;
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(API_RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                    .antMatchers("/ap/v1/users/register").permitAll();
        }

    }


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired TokenStore tokenStore;

        @Autowired JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private UsersService usersService;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore)
                    .authenticationManager(authenticationManager)
                    .accessTokenConverter(jwtAccessTokenConverter)
                    .userDetailsService(usersService);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("angular")
                    .authorizedGrantTypes("password","refresh_token")
                    .authorities("USER")
                    .scopes("read", "write")
                    .resourceIds(API_RESOURCE_ID)
                    .secret("secret");
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }
    }
}
