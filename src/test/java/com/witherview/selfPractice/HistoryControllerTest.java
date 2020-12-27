package com.witherview.selfPractice;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.SelfHistory;
import com.witherview.database.entity.User;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundUser;
import com.witherview.selfPractice.history.SelfHistoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class HistoryControllerTest extends SelfPracticeSupporter {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 히스토리_등록_성공() throws Exception {
        SelfHistoryDTO.SelfHistoryRequestDTO dto = new SelfHistoryDTO.SelfHistoryRequestDTO();
        dto.setQuestionListId(listId);

        mockMvc.perform(post("/api/self/history")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void 히스토리_등록_실패_유효하지_않은_유저() throws Exception {
        mockHttpSession.setAttribute("user", new AccountSession(userId + 1, email, name));

        SelfHistoryDTO.SelfHistoryRequestDTO dto = new SelfHistoryDTO.SelfHistoryRequestDTO();
        dto.setQuestionListId(listId);

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.code").value("SELF-PRACTICE001"));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 히스토리_등록_실패_유효하지_않은_리스트_아이디() throws Exception {
        long wrongListId = 0L;

        SelfHistoryDTO.SelfHistoryRequestDTO dto = new SelfHistoryDTO.SelfHistoryRequestDTO();
        dto.setQuestionListId(wrongListId);

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .session(mockHttpSession))
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

        SelfHistoryDTO.SelfHistoryRequestDTO dto = new SelfHistoryDTO.SelfHistoryRequestDTO();
        dto.setQuestionListId(questionList.getId());

        ResultActions resultActions = mockMvc.perform(post("/api/self/history")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto))
                .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.code").value("SELF-PRACTICE002"));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 히스토리_요청() throws Exception {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);

        QuestionList questionList1 = new QuestionList("title1", "ent1", "job1");
        QuestionList questionList2 = new QuestionList("title2", "ent2", "job2");
        QuestionList questionList3 = new QuestionList("title3", "ent3", "job3");

        user.addQuestionList(questionList1);
        user.addQuestionList(questionList2);
        user.addQuestionList(questionList3);

        SelfHistory selfHistory1 = new SelfHistory(questionList1);
        SelfHistory selfHistory2 = new SelfHistory(questionList2);
        SelfHistory selfHistory3 = new SelfHistory(questionList3);

        selfHistory1.updateSavedLocation("asd");
        selfHistory2.updateSavedLocation("asd");
        selfHistory3.updateSavedLocation("asd");

        user.addSelfHistory(selfHistory1);
        user.addSelfHistory(selfHistory2);
        user.addSelfHistory(selfHistory3);

        userRepository.save(user);

        ResultActions resultActions = mockMvc.perform(get("/api/self/history")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$", hasSize(4)));
    }
}
