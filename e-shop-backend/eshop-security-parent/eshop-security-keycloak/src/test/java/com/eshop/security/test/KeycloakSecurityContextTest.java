package com.eshop.security.test;

import com.eshop.models.entities.User;
import com.eshop.security.keycloak.KeyCloakSecurityContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {KeycloakSecurityContextTest.TestController.class, MockSecurityConfigs.class})
@WebMvcTest(KeycloakSecurityConfigsTest.TestController.class)
public class KeycloakSecurityContextTest extends KeycloakTestBase {


    @Test
    void givenNullAdapterDeploymentContext_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new KeyCloakSecurityContext(null));

        assertEquals("null adapter deployment context", thrown.getMessage());
    }

    @Test
    void givenValidUnAuthorizedRequest_whenGettingUser_returnNothing() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/user"));

        MvcResult mvcResult = resultActions
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @Test
    void givenValidAuthorizedRequest_whenGettingUser_returnExpected() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/user")
                        .header("Authorization", String.format("Bearer %s", generateJWT())));

        User user = new User.Builder()
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(user)));
    }


    @Test
    void givenValidAuthorizedRequest_whenGettingRoles_returnExpected() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/roles")
                        .header("Authorization", String.format("Bearer %s", generateJWT())));

        ObjectMapper mapper = new ObjectMapper();
        resultActions
                .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(userRoles)));
    }




    @RestController
    public static class TestController {
        @Autowired
        private AdapterDeploymentContext adapterDeploymentContext;
        @GetMapping("/user")
        public User user() {
            KeyCloakSecurityContext context = new KeyCloakSecurityContext(adapterDeploymentContext);
            return context.getUser();
        }
        @GetMapping("/roles")
        public Set<String> roles() {
            KeyCloakSecurityContext context = new KeyCloakSecurityContext(adapterDeploymentContext);
            return context.getRoles();
        }
    }


    private static boolean testSetupIsCompleted = false;


    @Override
    protected boolean isTestSetup() {
        return testSetupIsCompleted;
    }

    @Override
    protected void testIsSetup() {
        testSetupIsCompleted = true;
    }

}
