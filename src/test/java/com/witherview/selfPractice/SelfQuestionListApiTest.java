package com.witherview.selfPractice;

import com.witherview.selfPractice.QuestionList.SelfQuestionListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SelfQuestionListApiTest extends SelfPracticeSupporter {

    @Test
    public void 질문리스트_등록성공() throws Exception {
        SelfQuestionListDTO.QuestionListSaveDTO dto = SelfQuestionListDTO.QuestionListSaveDTO.builder()
                .title(title)
                .enterprise(enterprise)
                .job(job)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.title").value(title));
        resultActions.andExpect(jsonPath("$.enterprise").value(enterprise));
        resultActions.andExpect(jsonPath("$.job").value(job));
    }

    @Test
    public void 질문리스트_수정성공() throws Exception {
        SelfQuestionListDTO.QuestionListUpdateDTO dto = SelfQuestionListDTO.QuestionListUpdateDTO.builder()
                .id(listId)
                .title(updatedTitle)
                .enterprise(updatedEnterprise)
                .job(job)
                .build();

        List<SelfQuestionListDTO.QuestionListUpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$[0].title").value(updatedTitle));
        resultActions.andExpect(jsonPath("$[0].enterprise").value(updatedEnterprise));
    }

    @Test
    public void 질문리스트_등록실패_없는_사용자() throws Exception {
        SelfQuestionListDTO.QuestionListSaveDTO dto = SelfQuestionListDTO.QuestionListSaveDTO.builder()
                .title(title)
                .enterprise(enterprise)
                .job(job)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.message").value("해당 유저가 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 질문리스트_등록실패_입력값_공백() throws Exception {
        SelfQuestionListDTO.QuestionListSaveDTO dto = SelfQuestionListDTO.QuestionListSaveDTO.builder()
                .title("")
                .enterprise(enterprise)
                .job(job)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/self/questionList")
                .session(mockHttpSession)
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
        SelfQuestionListDTO.QuestionListUpdateDTO dto = SelfQuestionListDTO.QuestionListUpdateDTO.builder()
                .id(listId)
                .title(updatedTitle)
                .enterprise(updatedEnterprise)
                .job(job)
                .build();

        List<SelfQuestionListDTO.QuestionListUpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.message").value("해당 질문리스트가 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 질문리스트_수정실패_입력값_공백() throws Exception {
        SelfQuestionListDTO.QuestionListUpdateDTO dto = SelfQuestionListDTO.QuestionListUpdateDTO.builder()
                .id(listId)
                .title("")
                .enterprise("")
                .job(job)
                .build();

        List<SelfQuestionListDTO.QuestionListUpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/questionList")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }
}
