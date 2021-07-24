package com.witherview.study.service;

import com.witherview.mysql.entity.CheckList;
import com.witherview.mysql.entity.CheckListType;
import com.witherview.mysql.entity.SelfCheck;
import com.witherview.mysql.entity.SelfHistory;
import com.witherview.mysql.repository.CheckListTypeRepository;
import com.witherview.mysql.repository.SelfCheckRepository;
import com.witherview.mysql.repository.SelfHistoryRepository;
import com.witherview.mysql.repository.CheckListRepository;
import com.witherview.study.dto.SelfCheckDTO;
import com.witherview.study.mapper.SelfCheckMapper;
//import com.witherview.study.repository.CheckListRepository;
//import com.witherview.study.repository.CheckListTypeRepository;
//import com.witherview.study.repository.SelfCheckRepository;
//import com.witherview.study.repository.SelfHistoryRepository;
import exception.study.NotFoundCheckList;
import exception.study.NotFoundHistory;
import exception.study.NotOwnedSelfHistory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
