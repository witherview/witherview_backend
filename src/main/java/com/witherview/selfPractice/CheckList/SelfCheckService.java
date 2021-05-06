package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.CheckList;
import com.witherview.database.entity.CheckListType;
import com.witherview.database.entity.SelfCheck;
import com.witherview.database.entity.SelfHistory;
import com.witherview.database.repository.CheckListRepository;
import com.witherview.database.repository.CheckListTypeRepository;
import com.witherview.database.repository.SelfCheckRepository;
import com.witherview.database.repository.SelfHistoryRepository;
import com.witherview.selfPractice.exception.NotFoundCheckList;
import com.witherview.selfPractice.exception.NotFoundHistory;
import com.witherview.utils.SelfCheckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SelfCheckService {
    private final SelfHistoryRepository selfHistoryRepository;
    private final SelfCheckRepository selfCheckRepository;
    private final CheckListTypeRepository checkListTypeRepository;
    private final CheckListRepository checkListRepository;
    private final SelfCheckMapper selfCheckMapper;

    @Transactional
    public List<SelfCheck> save(SelfCheckDTO.SelfCheckRequestDTO requestDto, String userId) {
        SelfHistory selfHistory = selfHistoryRepository.findById(requestDto.getSelfHistoryId())
                .orElseThrow(NotFoundHistory::new);
        if (!selfHistory.getUser().getId().equals(userId)) {
            throw new NotFoundHistory();
        }

        // 혼자연습의 체크리스트 영역 덮어쓰기. (중복저장 문제 해결을 위해)
//        selfHistory.setSelfCheckList(new ArrayList<>());
        List<SelfCheck> result = new ArrayList<>();
        for (var data: requestDto.getCheckLists()) {
            SelfCheck selfCheck = selfCheckMapper.toSelfCheckEntity(data);
            selfHistory.addSelfCheck(selfCheck);
        }
        return selfHistory.getSelfCheckList();
    }

//    public List<SelfCheckDTO.CheckListResponseDTO> findAll(String userId) {
//        List<CheckListType> listTypes = checkListTypeRepository.findAll();
//        return selfCheckMapper.toResponseDtoList(listTypes);
//    }

    @Transactional
    public List<SelfCheck> findResults(Long selfHistoryId) {
        SelfHistory selfHistory = selfHistoryRepository.findById(selfHistoryId)
                .orElseThrow(NotFoundHistory::new);

        return selfHistory.getSelfCheckList();
    }

    public CheckList findCheckList(Long id) {
        return checkListRepository.findById(id).orElseThrow(NotFoundCheckList::new);
    }
}
