package com.witherview.selfPractice.history;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.SelfHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@Api(tags = "SelfHistory API")
@RequiredArgsConstructor
@RestController
public class SelfHistoryController {
    private final ModelMapper modelMapper;
    private final SelfHistoryService selfHistoryService;

    @ApiOperation(value="혼자 연습 기록 등록")
    @PostMapping(path = "/api/self/history", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                                             produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestParam("videoFile") MultipartFile videoFile,
                                  @RequestParam("questionListId") Long questionListId,
                                  @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        SelfHistory selfHistory = selfHistoryService.save(videoFile, questionListId, accountSession);
        return new ResponseEntity<>(modelMapper.map(selfHistory,
                SelfHistoryDTO.SelfHistorySaveResponseDTO.class), HttpStatus.CREATED);
    }

    @ApiOperation(value="혼자 연습 기록 조회")
    @GetMapping(path = "/api/self/history")
    public ResponseEntity<?> getList(@ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        List<SelfHistory> selfHistories = selfHistoryService.findAll(accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(selfHistories,
                SelfHistoryDTO.SelfHistoryResponseDTO[].class), HttpStatus.OK);
    }
}
