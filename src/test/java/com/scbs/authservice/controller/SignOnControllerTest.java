package com.scbs.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbs.authservice.model.AuthParams;
import com.scbs.authservice.model.SessionData;
import com.scbs.authservice.scbpartnerservice.SCBPartnerServiceImpl;
import com.scbs.authservice.service.UserDataService;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest()
class SignOnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SCBPartnerServiceImpl scbPartnerService;
    @MockBean
    private UserDataService userDataService;
    @MockBean
    private SessionData sessionData;

    @Mock
    private Supplier<Response.Builder> mockResponseSupplier;


    @Test()
    void testGetDeepLinkNotFoundPartner(){
        try {
            mockMvc.perform(get("/deeplink/{partner}", "NotFound")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test()
    void testAuthenticateNotFoundPartner(){
        AuthParams authParams = new AuthParams();
        authParams.setScheme("NotFound");
        try {
            mockMvc.perform(post("/authenticate/partner/token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(asJsonString(authParams))
                    )
                    .andExpect(status().is4xxClientError());
        } catch (Exception e) {
            fail(e);
        }
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}