package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SelfQuestionListApiController {
    private final ModelMapper modelMapper;
    private final SelfQuestionListService selfPracticeService;
    private final long userId = 1; // 임시

    // 질문 리스트 등록
    @PostMapping(path = "/self/questionList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveList(@RequestBody @Valid SelfQuestionListDTO.SaveDTO requestDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        QuestionList questionList = selfPracticeService.saveList(userId, requestDto);
        return new ResponseEntity<>(modelMapper.map(questionList, SelfQuestionListDTO.ResponseDTO.class), HttpStatus.CREATED);
    }

    // 모든 질문 리스트 조회
    @GetMapping(path = "/self/questionList")
    public ResponseEntity<?> findList(HttpSession session) {
        List<QuestionList> lists = selfPracticeService.findAllLists(userId);
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionListDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    // 질문 리스트 수정
    @PatchMapping(path = "/self/questionList/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateList(@PathVariable Long id,
                                        @RequestBody @Valid SelfQuestionListDTO.UpdateDTO requestDto,
                                        BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        selfPracticeService.updateList(id, requestDto);

        QuestionList updatedList = selfPracticeService.findList(id);
        return new ResponseEntity<>(modelMapper.map(updatedList, SelfQuestionListDTO.ResponseDTO.class), HttpStatus.OK);
    }

    // 질문 리스트 삭제
    @DeleteMapping(path = "/self/questionList/{id}")
    public ResponseEntity<?> deleteList(@PathVariable Long id) {
        QuestionList deletedList = selfPracticeService.deleteList(id);
        return new ResponseEntity<>(modelMapper.map(deletedList, SelfQuestionListDTO.DeleteResponseDTO.class), HttpStatus.OK);
    }
}
