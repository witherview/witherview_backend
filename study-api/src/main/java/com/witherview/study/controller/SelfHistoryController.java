package com.witherview.study.controller;

import com.witherview.mysql.entity.SelfHistory;
import com.witherview.study.dto.SelfHistoryDTO;
import com.witherview.study.mapper.SelfHistoryMapper;
import com.witherview.study.service.SelfHistoryService;
import com.witherview.study.util.AuthTokenParsing;
import exception.ErrorCode;
import exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "SelfHistory API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/self/history")
public class SelfHistoryController {

    private final SelfHistoryMapper selfHistoryMapper;
    private final SelfHistoryService selfHistoryService;

    @ApiOperation(value = "혼자 연습 기록 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSelfHistory(@RequestBody @Valid SelfHistoryDTO.SelfHistoryCreateRequestDTO dto,
        BindingResult error,
        @ApiIgnore Authentication authentication) {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.saveSelfHistory(userId, dto.getQuestionListId());
        return new ResponseEntity<>(selfHistoryMapper.toSelfHistoryIdDto(selfHistory), HttpStatus.CREATED);
    }

    @ApiOperation(value = "혼자 연습 영상 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @PostMapping(path = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile,
        @RequestParam("selfHistoryId") Long selfHistoryId,
        @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.uploadVideo(videoFile, selfHistoryId, userId);
        return new ResponseEntity<>(selfHistoryMapper.toVideoSavedDto(selfHistory), HttpStatus.OK);
    }

    @ApiOperation(value = "혼자 연습 기록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<?> getList(@ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<SelfHistory> selfHistories = selfHistoryService.findAll(userId);
        return new ResponseEntity<>(selfHistoryMapper.toResponseArray(selfHistories), HttpStatus.OK);
    }

    @ApiOperation(value = "혼자 연습 영상 제목 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @PutMapping
    public ResponseEntity<?> updateSelfHistory(@RequestBody @Valid SelfHistoryDTO.SelfHistoryUpdateRequestDTO dto,
        BindingResult error,
        @ApiIgnore Authentication authentication) {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.updateSelfHistory(userId, dto);
        return new ResponseEntity<>(selfHistoryMapper.toResponseDto(selfHistory), HttpStatus.OK);
    }

    @ApiOperation(value = "혼자 연습 기록 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteSelfHistory(@ApiParam(value = "삭제할 내역 id", required = true) @PathVariable("id") Long historyId,
        @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.deleteSelfHistory(userId, historyId);
        return new ResponseEntity<>(selfHistoryMapper.toSelfHistoryIdDto(selfHistory), HttpStatus.OK);
    }
}
