package com.eshop.security.test;


import com.eshop.security.keycloak.SecurityConfigs;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KeycloakSecurityContextTest.TestController.class)
@ContextConfiguration(classes= {KeycloakSecurityContextTest.TestController.class, SecurityConfigs.class})
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@TestPropertySource(locations = "classpath:wiremock.properties")
public class KeycloakSecurityContextTest {

    private static RsaJsonWebKey rsaJsonWebKey;

    private static boolean testSetupIsCompleted = false;

    @Value("${wiremock.server.baseUrl}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.aud}")
    private String expectedAud;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AdapterDeploymentContext context;
    @BeforeEach
    public void setUp() throws IOException, JoseException {
        if (!testSetupIsCompleted) {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");
            rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
            rsaJsonWebKey.setUse("sig");


            String openidConfig = "{\n" +
                    "  \"issuer\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "\",\n" +
                    "  \"authorization_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/auth\",\n" +
                    "  \"token_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/token\",\n" +
                    "  \"token_introspection_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/token/introspect\",\n" +
                    "  \"userinfo_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/userinfo\",\n" +
                    "  \"end_session_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/logout\",\n" +
                    "  \"jwks_uri\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/certs\",\n" +
                    "  \"check_session_iframe\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/login-status-iframe.html\",\n" +
                    "  \"registration_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/clients-registrations/openid-connect\",\n" +
                    "  \"introspection_endpoint\": \"" + keycloakBaseUrl + "/auth/realms/" + keycloakRealm + "/protocol/openid-connect/token/introspect\"\n" +
                    "}";
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(String.format("/auth/realms/%s/.well-known/openid-configuration", keycloakRealm)))
                    .willReturn(WireMock.aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(openidConfig)
                    )
            );
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(String.format("/auth/realms/%s/protocol/openid-connect/certs", keycloakRealm)))
                    .willReturn(WireMock.aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(new JsonWebKeySet(rsaJsonWebKey).toJson())
                    )
            );

            testSetupIsCompleted = true;
        }
    }

    @Test
    public void givenRequestToActuatorHealth_whenServing_thenReturnExpected() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/actuator/health"));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello"));
    }

    @Test
    public void givenRequestToNonRestEndpoint_whenServing_thenDenyAccessByDefault() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/" + UUID.randomUUID().toString()));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401));


        resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/" + UUID.randomUUID().toString())
                        .header("Authorization", String.format("Bearer %s", generateJWT(true))));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(403));


    }


    private String generateJWT(boolean withTenantClaim) throws JoseException {

        JwtClaims claims = new JwtClaims();
        claims.setJwtId(UUID.randomUUID().toString());
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setNotBeforeMinutesInThePast(0);
        claims.setIssuedAtToNow();
        claims.setAudience("account");
        claims.setIssuer(String.format("%s/auth/realms/%s", keycloakBaseUrl, keycloakRealm));
        claims.setSubject(UUID.randomUUID().toString());
        claims.setClaim("typ", "Bearer");
        claims.setClaim("azp", "example-client-id");
        claims.setClaim("auth_time", NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli()).getValue());
        claims.setClaim("session_state", UUID.randomUUID().toString());
        claims.setClaim("acr", "0");
        claims.setClaim("realm_access", Map.of("roles", List.of("offline_access", "uma_authorization")));
        claims.setClaim("resource_access", Map.of("account",
                Map.of("roles", List.of("manage-account", "manage-account-links", "view-profile"))
                )
        );
        claims.setClaim("scope", "profile email");
        claims.setClaim("name", "John Doe");
        claims.setClaim("email_verified", true);
        claims.setClaim("preferred_username", "doe.john");
        claims.setClaim("given_name", "John");
        claims.setClaim("family_name", "Doe");
        claims.setClaim("aud", expectedAud);

        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());

        jws.setKey(rsaJsonWebKey.getPrivateKey());

        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        jws.setHeader("typ", "JWT");

        return jws.getCompactSerialization();
    }

    @RestController
    public static class TestController {
        @GetMapping("/actuator/health")
        public String test() {
            return "hello";
        }
    }

}
