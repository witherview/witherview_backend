package com.witherview.utils;

import com.witherview.database.entity.SelfHistory;
import com.witherview.selfPractice.history.SelfHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfHistoryMapper {
    SelfHistoryDTO.SelfHistoryDefaultResponseDTO toResponseDto(SelfHistory selfHistory);
    @Mapping(source = "questionList.id", target = "questionListId")
    SelfHistoryDTO.SelfHistoryResponseDTO[] toResponseArray(List<SelfHistory> selfHistoryList);
}
