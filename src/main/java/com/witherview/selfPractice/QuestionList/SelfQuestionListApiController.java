package com.witherview.selfPractice.QuestionList;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.QuestionList;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.selfPractice.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class SelfQuestionListApiController {
    private final ModelMapper modelMapper;
    private final SelfQuestionListService selfQuestionListService;
    private final CustomValidator customValidator;

    // 질문 리스트 등록
    @PostMapping(path = "/self/questionList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveList(@RequestBody @Valid SelfQuestionListDTO.QuestionListSaveDTO requestDto,
                                      HttpSession session,
                                      BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        QuestionList questionList = selfQuestionListService.saveList(accountSession.getId(), requestDto);
        return new ResponseEntity<>(modelMapper.map(questionList, SelfQuestionListDTO.ResponseDTO.class), HttpStatus.CREATED);
    }

    // 모든 질문 리스트 조회
    @GetMapping(path = "/self/questionList")
    public ResponseEntity<?> findList(HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        List<QuestionList> lists = selfQuestionListService.findAllLists(accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionListDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    // 질문 리스트 수정
    @PatchMapping(path = "/self/questionList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateList(@RequestBody List<SelfQuestionListDTO.QuestionListUpdateDTO> requestDto,
                                         Errors errors) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        selfQuestionListService.updateList(requestDto);

        List<QuestionList> lists = requestDto.stream()
                                        .map(dto -> selfQuestionListService.findList(dto.getId()))
                                        .collect(Collectors.toList());
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionListDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    // 질문 리스트 삭제
    @DeleteMapping(path = "/self/questionList/{id}")
    public ResponseEntity<?> deleteList(@PathVariable Long id) {
        QuestionList deletedList = selfQuestionListService.deleteList(id);
        return new ResponseEntity<>(modelMapper.map(deletedList, SelfQuestionListDTO.DeleteResponseDTO.class), HttpStatus.OK);
    }
}
