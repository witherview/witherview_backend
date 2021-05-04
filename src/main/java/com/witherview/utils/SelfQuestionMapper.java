package com.witherview.utils;

import com.witherview.database.entity.Question;
import com.witherview.selfPractice.Question.SelfQuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfQuestionMapper {
    SelfQuestionDTO.ResponseDTO[] toResponseDtoArray(List<Question> questionList);
    SelfQuestionDTO.ResponseDTO toResponseDto(Question question);
    Question toQuestionEntity(SelfQuestionDTO.QuestionDTO questionDTO);
}
