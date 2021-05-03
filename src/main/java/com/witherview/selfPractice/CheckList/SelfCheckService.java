package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.selfPractice.exception.NotFoundCheckList;
import com.witherview.selfPractice.exception.NotFoundHistory;
import com.witherview.selfPractice.exception.NotOwnedSelfHistory;
import com.witherview.utils.SelfCheckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfCheckService {
    private final SelfHistoryRepository selfHistoryRepository;
    private final SelfCheckRepository selfCheckRepository;
    private final CheckListTypeRepository checkListTypeRepository;
    private final CheckListRepository checkListRepository;
    private final SelfCheckMapper selfCheckMapper;

    @Transactional
    public List<SelfCheck> save(String userId, SelfCheckDTO.SelfCheckRequestDTO requestDto) {
        SelfHistory selfHistory = selfHistoryRepository.findById(requestDto.getSelfHistoryId())
                .orElseThrow(NotFoundHistory::new);

        authenticateOwner(userId, selfHistory);
        return requestDto.getCheckLists().stream()
                .map(dto -> {
                    findCheckList(dto.getCheckListId());
                    SelfCheck selfCheck = selfCheckMapper.toSelfCheckEntity(dto);
                    selfHistory.addSelfCheck(selfCheck);
                    return selfCheckRepository.save(selfCheck);
                })
                .collect(Collectors.toList());
    }

    public List<SelfCheckDTO.CheckListResponseDTO> findAllCheckLists() {
        List<CheckListType> listTypes = checkListTypeRepository.findAll();
        return listTypes.stream().map(e -> selfCheckMapper.toResponseDto(e)).collect(Collectors.toList());
    }

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
