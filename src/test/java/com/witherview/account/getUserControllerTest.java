package com.witherview.account;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
public class getUserControllerTest extends AccountSupporter{

    @Test
    public void 유저정보_조회_성공() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.email").value(email1));
        resultActions.andExpect(jsonPath("$.name").value(name));
        resultActions.andExpect(jsonPath("$.profileImg").value(profileImg));
        resultActions.andExpect(jsonPath("$.mainIndustry").value(mainIndustry1));
        resultActions.andExpect(jsonPath("$.subIndustry").value(subIndustry1));
        resultActions.andExpect(jsonPath("$.mainJob").value(mainJob1));
        resultActions.andExpect(jsonPath("$.subJob").value(subJob1));
    }


    @Test
    public void 유저정보_조회_실패_토큰없이_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/user")
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.message").value("로그인 후 이용해 주세요."));
        resultActions.andExpect(jsonPath("$.status").value(401));

    }

    @Test
    public void 유저정보_조회_실패_유효하지_않은_토큰() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + token + "1")
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.message").value("유효하지 않은 로그인입니다."));
        resultActions.andExpect(jsonPath("$.status").value(401));
    }
}
