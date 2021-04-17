package com.witherview.account;

import com.witherview.database.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
public class RegisterControllerTest extends AccountSupporter {

    @Test
    public void 회원가입_성공케이스() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm);
        dto.setName(name);
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.email").value(email));
        resultActions.andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void 회원가입_실패케이스_이메일_중복() throws Exception {
        userRepository.save(new User(email + "2", password, name,
                "주 관심산업", "부 관심산업", "주 관심직무", "부 관심직무", "01000000000"));

        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email + "2");
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm);
        dto.setName(name);
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 회원가입_실패케이스_이메일형태가_아님() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail("iddomain.com");
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm);
        dto.setName(name);
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
        resultActions.andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    public void 회원가입_실패케이스_비밀번호가_같지_않음() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm + "1");
        dto.setName(name);
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 회원가입_실패케이스_이름이_짧음() throws Exception {
        AccountDTO.RegisterDTO dto = new AccountDTO.RegisterDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setPasswordConfirm(passwordConfirm);
        dto.setName("a");
        dto.setMainIndustry("주 관심산업");
        dto.setSubIndustry("부 관심산업");
        dto.setMainJob("주 관심직무");
        dto.setSubJob("부 관심직무");

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("Invalid Input Value"));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }
}
