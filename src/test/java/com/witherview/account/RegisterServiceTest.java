package com.witherview.account;

import com.witherview.database.entity.User;
import com.witherview.selfPractice.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class RegisterServiceTest extends AccountSupporter {
    @Autowired
    AccountService accountService;

    @Test
    public void 회원가입_성공케이스_질문리스트_및_질문_갯수_확인() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email);
        dto.setName(name);
        dto.setPassword(password);
        dto.setPasswordConfirm(password);
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        Long registerdUserId = accountService.register(dto).getId();
        User user = userRepository.findById(registerdUserId).orElseThrow(UserNotFoundException::new);

        assertEquals(1, user.getQuestionLists().size());
        assertEquals(14, user.getQuestionLists().get(0).getQuestions().size());
    }
}
