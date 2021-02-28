package com.witherview.utils;

import com.witherview.database.entity.QuestionList;
import com.witherview.selfPractice.QuestionList.SelfQuestionListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface QuestionListMapper {

    QuestionList toQuestionList(SelfQuestionListDTO.QuestionListSaveDTO questionListSaveDTO);
    SelfQuestionListDTO.ResponseDTO toResponseDto(QuestionList questionList);
}
