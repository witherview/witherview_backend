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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "SelfHistory API")
@RequiredArgsConstructor
@RestController
public class SelfHistoryController {
    private final SelfHistoryMapper selfHistoryMapper;
    private final SelfHistoryService selfHistoryService;

    @ApiOperation(value="혼자 연습 기록 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/history", consumes = MediaType.APPLICATION_JSON_VALUE,
                                             produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid SelfHistoryDTO.SelfHistoryRequestDTO dto,
                                  BindingResult error,
                                  @ApiIgnore Authentication authentication) {
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.save(dto.getQuestionListId(), userId);
        return new ResponseEntity<>(selfHistoryMapper.toResponseDto(selfHistory), HttpStatus.CREATED);
    }

    @ApiOperation(value="혼자 연습 영상 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/history/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                                                   produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SelfHistoryDTO.SelfHistoryDefaultResponseDTO> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile,
                                         @RequestParam("selfHistoryId") Long selfHistoryId,
                                         @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.uploadVideo(videoFile, selfHistoryId, userId);
        return new ResponseEntity<>(selfHistoryMapper.toResponseDto(selfHistory), HttpStatus.OK);
    }

    @ApiOperation(value="혼자 연습 기록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/history")
    public ResponseEntity<?> getList(@ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<SelfHistory> selfHistories = selfHistoryService.findAll(userId);
        return new ResponseEntity<>(selfHistoryMapper.toResponseArray(selfHistories), HttpStatus.OK);
    }

    @ApiOperation(value="혼자 연습 기록 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @DeleteMapping(path = "/api/self/history/{id}")
    public ResponseEntity<?> deleteHistory(@ApiParam(value = "삭제할 내역 id", required = true) @PathVariable("id") Long historyId,
                                           @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        SelfHistory selfHistory = selfHistoryService.deleteHistory(userId, historyId);
        return new ResponseEntity<>(selfHistoryMapper.toResponseDto(selfHistory), HttpStatus.OK);
    }
}
