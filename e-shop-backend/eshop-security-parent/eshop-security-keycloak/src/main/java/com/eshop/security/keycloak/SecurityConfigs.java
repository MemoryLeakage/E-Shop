package com.eshop.security.keycloak;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableWebSecurity
@KeycloakConfiguration
public class SecurityConfigs extends KeycloakWebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigs.class);

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/actuator/health", "/v1/products/images/?*")
                .permitAll()
                .mvcMatchers(HttpMethod.GET,"/v1/products/?*").permitAll()
                .mvcMatchers(HttpMethod.GET,"/v1/user/info").authenticated()
                .mvcMatchers(HttpMethod.POST,"/v1/cart/item").authenticated()
                .mvcMatchers(HttpMethod.POST,"/v1/products", "/v1/products/*/images").hasRole("MERCHANT")
                .mvcMatchers(HttpMethod.GET,"/v1/cart").authenticated()
                .mvcMatchers(HttpMethod.DELETE,"/v1/products/images/*").hasRole("MERCHANT")
                .mvcMatchers(HttpMethod.GET,"/v1/products").permitAll()
                .anyRequest().denyAll()
                .and().csrf().disable();
    }


    @Value("classpath:WEB-INF/keycloak.json")
    private Resource keycloakConfigFileResource;

    @Value("${KEYCLOAK_AUTH_URL}")
    private String keycloakServerUrl;

    @Bean
    @Override
    protected AdapterDeploymentContext adapterDeploymentContext() {
        AdapterConfig config;
        try (InputStream inputStream = keycloakConfigFileResource.getInputStream()) {
            config = KeycloakDeploymentBuilder.loadAdapterConfig(inputStream);
            config.setAuthServerUrl(keycloakServerUrl);
            KeycloakDeployment keycloakDeployment = KeycloakDeploymentBuilder.build(config);
            return new AdapterDeploymentContext(keycloakDeployment);
        } catch (IOException e) {
            logger.error("Unable to read keycloak configuration file cause: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
