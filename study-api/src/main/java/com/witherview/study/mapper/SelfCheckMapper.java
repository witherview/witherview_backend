package com.witherview.study.mapper;

import com.witherview.mysql.entity.CheckListType;
import com.witherview.mysql.entity.SelfCheck;
import com.witherview.study.dto.SelfCheckDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfCheckMapper {
    @Mapping(target= "checkListTypeId", source = "id")
    SelfCheckDTO.CheckListResponseDTO toResponseDto(CheckListType checkListType);
    SelfCheck toSelfCheckEntity(SelfCheckDTO.CheckListDTO checkListDTO);
    SelfCheckDTO.CheckListResultDTO[] toResultArray(List<SelfCheck> selfChecksList);
}
