package com.witherview.selfPractice.Question;

import com.witherview.database.entity.Question;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.QuestionRepository;
import com.witherview.selfPractice.exception.NotFoundQuestion;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.utils.SelfQuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
