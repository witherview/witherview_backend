package com.witherview.study.mapper;

import com.witherview.mysql.entity.StudyFeedback;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.mysql.entity.User;
import com.witherview.study.dto.GroupStudyDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface GroupStudyMapper {

    StudyRoom toStudyRoomEntity(GroupStudyDTO.StudyCreateDTO studyCreateDTO);

    GroupStudyDTO.ParticipantDTO toParticipantDto(User user);
    GroupStudyDTO.ResponseDTO toResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.ResponseDTO[] toResponseDtoArray(List<StudyRoom> studyRoomList);
    GroupStudyDTO.DeleteResponseDTO toDeleteResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.FeedBackResponseDTO toFeedBackResponseDto(StudyFeedback studyFeedback);
    GroupStudyDTO.UserRankResponseDTO toUserRankResponseDto(User user);
}
