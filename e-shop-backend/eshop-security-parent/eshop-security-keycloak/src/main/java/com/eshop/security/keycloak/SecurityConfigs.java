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

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfigs.class);

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
                .antMatchers("/actuator/health")
                .permitAll()
                .anyRequest().denyAll();
    }


    @Value("classpath:WEB-INF/keycloak.json")
    private Resource keycloakConfigFileResource;

    @Value("${KEYCLOAK_AUTH_URL}")
    private String keycloakServerUrl;

    @Override
    protected AdapterDeploymentContext adapterDeploymentContext(){
        AdapterConfig config;
        try (InputStream inputStream = keycloakConfigFileResource.getInputStream()) {
            config = KeycloakDeploymentBuilder.loadAdapterConfig(inputStream);
            config.setAuthServerUrl(keycloakServerUrl);
            KeycloakDeployment keycloakDeployment = KeycloakDeploymentBuilder.build(config);
            return new AdapterDeploymentContext(keycloakDeployment);
        } catch (IOException e) {
            LOG.error("Unable to read keycloak configuration file cause: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
