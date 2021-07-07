package com.witherview.study.mapper;

import com.witherview.mysql.entity.StudyHistory;
import com.witherview.study.dto.StudyHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StudyHistoryMapper {
    StudyHistoryDTO.HistoryCreatedResponseDTO toHistoryCreatedDto(StudyHistory studyHistory);
    StudyHistoryDTO.HistoryResponseDTO toHistoryResponseDto(StudyHistory studyHistory);
    StudyHistoryDTO.VideoSaveResponseDTO toVideoSavedDto(StudyHistory studyHistory);
}
