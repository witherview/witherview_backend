package com.witherview.groupPractice.history;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.StudyHistory;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.groupPractice.GroupStudy.GroupStudyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Api(tags = "StudyHistory API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group/history")
public class StudyHistoryController {
    private final ModelMapper modelMapper;
    private final StudyHistoryService studyHistoryService;

    @ApiOperation(value = "스터디 연습 내역 등록")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveStudyHistory(@RequestBody @Valid GroupStudyDTO.StudyRequestDTO requestDto,
                                              BindingResult result,
                                              @ApiIgnore HttpSession session) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyHistory studyHistory = studyHistoryService.saveStudyHistory(requestDto.getId(), accountSession.getId().toString());
        return new ResponseEntity<>(modelMapper.map(studyHistory,
                StudyHistoryDTO.HistoryCreatedResponseDTO.class), HttpStatus.CREATED);
    }

    @ApiOperation(value = "스터디 녹화 등록")
    @PostMapping(path = "/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile,
                                         @RequestParam("studyHistoryId") Long studyHistoryId,
                                         @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyHistory studyHistory = studyHistoryService.uploadVideo(videoFile, studyHistoryId, accountSession.getId().toString());
        return new ResponseEntity<>(modelMapper.map(studyHistory, StudyHistoryDTO.HistoryResponseDTO.class), HttpStatus.OK);
    }
}
