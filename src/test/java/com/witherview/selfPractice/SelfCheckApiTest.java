package com.witherview.selfPractice;

import com.witherview.database.entity.CheckList;
import com.witherview.database.entity.CheckListType;
import com.witherview.database.repository.CheckListRepository;
import com.witherview.database.repository.CheckListTypeRepository;
import com.witherview.selfPractice.CheckList.SelfCheckDTO;
import com.witherview.selfPractice.exception.NotFoundCheckListType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class SelfCheckApiTest extends SelfPracticeSupporter {
    @Autowired
    CheckListTypeRepository checkListTypeRepository;

    @Autowired
    CheckListRepository checkListRepository;

    Long checkListId = (long) 1;

    @BeforeEach
    public void 미리체크리스트등록() {
        Long id1 = checkListTypeRepository.save(new CheckListType("답변내용 및 목소리 체크")).getId();

        CheckListType checkListType = checkListTypeRepository.findById(id1).orElseThrow(NotFoundCheckListType::new);

        CheckList checkList = new CheckList("묻는 말에 알맞게 대답을 했다");
        checkListType.addCheckList(checkList);
        checkListId = checkListRepository.save(checkList).getId();
    }

    @Test
    public void 체크리스트_결과_등록성공() throws Exception {
        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
                .checkListId(checkListId)
                .isChecked(true)
                .build();

        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
        list.add(checkListDTO);

        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
        requestDTO.setSelfHistoryId(selfHistoryId);
        requestDTO.setCheckLists(list);

        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$[0].checkListId").value(checkListId));
        resultActions.andExpect(jsonPath("$[0].isChecked").value(true));
    }

    @Test
    public void 체크리스트_결과_등록실패_없는_연습내역() throws Exception {
        SelfCheckDTO.CheckListDTO checkListDTO = SelfCheckDTO.CheckListDTO.builder()
                .checkListId(checkListId)
                .isChecked(true)
                .build();

        List<SelfCheckDTO.CheckListDTO> list = new ArrayList<>();
        list.add(checkListDTO);

        SelfCheckDTO.SelfCheckRequestDTO requestDTO = new SelfCheckDTO.SelfCheckRequestDTO();
        requestDTO.setSelfHistoryId((long) 100);
        requestDTO.setCheckLists(list);

        ResultActions resultActions = mockMvc.perform(post("/api/self/checklist")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.message").value("해당 혼자 연습 내역이 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }
}
