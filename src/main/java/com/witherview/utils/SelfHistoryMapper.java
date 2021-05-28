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
}
