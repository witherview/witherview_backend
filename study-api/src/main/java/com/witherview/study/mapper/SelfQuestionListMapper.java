package com.witherview.study.mapper;

import com.witherview.mysql.entity.QuestionList;
import com.witherview.study.dto.SelfQuestionListDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfQuestionListMapper {

    QuestionList toQuestionList(SelfQuestionListDTO.QuestionListSaveDTO questionListSaveDTO);
    SelfQuestionListDTO.ResponseDTO toResponseDto(QuestionList questionList);
    SelfQuestionListDTO.ResponseDTO[] toResponseDtoArray(List<QuestionList> questionListList);
    SelfQuestionListDTO.DeleteResponseDTO toDeleteDTo(QuestionList questionList);
}
