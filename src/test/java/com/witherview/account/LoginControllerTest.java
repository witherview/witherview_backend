//package com.witherview.account;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//public class LoginControllerTest extends AccountSupporter {
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Test
//    public void 로그인_성공_케이스() throws Exception {
//        AccountDTO.LoginDTO dto = new AccountDTO.LoginDTO();
//        dto.setEmail(email1);
//        dto.setPassword(password);
//
//        ResultActions resultActions = mockMvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        resultActions.andExpect(jsonPath("$.email").value(email1));
//        resultActions.andExpect(jsonPath("$.name").value(name));
//        resultActions.andExpect(jsonPath("$.profileImg").value(profileImg));
//        resultActions.andExpect(jsonPath("$.mainIndustry").value(mainIndustry1));
//        resultActions.andExpect(jsonPath("$.subIndustry").value(subIndustry1));
//        resultActions.andExpect(jsonPath("$.mainJob").value(mainJob1));
//        resultActions.andExpect(jsonPath("$.subJob").value(subJob1));
//    }
//
//    @Test
//    public void 로그인_실패_케이스_비밀번호_틀림() throws Exception {
//        AccountDTO.LoginDTO dto = new AccountDTO.LoginDTO();
//        dto.setEmail(email);
//        dto.setPassword(password + "1");
//
//        ResultActions resultActions = mockMvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//
//        resultActions.andExpect(jsonPath("$.status").value(404));
//        resultActions.andExpect(jsonPath("$.code").value("AUTH003"));
//    }
//
//    @Test
//    public void 로그인_실패_케이스_아이디_틀림() throws Exception {
//        AccountDTO.LoginDTO dto = new AccountDTO.LoginDTO();
//        dto.setEmail("hohoho2@witherview.com");
//        dto.setPassword(password);
//
//        ResultActions resultActions = mockMvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//
//        resultActions.andExpect(jsonPath("$.status").value(404));
//        resultActions.andExpect(jsonPath("$.code").value("AUTH003"));
//    }
//
//    @Test
//    public void 로그인_없이_API_접근() throws Exception {
//        ResultActions resultActions = mockMvc.perform(post("/api/self/question")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//
//        resultActions.andExpect(jsonPath("$.status").value(401));
//        resultActions.andExpect(jsonPath("$.code").value("AUTH004"));
//    }
//}
