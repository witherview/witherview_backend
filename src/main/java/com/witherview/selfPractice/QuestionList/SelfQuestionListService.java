package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SelfQuestionListService {
    private final QuestionListRepository questionListRepository;
    private final UserRepository userRepository;

    @Transactional
    public QuestionList saveList(Long userId, SelfQuestionListDTO.SaveDTO requestDto) {
        QuestionList questionList = requestDto.toEntity();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUser());
        user.addQuestionList(questionList);

        return questionListRepository.save(questionList);
    }

    @Transactional
    public void updateList(Long id, SelfQuestionListDTO.UpdateDTO requestDto) {
        QuestionList questionList = questionListRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestionList());

        questionList.update(requestDto.getTitle(), requestDto.getEnterprise(), requestDto.getJob());
    }

    @Transactional
    public QuestionList deleteList(Long id) {
        QuestionList questionList = questionListRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestionList());

        questionListRepository.delete(questionList);
        return questionList;
    }

    public QuestionList findList(Long id) {
        return questionListRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestionList());
    }

    public List<QuestionList> findAllLists() {
        return questionListRepository.findAll();
    }

    public List<QuestionList> findAllLists(Long userId) {
        return questionListRepository.findAllByOwnerId(userId);
    }
}