package com.witherview.account;

import com.witherview.support.MockMvcSupporter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends MockMvcSupporter {
    final String email = "hohoho@witherview.com";
    final String password = "123456";
    final String passwordConfirm = "123456";
    final String name = "위더뷰";

    @Test
    public void 회원가입_성공케이스() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm);
        dto.setName(name);

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.email").value(email));
        resultActions.andExpect(jsonPath("$.name").value(name));
    }
}
