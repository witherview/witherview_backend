package com.witherview.study.controller;

import com.witherview.mysql.entity.StudyHistory;
import com.witherview.study.dto.GroupStudyDTO;
import com.witherview.study.dto.StudyHistoryDTO;
import com.witherview.study.mapper.StudyHistoryMapper;
import com.witherview.study.service.StudyHistoryService;
import com.witherview.study.util.AuthTokenParsing;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "StudyHistory API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group/history")
public class StudyHistoryController {
    private final StudyHistoryService studyHistoryService;
    private final StudyHistoryMapper studyHistoryMapper;

    @ApiOperation(value = "스터디 연습 내역 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveStudyHistory(@RequestBody @Valid GroupStudyDTO.StudyRequestDTO requestDto,
                                              BindingResult error,
                                              @ApiIgnore Authentication authentication) {
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyHistory studyHistory = studyHistoryService.saveStudyHistory(requestDto.getId(), userId);
        return new ResponseEntity<>(studyHistoryMapper.toHistoryIdDto(studyHistory), HttpStatus.CREATED);
    }

    @ApiOperation(value = "스터디 녹화 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile,
                                         @RequestParam("studyHistoryId") Long studyHistoryId,
                                         @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyHistory studyHistory = studyHistoryService.uploadVideo(videoFile, studyHistoryId, userId);
        return new ResponseEntity<>(studyHistoryMapper.toVideoSavedDto(studyHistory), HttpStatus.OK);
    }

    @ApiOperation(value = "스터디 연습 기록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<?> getList(@ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<StudyHistory> studyHistories = studyHistoryService.findAll(userId);
        return new ResponseEntity<>(studyHistoryMapper.toResponseArray(studyHistories), HttpStatus.OK);
    }

    @ApiOperation(value = "스터디 연습 영상 제목 수정")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", paramType = "header")
    })
    @PutMapping
    public ResponseEntity<?> updateStudyHistory(@RequestBody @Valid StudyHistoryDTO.HistoryUpdateRequestDTO dto,
        BindingResult error,
        @ApiIgnore Authentication authentication) {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyHistory studyHistory = studyHistoryService.updateStudyHistory(userId, dto);
        return new ResponseEntity<>(studyHistoryMapper.toResponseDto(studyHistory), HttpStatus.OK);
    }
}
