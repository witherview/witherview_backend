package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.selfPractice.exception.NotFoundCheckList;
import com.witherview.selfPractice.exception.NotFoundHistory;
import com.witherview.utils.SelfCheckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public List<SelfCheck> save(SelfCheckDTO.SelfCheckRequestDTO requestDto) {
        SelfHistory selfHistory = selfHistoryRepository.findById(requestDto.getSelfHistoryId())
                .orElseThrow(NotFoundHistory::new);

        return requestDto.getCheckLists().stream()
                .map(dto -> {
                    findCheckList(dto.getCheckListId());
                    SelfCheck selfCheck = selfCheckMapper.toSelfCheckEntity(dto);
                    selfHistory.addSelfCheck(selfCheck);
                    return selfCheckRepository.save(selfCheck);
                })
                .collect(Collectors.toList());
    }

    public List<SelfCheckDTO.CheckListResponseDTO> findAll() {
        List<CheckListType> listTypes = checkListTypeRepository.findAll();
        return selfCheckMapper.toResponseDtoList(listTypes);
    }

    public List<SelfCheck> findResults(Long selfHistoryId) {
        SelfHistory selfHistory = selfHistoryRepository.findById(selfHistoryId)
                .orElseThrow(NotFoundHistory::new);

        return selfHistory.getSelfCheckList();
    }

    public CheckList findCheckList(Long id) {
        return checkListRepository.findById(id).orElseThrow(NotFoundCheckList::new);
    }
}
