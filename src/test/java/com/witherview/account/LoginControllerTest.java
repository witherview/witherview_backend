//package com.witherview.account;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@Transactional
//@ActiveProfiles("test")
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
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").exists())
//                .andExpect(header().stringValues("location", "/api/user"))
//                ;
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
//                .andExpect(status().isUnauthorized());
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
//                .andExpect(status().isUnauthorized());
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
