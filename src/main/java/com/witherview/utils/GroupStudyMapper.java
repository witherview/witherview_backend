package com.witherview.utils;

import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.groupPractice.GroupStudy.GroupStudyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface GroupStudyMapper {

    GroupStudyDTO.ResponseDTO toResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.ResponseDTO[] toResponseDtoArray(List<StudyRoom> studyRoomList);
    GroupStudyDTO.DeleteResponseDTO toDeleteResponseDto(StudyRoom studyRoom);
    GroupStudyDTO.ParticipantDTO toParticipantDto(StudyRoom studyRoom);
    GroupStudyDTO.FeedBackResponseDTO toFeedBackResponseDto(StudyFeedback studyFeedback);

}
