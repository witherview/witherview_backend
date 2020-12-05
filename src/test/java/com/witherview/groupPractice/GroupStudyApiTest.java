package com.witherview.groupPractice;

import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.entity.User;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.selfPractice.exception.NotFoundUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class GroupStudyApiTest extends GroupStudySupporter {

    @Autowired
    StudyRoomRepository studyRoomRepository;

    @Test
    public void 스터디룸_등록성공() throws Exception {
        GroupStudyDTO.StudyCreateDTO dto = GroupStudyDTO.StudyCreateDTO.builder()
                .title(title2)
                .description(description2)
                .industry(industry1)
                .job(job1)
                .date(date2)
                .time(time2)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.title").value(title2));
        resultActions.andExpect(jsonPath("$.description").value(description2));
        resultActions.andExpect(jsonPath("$.industry").value(industry1));
        resultActions.andExpect(jsonPath("$.job").value(job1));
        resultActions.andExpect(jsonPath("$.date").value(date2.toString()));
        resultActions.andExpect(jsonPath("$.time").value(time2.toString()));
        resultActions.andExpect(jsonPath("$.nowUserCnt").value(1));
        resultActions.andExpect(jsonPath("$.maxUserCnt").value(2));
    }

    @Test
    public void 스터디룸_수정성공() throws Exception {
        GroupStudyDTO.StudyUpdateDTO dto = GroupStudyDTO.StudyUpdateDTO.builder()
                .id(roomId)
                .title(updatedTitle)
                .description(updatedDescription)
                .industry(industry1)
                .job(job1)
                .date(date1)
                .time(time1)
                .build();

        ResultActions resultActions = mockMvc.perform(patch("/api/group")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.title").value(updatedTitle));
        resultActions.andExpect(jsonPath("$.description").value(updatedDescription));
    }

    @Test
    public void 스터디_피드백_등록_성공() throws Exception {
        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .id(roomId)
                .targetUser(userId2)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group/feedback")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andExpect(jsonPath("$.targetUserId").value(userId2));
        resultActions.andExpect(jsonPath("$.score").value(score.intValue()));
        resultActions.andExpect(jsonPath("$.passOrFail").value(passOrFail));
    }

    @Test
    public void 스터디방_참여_성공() throws Exception {
        GroupStudyDTO.StudyJoinDTO dto = new GroupStudyDTO.StudyJoinDTO();
        dto.setId(roomId);

        ResultActions resultActions = mockMvc.perform(post("/api/group/room")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.title").value(title1));
        resultActions.andExpect(jsonPath("$.description").value(description1));
        resultActions.andExpect(jsonPath("$.industry").value(industry1));
        resultActions.andExpect(jsonPath("$.job").value(job1));
        resultActions.andExpect(jsonPath("$.date").value(date1.toString()));
        resultActions.andExpect(jsonPath("$.time").value(time1.toString()));
    }

    @Test
    public void 특정스터디룸_조회성공() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/group/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.title").value(title1));
        resultActions.andExpect(jsonPath("$.description").value(description1));
        resultActions.andExpect(jsonPath("$.industry").value(industry1));
        resultActions.andExpect(jsonPath("$.job").value(job1));
        resultActions.andExpect(jsonPath("$.date").value(date1.toString()));
        resultActions.andExpect(jsonPath("$.time").value(time1.toString()));
    }

    @Test
    public void 스터디룸_등록실패_유효하지_않은_사용자() throws Exception {
        GroupStudyDTO.StudyCreateDTO dto = GroupStudyDTO.StudyCreateDTO.builder()
                .title(title2)
                .description(description2)
                .industry(industry1)
                .job(job1)
                .date(date2)
                .time(time2)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        resultActions.andExpect(jsonPath("$.message").value("로그인 후 이용해 주세요."));
        resultActions.andExpect(jsonPath("$.status").value(401));
    }

    @Test
    public void 스터디_피드백_등록_실패_존재하지_않는_스터디룸() throws Exception {
        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .id((long) 400)
                .targetUser(userId2)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group/feedback")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.message").value("해당 스터디방이 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 스터디룸_참여_실패_이미_참여하고있는_스터디룸() throws Exception {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId).orElseThrow(NotFoundStudyRoom::new);
        User user = userRepository.findById(userId1).orElseThrow(NotFoundUser::new);
        StudyRoomParticipant studyRoomParticipant = StudyRoomParticipant.builder()
                .studyRoom(studyRoom)
                .user(user)
                .build();

        studyRoom.addParticipants(studyRoomParticipant);
        user.addParticipatedRoom(studyRoomParticipant);

        GroupStudyDTO.StudyJoinDTO dto = new GroupStudyDTO.StudyJoinDTO();
        dto.setId(roomId);

        ResultActions resultActions = mockMvc.perform(post("/api/group/room")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("이미 참여하고 있는 스터디룸입니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 스터디룸_나가기_실패_참여하고있지_않은_스터디룸() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/group/room/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("참여하지 않은 스터디룸입니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }
}
