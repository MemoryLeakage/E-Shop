package com.eshop.security.test;


import com.eshop.security.keycloak.SecurityConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@ContextConfiguration(classes= {KeycloakSecurityConfigsTest.TestController.class, SecurityConfigs.class})
@WebMvcTest(KeycloakSecurityConfigsTest.TestController.class)
public class KeycloakSecurityConfigsTest extends KeycloakTestBase {


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
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/" + UUID.randomUUID().toString());
        ResultActions resultActions = this.mockMvc
                .perform(request);
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401));


        resultActions = this.mockMvc
                .perform(request
                        .header("Authorization", String.format("Bearer %s", generateJWT())));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(403));
    }



    @RestController
    public static class TestController {
        @GetMapping("/actuator/health")
        public String test() {
            return "hello";
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
