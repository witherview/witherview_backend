package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.SelfCheck;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.selfPractice.CustomValidator;
import com.witherview.utils.SelfCheckMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    private final SelfCheckMapper selfCheckMapper;
    private final SelfCheckService selfCheckService;
    private final CustomValidator customValidator;

    @ApiOperation(value="체크리스트 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/checklist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCheckList(@RequestBody @Valid SelfCheckDTO.SelfCheckRequestDTO requestDto,
                                           BindingResult error,
                                           @ApiIgnore Errors validateErrors) {
        // requestDTO 객체 검사
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // 체크리스트 객체 하나하나 검사
        customValidator.validate(requestDto.getCheckLists(), validateErrors);

        if(validateErrors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<SelfCheck> selfChecks = selfCheckService.save(requestDto);
        return new ResponseEntity<>(selfCheckMapper.toResultArray(selfChecks), HttpStatus.CREATED);
    }

    @ApiOperation(value="체크리스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/checklist")
    public ResponseEntity<?> getCheckList() {
        List<SelfCheckDTO.CheckListResponseDTO> lists = selfCheckService.findAll();
        return new ResponseEntity<>(lists.toArray(), HttpStatus.OK);
    }

    @ApiOperation(value="혼자 연습 후 체크리스트 결과 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/checklist/result")
    public ResponseEntity<?> getCheckListResult(@RequestParam("selfHistoryId") Long selfHistoryId) {
        List<SelfCheck> lists = selfCheckService.findResults(selfHistoryId);
        var result = selfCheckMapper.toResultArray(lists);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
