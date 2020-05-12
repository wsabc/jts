package com.example.cs;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class)
//@WebMvcTest(CsController.class)
public class CsControllerUT extends BaseControllerUT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CsService csService;

    @Test
    public void whenDemo_thenReturnGreeting() throws Exception {
        given(csService.demo()).willReturn("helloCS");

        mockMvc.perform(MockMvcRequestBuilders.get("/demo")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is("helloCS")));
//                .andDo((result) -> {
//            String content = result.getResponse().getContentAsString();
//            Assertions.assertThat("helloCS".equals(content));
//        });
    }
}
