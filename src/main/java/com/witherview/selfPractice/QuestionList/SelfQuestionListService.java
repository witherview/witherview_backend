package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.selfPractice.exception.UserNotFoundException;
import com.witherview.utils.SelfQuestionListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfQuestionListService {
    private final SelfQuestionListMapper questionListMapper;
    private final QuestionListRepository questionListRepository;
    private final UserRepository userRepository;

    @Transactional
    public QuestionList saveList(String userId, SelfQuestionListDTO.QuestionListSaveDTO requestDto) {
        // 해당 list의 owner인 사용자 확인
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        QuestionList questionList = questionListMapper.toQuestionList(requestDto);
        questionList.setUserId(user.getId());
        return questionListRepository.save(questionList);
    }

    @Transactional
    public List<QuestionList> updateList(List<SelfQuestionListDTO.QuestionListUpdateDTO> requestDto) {
        var result = requestDto.stream().map(dto -> {
            QuestionList questionList = findList(dto.getId());
            questionList.update(dto.getTitle(), dto.getEnterprise(), dto.getJob());
            return questionList;
        }).collect(Collectors.toList());

        return result;
    }

    @Transactional
    public QuestionList deleteList(Long id) {
        QuestionList questionList = findList(id);
        questionListRepository.delete(questionList);
        return questionList;
    }

    public QuestionList findList(Long id) {
        return questionListRepository.findById(id)
                .orElseThrow(NotFoundQuestionList::new);
    }

    public List<QuestionList> findAllLists() {
        return (List) questionListRepository.findAll();
    }

    public List<QuestionList> findAllLists(String userId) {
        return questionListRepository.findAllByUserId(userId);
    }
}