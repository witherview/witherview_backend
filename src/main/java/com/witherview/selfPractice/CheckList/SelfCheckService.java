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
import com.witherview.selfPractice.exception.NotOwnedSelfHistory;
import com.witherview.utils.SelfCheckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
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
        authenticateOwner(userId, selfHistory);
        List<SelfCheck> result = new ArrayList<>();
        for (var data: requestDto.getCheckLists()) {
            SelfCheck selfCheck = selfCheckMapper.toSelfCheckEntity(data);
            selfHistory.addSelfCheck(selfCheck);
        }
        return selfHistory.getSelfCheckList();
    }

    public List<SelfCheckDTO.CheckListResponseDTO> findAllCheckLists() {
        List<CheckListType> listTypes = checkListTypeRepository.findAll();
        return listTypes.stream().map(e -> selfCheckMapper.toResponseDto(e)).collect(Collectors.toList());
    }

    @Transactional
    public List<SelfCheck> findResults(String userId, Long selfHistoryId) {
        SelfHistory selfHistory = selfHistoryRepository.findById(selfHistoryId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(userId, selfHistory);
        return selfHistory.getSelfCheckList();
    }

    public CheckList findCheckList(Long id) {
        return checkListRepository.findById(id).orElseThrow(NotFoundCheckList::new);
    }

    private void authenticateOwner(String userId, SelfHistory selfHistory) {
        if (!selfHistory.getUser().getId().equals(userId)) {
          throw new NotOwnedSelfHistory();
        }
    }
}
