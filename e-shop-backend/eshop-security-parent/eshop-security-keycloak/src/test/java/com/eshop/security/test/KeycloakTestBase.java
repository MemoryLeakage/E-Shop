package com.eshop.security.test;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:wiremock.properties")
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
public abstract class KeycloakTestBase {
    protected static RsaJsonWebKey rsaJsonWebKey;

    @Value("${wiremock.server.baseUrl}")
    protected String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    protected String keycloakRealm;

    @Value("${keycloak.aud}")
    protected String expectedAud;
    @Autowired
    protected MockMvc mockMvc;

    protected String username = "username";
    protected String firstName = "john";
    protected String lastName = "doe";
    protected String email = "johndoe@eshop.com";
    protected String[] userRoles = new String[]{"role1", "role2"};
    protected String resourceName = "eshop-ui";
    @BeforeEach
    public void setUp() throws JoseException {
        if (!isTestSetup()) {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");
            rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
            rsaJsonWebKey.setUse("sig");

            //validate
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
            testIsSetup();
        }
    }

    protected String generateJWT() throws JoseException {

        JwtClaims claims = new JwtClaims();
        claims.setJwtId(UUID.randomUUID().toString());
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setNotBeforeMinutesInThePast(0);
        claims.setIssuedAtToNow();
        claims.setAudience("account",expectedAud);
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
        claims.setClaim("name", firstName + " " + lastName);
        claims.setClaim("email_verified", true);
        claims.setClaim("preferred_username", username);
        claims.setClaim("given_name", firstName);
        claims.setClaim("family_name", lastName);
        claims.setClaim("email", email);
        claims.setClaim("resource_access",Map.of(resourceName, Map.of("roles",userRoles)));
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());

        jws.setKey(rsaJsonWebKey.getPrivateKey());

        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        jws.setHeader("typ", "JWT");

        return jws.getCompactSerialization();
    }

    protected abstract boolean isTestSetup();
    protected abstract void testIsSetup();
}
