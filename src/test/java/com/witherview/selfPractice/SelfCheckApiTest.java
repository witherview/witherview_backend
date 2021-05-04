//package com.witherview.selfPractice;
//
//import com.witherview.database.entity.CheckList;
//import com.witherview.database.entity.CheckListType;
//import com.witherview.database.repository.CheckListRepository;
//import com.witherview.database.repository.CheckListTypeRepository;
//import com.witherview.selfPractice.CheckList.SelfCheckDTO;
//import com.witherview.selfPractice.exception.NotFoundCheckListType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.ResultActions;
//
//import javax.transaction.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//public class SelfCheckApiTest extends SelfPracticeSupporter {
//    @Autowired
//    CheckListTypeRepository checkListTypeRepository;
//
//    @Autowired
//    CheckListRepository checkListRepository;
//
//    Long checkListId = (long) 1;
//    final String checkListType = "답변내용 및 목소리 체크";
//    final String checkListValue = "묻는 말에 알맞게 대답을 했다";
//    Long checkListTypeId = (long) 1;
//
//    @BeforeEach
//    public void 미리체크리스트등록() {
//        checkListTypeId = checkListTypeRepository.save(new CheckListType(checkListType)).getId();
//
//        CheckListType checkListType = checkListTypeRepository.findById(checkListTypeId).orElseThrow(NotFoundCheckListType::new);
//
//        CheckList checkList = new CheckList(checkListValue);
//        checkListType.addCheckList(checkList);
//        checkListId = checkListRepository.save(checkList).getId();
//    }
//
//    @Test
//    public void 체크리스트_결과_등록성공() throws Exception {
//        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
//                .checkListId(checkListId)
//                .isChecked(true)
//                .build();
//
//        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
//        list.add(checkListDTO);
//
//        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
//        requestDTO.setSelfHistoryId(selfHistoryId);
//        requestDTO.setCheckLists(list);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
//                .session(mockHttpSession)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(requestDTO)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//        resultActions.andExpect(jsonPath("$[0].checkListId").value(checkListId));
//        resultActions.andExpect(jsonPath("$[0].isChecked").value(true));
//    }
//
//    @Test
//    public void 체크리스트_조회성공() throws Exception {
//        ResultActions resultActions = mockMvc.perform(get("/api/self/checklist")
//                .session(mockHttpSession))
//                .andExpect(status().isOk());
//
//        resultActions.andExpect(jsonPath("$[0].checkListTypeId").value(checkListTypeId));
//        resultActions.andExpect(jsonPath("$[0].checkListType").value(checkListType));
//        resultActions.andExpect(jsonPath("$[0].checkLists[0].checkList").value(checkListValue));
//    }
//
//    @Test
//    public void 체크리스트_결과_등록실패_없는_연습내역() throws Exception {
//        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
//                .checkListId(checkListId)
//                .isChecked(true)
//                .build();
//
//        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
//        list.add(checkListDTO);
//
//        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
//        requestDTO.setSelfHistoryId((long) 100);
//        requestDTO.setCheckLists(list);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
//                .session(mockHttpSession)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(requestDTO)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//
//        resultActions.andExpect(jsonPath("$.message").value("해당 혼자 연습 내역이 없습니다."));
//        resultActions.andExpect(jsonPath("$.status").value(404));
//    }
//
//    @Test
//    public void 체크리스트_결과_등록실패_유효하지_않은_사용자() throws Exception {
//        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
//                .checkListId(checkListId)
//                .isChecked(true)
//                .build();
//
//        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
//        list.add(checkListDTO);
//
//        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
//        requestDTO.setSelfHistoryId(selfHistoryId);
//        requestDTO.setCheckLists(list);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(requestDTO)))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//
//        resultActions.andExpect(jsonPath("$.message").value("로그인 후 이용해 주세요."));
//        resultActions.andExpect(jsonPath("$.status").value(401));
//    }
//
//    @Test
//    public void 체크리스트_결과_등록실패_없는_체크리스트() throws Exception {
//        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
//                .checkListId((long) 0)
//                .isChecked(true)
//                .build();
//
//        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
//        list.add(checkListDTO);
//
//        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
//        requestDTO.setSelfHistoryId(selfHistoryId);
//        requestDTO.setCheckLists(list);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
//                .session(mockHttpSession)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(requestDTO)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//
//        resultActions.andExpect(jsonPath("$.message").value("해당 체크리스트가 없습니다."));
//        resultActions.andExpect(jsonPath("$.status").value(404));
//    }
//}
