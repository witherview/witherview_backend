package com.witherview.selfPractice.Question;

import com.witherview.database.entity.Question;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.QuestionRepository;
import com.witherview.selfPractice.exception.NotFoundQuestion;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfQuestionService {
    private final QuestionListRepository questionListRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public List<Question> save(Long listId, List<SelfQuestionDTO.SaveDTO> requestDto) {
        QuestionList questionList = questionListRepository.findById(listId)
                                        .orElseThrow(() -> new NotFoundQuestionList());

        return requestDto.stream()
                .map(dto -> {
                    Question question = dto.toEntity();
                    questionList.addQuestion(question);
                    return questionRepository.save(question);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long listId, List<SelfQuestionDTO.UpdateDTO> requestDto) {
        questionListRepository.findById(listId).orElseThrow(() -> new NotFoundQuestionList());

        requestDto.stream()
                .forEach(dto -> {
                    Question question = findQuestion(dto.getId());
                    question.update(dto.getQuestion(), dto.getAnswer());
                });
    }

    @Transactional
    public Question delete(Long id) {
        Question question = findQuestion(id);

        questionRepository.delete(question);
        return question;
    }

    public Question findQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestion());
    }

    public List<Question> findAllQuestions(Long listId) {
        return questionRepository.findAllByBelongListId(listId);
    }
}
