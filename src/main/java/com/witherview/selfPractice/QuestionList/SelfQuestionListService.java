package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public void updateList(List<SelfQuestionListDTO.UpdateDTO> requestDto) {
        requestDto.stream().forEach(dto -> {
            QuestionList questionList = findList(dto.getId());
            questionList.update(dto.getTitle(), dto.getEnterprise(), dto.getJob());
        });
    }

    @Transactional
    public QuestionList deleteList(Long id) {
        QuestionList questionList = findList(id);
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