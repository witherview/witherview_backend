package com.witherview.groupPractice;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.entity.User;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.selfPractice.exception.NotFoundUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class GroupStudyApiTest extends GroupStudySupporter {

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
        StudyRoom studyRoom = studyRoomRepository.findById(roomId).orElseThrow(NotFoundStudyRoom::new);
        User user = userRepository.findById(userId2).orElseThrow(NotFoundUser::new);
        StudyRoomParticipant studyRoomParticipant = StudyRoomParticipant.builder()
                .studyRoom(studyRoom)
                .user(user)
                .build();

        studyRoom.addParticipants(studyRoomParticipant);
        user.addParticipatedRoom(studyRoomParticipant);

        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .studyRoomId(roomId)
                .historyId(studyHistoryId2)
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
        AccountSession accountSession = new AccountSession(userId2, email2, name2);
        mockHttpSession.setAttribute("user", accountSession);

        GroupStudyDTO.StudyRequestDTO dto = new GroupStudyDTO.StudyRequestDTO();
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
    public void 특정스터디룸_참여자_조회성공() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/group/room/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$[0].id").value(userId1));
        resultActions.andExpect(jsonPath("$[0].email").value(email1));
        resultActions.andExpect(jsonPath("$[0].name").value(name1));
        resultActions.andExpect(jsonPath("$[0].groupPracticeCnt").value(1));
        resultActions.andExpect(jsonPath("$[0].reliability").value(70));
        resultActions.andExpect(jsonPath("$[0].isHost").value(true));

        resultActions.andExpect(jsonPath("$[1].id").value(userId3));
        resultActions.andExpect(jsonPath("$[1].email").value(email3));
        resultActions.andExpect(jsonPath("$[1].name").value(name3));
        resultActions.andExpect(jsonPath("$[1].groupPracticeCnt").value(0));
        resultActions.andExpect(jsonPath("$[1].reliability").value(70));
        resultActions.andExpect(jsonPath("$[1].isHost").value(false));
    }

    @Test
    public void 스터디룸_삭제_성공() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/group/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(roomId));
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
    public void 스터디_피드백_등록_실패_존재하지_않는_스터디() throws Exception {
        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .studyRoomId((long) 400)
                .historyId(studyHistoryId2)
                .targetUser(userId3)
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

        resultActions.andExpect(jsonPath("$.message").value("해당 스터디룸이 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 스터디_피드백_등록_실패_참여하지_않는_스터디룸() throws Exception {
        AccountSession accountSession = new AccountSession(userId2, email2, name2);
        mockHttpSession.setAttribute("user", accountSession);

        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .studyRoomId(roomId)
                .historyId(studyHistoryId2)
                .targetUser(userId1)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group/feedback")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("참여하지 않은 스터디룸입니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 스터디_피드백_등록_실패_참여하지_않는_유저에대한_피드백() throws Exception {
        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .studyRoomId(roomId)
                .historyId(studyHistoryId2)
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

        resultActions.andExpect(jsonPath("$.message").value("스터디룸에 참여하지 않은 사람에게 피드백을 줄 수 없습니다."));
        resultActions.andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void 스터디_피드백_등록_실패_스터디_연습내역의_타겟대상이_아님() throws Exception {
        GroupStudyDTO.StudyFeedBackDTO dto = GroupStudyDTO.StudyFeedBackDTO.builder()
                .studyRoomId(roomId)
                .historyId(studyHistoryId2)
                .targetUser(userId1)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/group/feedback")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 스터디 연습 내역의 타겟 유저가 아닙니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 스터디룸_참여_실패_이미_참여하고있는_스터디룸() throws Exception {
        GroupStudyDTO.StudyRequestDTO dto = new GroupStudyDTO.StudyRequestDTO();
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
        AccountSession accountSession = new AccountSession(userId2, email2, name2);
        mockHttpSession.setAttribute("user", accountSession);

        ResultActions resultActions = mockMvc.perform(delete("/api/group/room/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("참여하지 않은 스터디룸입니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 스터디룸_삭제_실패_해당_스터디룸의_호스트가_아님() throws Exception {
        AccountSession accountSession = new AccountSession(userId2, email2, name2);
        mockHttpSession.setAttribute("user", accountSession);

        ResultActions resultActions = mockMvc.perform(delete("/api/group/" + roomId)
                .session(mockHttpSession))
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.message").value("해당 스터디룸의 호스트가 아닙니다."));
        resultActions.andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void 스터디_영상_등록_실패_없는_스터디_연습내역() throws Exception {
        MockMultipartFile file = new MockMultipartFile("video",
                "video.webm", "video/webm", "test webm".getBytes());

        ResultActions resultActions = mockMvc.perform(multipart("/api/group/video")
                .file("videoFile", file.getBytes())
                .param("studyHistoryId", "0")
                .session(mockHttpSession))
                .andExpect(status().isNotFound());

        resultActions.andExpect(jsonPath("$.status").value(404));
        resultActions.andExpect(jsonPath("$.code").value("GROUP-HISTORY001"));
    }

    @Test
    public void 스터디_영상_등록_실패_해당_유저의_연습내역_아닌경우() throws Exception {
        AccountSession accountSession = new AccountSession(userId2, email2, name2);
        mockHttpSession.setAttribute("user", accountSession);

        MockMultipartFile file = new MockMultipartFile("video",
                "video.webm", "video/webm", "test webm".getBytes());

        ResultActions resultActions = mockMvc.perform(multipart("/api/group/video")
                .file("videoFile", file.getBytes())
                .param("studyHistoryId", studyHistoryId1.toString())
                .session(mockHttpSession))
                .andExpect(status().isBadRequest());

        resultActions.andExpect(jsonPath("$.status").value(400));
        resultActions.andExpect(jsonPath("$.code").value("GROUP-HISTORY002"));
    }
}
