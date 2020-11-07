package com.witherview.selfPractice;

import com.witherview.selfPractice.Question.SelfQuestionDTO;
import com.witherview.support.MockMvcSupporter;
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

public class SelfQuestionApiTest extends MockMvcSupporter {
    final Long id = (long) 6;
    final String question = "당신의 지원동기는 무엇인가요?";
    final String answer = "저의 지원동기는 ~~입니다";
    final Integer order = 1;
    final String updatedQuestion = "당신의 입사 후 목표는 무엇인가요?";
    final String updatedAnswer = "저의 목표는 ~입니다.";

    @Test
    public void 질문_등록성공() throws Exception {
        SelfQuestionDTO.SaveDTO dto = SelfQuestionDTO.SaveDTO.builder()
                .question(question)
                .answer(answer)
                .order(order)
                .build();

        List<SelfQuestionDTO.SaveDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(post("/self/question/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$[0].question").value(question));
        resultActions.andExpect(jsonPath("$[0].answer").value(answer));
        resultActions.andExpect(jsonPath("$[0].order").value(order));
    }

    @Test
    public void 질문_수정성공() throws Exception {
        SelfQuestionDTO.UpdateDTO dto = SelfQuestionDTO.UpdateDTO.builder()
                .id(id)
                .question(updatedQuestion)
                .answer(updatedAnswer)
                .order(order)
                .build();

        List<SelfQuestionDTO.UpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/question/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.message").value("수정 성공"));
    }

    @Test
    public void 질문_등록실패_입력값_공백() throws Exception {
        SelfQuestionDTO.SaveDTO dto = SelfQuestionDTO.SaveDTO.builder()
                .question("")
                .answer(answer)
                .order(order)
                .build();

        List<SelfQuestionDTO.SaveDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(post("/self/question/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문_수정실패_입력값_공백() throws Exception {
        SelfQuestionDTO.UpdateDTO dto = SelfQuestionDTO.UpdateDTO.builder()
                .id(id)
                .question("")
                .answer(updatedAnswer)
                .order(order)
                .build();

        List<SelfQuestionDTO.UpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/question/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문_등록실패_없는_질문리스트() throws Exception {
        SelfQuestionDTO.SaveDTO dto = SelfQuestionDTO.SaveDTO.builder()
                .question(question)
                .answer(answer)
                .order(order)
                .build();

        List<SelfQuestionDTO.SaveDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(post("/self/question/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 질문리스트가 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 질문_수정실패_없는_질문() throws Exception {
        SelfQuestionDTO.UpdateDTO dto = SelfQuestionDTO.UpdateDTO.builder()
                .id(id)
                .question(updatedQuestion)
                .answer(updatedAnswer)
                .order(order)
                .build();

        List<SelfQuestionDTO.UpdateDTO> list = new ArrayList<>();
        list.add(dto);

        ResultActions resultActions = mockMvc.perform(patch("/self/question/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 질문이 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }
}
