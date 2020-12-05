package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.SelfCheck;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.selfPractice.CustomValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "SelfCheckList API")
@RequiredArgsConstructor
@RestController
public class SelfCheckController {
    private final ModelMapper modelMapper;
    private final SelfCheckService selfCheckService;
    private final CustomValidator customValidator;

    @ApiOperation(value="체크리스트 등록")
    @PostMapping(path = "/api/self/checklist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCheckList(@RequestBody @Valid SelfCheckDTO.SelfCheckRequestDTO requestDto,
                                           BindingResult result,
                                           @ApiIgnore Errors errors) {
        // requestDTO 객체 검사
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // 체크리스트 객체 하나하나 검사
        customValidator.validate(requestDto.getCheckLists(), errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<SelfCheck> selfChecks = selfCheckService.save(requestDto);
        return new ResponseEntity<>(modelMapper.map(selfChecks, SelfCheckDTO.CheckListResultDTO[].class), HttpStatus.CREATED);
    }

    @ApiOperation(value="체크리스트 목록 조회")
    @GetMapping(path = "/api/self/checklist")
    public ResponseEntity<?> getCheckList() {
        List<SelfCheckDTO.CheckListResponseDTO> lists = selfCheckService.findAll();
        return new ResponseEntity<>(modelMapper.map(lists, SelfCheckDTO.CheckListResponseDTO[].class), HttpStatus.OK);
    }

    @ApiOperation(value="혼자 연습 후 체크리스트 결과 조회")
    @GetMapping(path = "/api/self/checklist/result")
    public ResponseEntity<?> getCheckListResult(@RequestParam("selfHistoryId") Long selfHistoryId) {
        List<SelfCheck> lists = selfCheckService.findResults(selfHistoryId);
        return new ResponseEntity<>(modelMapper.map(lists, SelfCheckDTO.CheckListResultDTO[].class), HttpStatus.OK);
    }
}
