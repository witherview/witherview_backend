package com.witherview.utils;

import com.witherview.database.entity.CheckListType;
import com.witherview.database.entity.SelfCheck;
import com.witherview.selfPractice.CheckList.SelfCheckDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfCheckMapper {
    @Mapping(target= "checkListTypeId", source = "id")
    SelfCheckDTO.CheckListResponseDTO toResponseDto(CheckListType checkListType);
    SelfCheck toSelfCheckEntity(SelfCheckDTO.CheckListDTO checkListDTO);
    SelfCheckDTO.CheckListResultDTO[] toResultArray(List<SelfCheck> selfChecksList);
}
