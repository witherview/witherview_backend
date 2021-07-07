package com.witherview.study.controller;

import com.witherview.mysql.entity.SelfCheck;
import com.witherview.study.dto.SelfCheckDTO;
import com.witherview.study.mapper.SelfCheckMapper;
import com.witherview.study.service.SelfCheckService;
import com.witherview.study.util.AuthTokenParsing;
import com.witherview.study.util.CustomValidator;
import exception.ErrorCode;
import exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "SelfCheckList API")
@RequiredArgsConstructor
@RestController
public class SelfCheckController {
    private final SelfCheckMapper selfCheckMapper;
    private final SelfCheckService selfCheckService;
    private final CustomValidator customValidator;


    @ApiOperation(value="혼자연습 체크리스트 결과 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/checklist/result", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCheckList(@RequestBody @Valid SelfCheckDTO.SelfCheckRequestDTO requestDto,
                                           BindingResult error,
                                           @ApiIgnore Errors validateErrors,
                                           @ApiIgnore Authentication authentication) {
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

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<SelfCheck> selfChecks = selfCheckService.save(requestDto, userId);
        return new ResponseEntity<>(selfCheckMapper.toResultArray(selfChecks), HttpStatus.CREATED);
    }

    @ApiOperation(value="기본으로 제공되는 체크리스트 질문 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/checklist")
    public ResponseEntity<?> getCheckList(@ApiIgnore Authentication authentication) {
        List<SelfCheckDTO.CheckListResponseDTO> lists = selfCheckService.findAllCheckLists();
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
  
    @ApiOperation(value="혼자 연습내역 체크리스트 결과 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/checklist/result/{selfHistoryId}")
    public ResponseEntity<?> getCheckListResult(
            @PathVariable("selfHistoryId") Long selfHistoryId,
            @ApiIgnore Authentication authentication
    ) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<SelfCheck> lists = selfCheckService.findResults(userId, selfHistoryId);
        return new ResponseEntity<>(selfCheckMapper.toResultArray(lists), HttpStatus.OK);
    }
}
