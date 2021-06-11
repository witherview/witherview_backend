package com.witherview.study.mapper;

import com.witherview.mysql.entity.SelfHistory;
import com.witherview.study.dto.SelfHistoryDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfHistoryMapper {
    SelfHistoryDTO.SelfHistoryDefaultResponseDTO toResponseDto(SelfHistory selfHistory);

    SelfHistoryDTO.SelfHistoryResponseDTO[] toResponseArray(List<SelfHistory> selfHistoryList);
}
