package com.witherview.study.service;

import com.witherview.mysql.entity.Question;
import com.witherview.mysql.entity.QuestionList;
import com.witherview.mysql.repository.QuestionListRepository;
import com.witherview.mysql.repository.QuestionRepository;
import com.witherview.study.dto.SelfQuestionDTO;
import com.witherview.study.mapper.SelfQuestionMapper;
//import com.witherview.study.repository.QuestionListRepository;
//import com.witherview.study.repository.QuestionRepository;
import exception.study.NotFoundQuestion;
import exception.study.NotFoundQuestionList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfQuestionService {

  private final SelfQuestionMapper selfQuestionMapper;
  private final QuestionListRepository questionListRepository;
  private final QuestionRepository questionRepository;

  @Transactional
  public List<Question> save(SelfQuestionDTO.QuestionSaveDTO requestDto) {
    QuestionList questionList = questionListRepository.findById(requestDto.getListId())
        .orElseThrow(NotFoundQuestionList::new);

    return requestDto.getQuestions().stream()
        .map(dto -> {
          Question question = selfQuestionMapper.toQuestionEntity(dto);
          questionList.addQuestion(question);
          return questionRepository.save(question);
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public List<Question> update(List<SelfQuestionDTO.QuestionUpdateDTO> requestDto) {
    List<Question> result = requestDto.stream()
        .map(dto -> {
          Question question = findQuestion(dto.getId());
          question.update(dto.getQuestion(), dto.getAnswer(), dto.getOrder());
          return question;
        }).collect(Collectors.toList());
    return result;
  }

  @Transactional
  public Question delete(Long id) {
    Question question = findQuestion(id);
    questionRepository.delete(question);
    return question;
  }

  public Question findQuestion(Long id) {
    return questionRepository.findById(id).orElseThrow(NotFoundQuestion::new);
  }

  public List<Question> findAllQuestions(Long listId) {
    questionListRepository.findById(listId).orElseThrow(NotFoundQuestionList::new);
    return questionRepository.findAllByBelongListId(listId);
  }
}
