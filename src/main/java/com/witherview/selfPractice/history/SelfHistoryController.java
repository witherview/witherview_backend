package com.witherview.selfPractice.history;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.SelfHistory;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "SelfHistory API")
@RequiredArgsConstructor
@RestController
public class SelfHistoryController {
    private final ModelMapper modelMapper;
    private final SelfHistoryService selfHistoryService;

    @ApiOperation(value="혼자 연습 기록 등록")
    @PostMapping(path = "/api/self/history", consumes = MediaType.APPLICATION_JSON_VALUE,
                                            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid SelfHistoryDTO.SelfHistorySaveDTO dto,
                                      BindingResult result,
                                      @ApiIgnore HttpSession session) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        SelfHistory selfHistory = selfHistoryService.save(dto, accountSession.getId());
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
