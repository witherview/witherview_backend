package com.witherview.selfPractice;

import com.witherview.selfPractice.QuestionList.SelfQuestionListDTO;
import com.witherview.support.MockMvcSupporter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SelfQuestionListApiTest extends MockMvcSupporter {

    final String title = "개발자 스터디 모집해요.";
    final String enterprise = "kakao";
    final String job = "sw 개발자";
    final Integer order = 1;
    final String updatedTitle = "기획자 스터디 모집합니다.";
    final String updatedEnterprise = "naver";

    @Test
    public void 질문리스트_등록성공() throws Exception {
        SelfQuestionListDTO.SaveDTO dto = SelfQuestionListDTO.SaveDTO.builder()
                .title(title)
                .enterprise(enterprise)
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.title").value(title));
        resultActions.andExpect(jsonPath("$.enterprise").value(enterprise));
        resultActions.andExpect(jsonPath("$.job").value(job));
        resultActions.andExpect(jsonPath("$.order").value(order));
    }

    @Test
    public void 질문리스트_수정성공() throws Exception {
        SelfQuestionListDTO.UpdateDTO dto = SelfQuestionListDTO.UpdateDTO.builder()
                .title(updatedTitle)
                .enterprise(updatedEnterprise)
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.title").value(updatedTitle));
        resultActions.andExpect(jsonPath("$.enterprise").value(updatedEnterprise));
    }

    @Test
    public void 질문리스트_등록실패_없는_사용자() throws Exception {
        SelfQuestionListDTO.SaveDTO dto = SelfQuestionListDTO.SaveDTO.builder()
                .title(title)
                .enterprise(enterprise)
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 유저가 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문리스트_등록실패_입력값_공백() throws Exception {
        SelfQuestionListDTO.SaveDTO dto = SelfQuestionListDTO.SaveDTO.builder()
                .title("")
                .enterprise(enterprise)
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문리스트_수정실패_없는_질문리스트() throws Exception {
        SelfQuestionListDTO.UpdateDTO dto = SelfQuestionListDTO.UpdateDTO.builder()
                .title(updatedTitle)
                .enterprise(updatedEnterprise)
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 질문리스트가 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문리스트_수정실패_입력값_공백() throws Exception {
        SelfQuestionListDTO.UpdateDTO dto = SelfQuestionListDTO.UpdateDTO.builder()
                .title("")
                .enterprise("")
                .job(job)
                .order(order)
                .build();

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }
}
