package com.witherview.utils;

import com.witherview.database.entity.CheckListType;
import com.witherview.database.entity.SelfCheck;
import com.witherview.selfPractice.CheckList.SelfCheckDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfCheckMapper {
    // service
    SelfCheckDTO.CheckListResponseDTO toResponseDto(CheckListType checkListType);
    List<SelfCheckDTO.CheckListResponseDTO> toResponseDtoList(List<CheckListType> checkListType);
    List<SelfCheck> toSelfCheckEntityList(List<SelfCheckDTO.CheckListDTO> checkListDTOs);

    // controller
    SelfCheckDTO.CheckListResultDTO[] toResultArray(List<SelfCheck> selfChecksList);
    SelfCheckDTO.CheckListResponseDTO[] toResponseArray(List<SelfCheckDTO.CheckListResponseDTO> checkListResponseDTOList);


}
