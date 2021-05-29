package com.witherview.utils;

import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.groupPractice.GroupStudy.GroupStudyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface GroupStudyMapper {

    StudyRoom toStudyRoomEntity(GroupStudyDTO.StudyCreateDTO studyCreateDTO);

    GroupStudyDTO.ParticipantDTO toParticipantDto(User user);
    GroupStudyDTO.ResponseDTO toResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.ResponseDTO[] toResponseDtoArray(List<StudyRoom> studyRoomList);
    GroupStudyDTO.DeleteResponseDTO toDeleteResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.FeedBackResponseDTO toFeedBackResponseDto(StudyFeedback studyFeedback);
}
