package com.witherview.groupPractice.history;

import com.witherview.database.entity.StudyHistory;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.groupPractice.GroupStudy.GroupStudyDTO;
import com.witherview.utils.AuthTokenParsing;
import com.witherview.utils.StudyHistoryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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
        return new ResponseEntity<>(studyHistoryMapper.toHistoryCreatedDto(studyHistory), HttpStatus.CREATED);
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

    // TODO: 스터디 조회 api 작성 필요
}
