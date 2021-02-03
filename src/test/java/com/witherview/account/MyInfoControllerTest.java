package com.witherview.account;

import com.witherview.database.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyInfoControllerTest extends AccountSupporter {
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void 미리_회원가입() {
        userId = userRepository.save(new User(email, passwordEncoder.encode(password), name,
                mainIndustry1, subIndustry1, mainJob1, subJob1)).getId();

        // 세션생성
        AccountSession accountSession = new AccountSession(userId, email, name);
        mockHttpSession.setAttribute("user", accountSession);
    }

    @Test
    public void 마이페이지_조회_성공() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/myinfo")
                .session(mockHttpSession))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.groupStudyCnt").value(groupStudyCnt));
        resultActions.andExpect(jsonPath("$.selfPracticeCnt").value(selfPracticeCnt));
        resultActions.andExpect(jsonPath("$.questionListCnt").value(questionListCnt));
        resultActions.andExpect(jsonPath("$.interviewScore").value(interviewScore));
        resultActions.andExpect(jsonPath("$.passCnt").value(passCnt));
        resultActions.andExpect(jsonPath("$.failCnt").value(failCnt));
        resultActions.andExpect(jsonPath("$.mainIndustry").value(mainIndustry1));
        resultActions.andExpect(jsonPath("$.subIndustry").value(subIndustry1));
        resultActions.andExpect(jsonPath("$.mainJob").value(mainJob1));
        resultActions.andExpect(jsonPath("$.subJob").value(subJob1));
    }

    @Test
    public void 마이페이지_수정_성공() throws Exception {
        AccountDTO.UpdateMyInfoDTO dto = new AccountDTO.UpdateMyInfoDTO();
        dto.setName(name);
        dto.setMainIndustry(mainIndustry2);
        dto.setSubIndustry(subIndustry2);
        dto.setMainJob(mainJob2);
        dto.setSubJob(subJob2);

        ResultActions resultActions = mockMvc.perform(put("/api/myinfo")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.name").value(name));
        resultActions.andExpect(jsonPath("$.mainIndustry").value(mainIndustry2));
        resultActions.andExpect(jsonPath("$.subIndustry").value(subIndustry2));
        resultActions.andExpect(jsonPath("$.mainJob").value(mainJob2));
        resultActions.andExpect(jsonPath("$.subJob").value(subJob2));
    }

    @Test
    public void 마이페이지_수정_실패_유효하지_않은_사용자() throws Exception {
        AccountDTO.UpdateMyInfoDTO dto = new AccountDTO.UpdateMyInfoDTO();
        dto.setName(name);
        dto.setMainIndustry(mainIndustry2);
        dto.setSubIndustry(subIndustry2);
        dto.setMainJob(mainJob2);
        dto.setSubJob(subJob2);

        ResultActions resultActions = mockMvc.perform(put("/api/myinfo")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        resultActions.andExpect(jsonPath("$.message").value("로그인 후 이용해 주세요."));
        resultActions.andExpect(jsonPath("$.status").value(401));
    }
}
