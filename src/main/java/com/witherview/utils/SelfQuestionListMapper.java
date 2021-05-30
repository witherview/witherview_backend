package com.witherview.utils;

import com.witherview.database.entity.QuestionList;
import com.witherview.selfPractice.QuestionList.SelfQuestionListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfQuestionListMapper {

    QuestionList toQuestionList(SelfQuestionListDTO.QuestionListSaveDTO questionListSaveDTO);
    @Mapping(expression = "java(questionList.getQuestions().size())", target = "length")
    SelfQuestionListDTO.ResponseDTO toResponseDto(QuestionList questionList);

    SelfQuestionListDTO.ResponseDTO[] toResponseDtoArray(List<QuestionList> questionListList);
    SelfQuestionListDTO.DeleteResponseDTO toDeleteDTo(QuestionList questionList);
}
