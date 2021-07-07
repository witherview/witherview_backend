package com.witherview.study.mapper;

import com.witherview.mysql.entity.Question;
import com.witherview.study.dto.SelfQuestionDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SelfQuestionMapper {
    SelfQuestionDTO.ResponseDTO[] toResponseDtoArray(List<Question> questionList);
    SelfQuestionDTO.ResponseDTO toResponseDto(Question question);
    Question toQuestionEntity(SelfQuestionDTO.QuestionDTO questionDTO);
}
