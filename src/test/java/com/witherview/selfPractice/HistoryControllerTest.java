package com.witherview.selfPractice;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.history.SelfHistoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class HistoryControllerTest extends SelfPracticeSupporter {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 히스토리_정상_등록() throws Exception {
        SelfHistoryDTO.SelfHistorySaveDTO dto = new SelfHistoryDTO.SelfHistorySaveDTO();
        dto.setQuestionListId(listId);

        mockMvc.perform(post("/api/self/history")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void 히스토리_등록_실패_유효하지_않은_유저() throws Exception {
        SelfHistoryDTO.SelfHistorySaveDTO dto = new SelfHistoryDTO.SelfHistorySaveDTO();
        dto.setQuestionListId(listId);

        mockHttpSession.setAttribute("user", new AccountSession(userId + 1, email, name));

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.code").value("SELF-PRACTICE001"));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 히스토리_등록_실패_유효하지_않은_리스트_아이디() throws Exception {
        SelfHistoryDTO.SelfHistorySaveDTO dto = new SelfHistoryDTO.SelfHistorySaveDTO();
        dto.setQuestionListId(listId + 1);

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.code").value("SELF-PRACTICE002"));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 히스토리_등록_실패_해당_리스트는_요청한_유저의_리스트가_아님() throws Exception {
        QuestionList questionList = new QuestionList("제목2", "기업명2", "직무명2");
        User user = new User("hohoho2@witherview.com", "pass2", "name2");
        user.addQuestionList(questionList);
        userRepository.save(user);

        SelfHistoryDTO.SelfHistorySaveDTO dto = new SelfHistoryDTO.SelfHistorySaveDTO();
        dto.setQuestionListId(questionList.getId());

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.code").value("SELF-PRACTICE002"));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }
}
