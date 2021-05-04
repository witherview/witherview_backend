package com.witherview.utils;

import com.witherview.database.entity.SelfHistory;
import com.witherview.selfPractice.history.SelfHistoryDTO;
import com.witherview.selfPractice.history.SelfHistoryDTO.SelfHistoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfHistoryMapper {
    SelfHistoryDTO.SelfHistoryDefaultResponseDTO toResponseDto(SelfHistory selfHistory);

    SelfHistoryDTO.SelfHistoryResponseDTO[] toResponseArray(List<SelfHistory> selfHistoryList);

    default SelfHistoryResponseDTO toResponseArray(SelfHistory selfHistory) {
        SelfHistoryResponseDTO selfHistoryResponseDTO = new SelfHistoryResponseDTO();

        selfHistoryResponseDTO.setId(selfHistory.getId());
        selfHistoryResponseDTO.setQuestionListId(selfHistory.getQuestionList().getId());
        selfHistoryResponseDTO.setQuestionListEnterprise(selfHistory.getQuestionList().getEnterprise());
        selfHistoryResponseDTO.setQuestionListJob(selfHistory.getQuestionList().getJob());
        selfHistoryResponseDTO.setSavedLocation(selfHistory.getSavedLocation());
        selfHistoryResponseDTO.setCreatedAt(selfHistory.getCreatedAt());

        return selfHistoryResponseDTO;
    }
}
