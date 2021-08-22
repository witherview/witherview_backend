package com.witherview.study.mapper;

import com.witherview.mysql.entity.SelfHistory;
import com.witherview.mysql.entity.StudyHistory;
import com.witherview.study.dto.SelfHistoryDTO;
import com.witherview.study.dto.StudyHistoryDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StudyHistoryMapper {
    StudyHistoryDTO.HistoryIdResponseDTO toHistoryIdDto(StudyHistory studyHistory);
    StudyHistoryDTO.VideoSaveResponseDTO toVideoSavedDto(StudyHistory studyHistory);
    StudyHistoryDTO.ResponseDTO toResponseDto(StudyHistory studyHistory);
    StudyHistoryDTO.ResponseDTO[] toResponseArray(List<StudyHistory> studyHistoryList);
}
