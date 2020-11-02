package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SelfQuestionListApiController {
    private final SelfQuestionListService selfPracticeService;
    private final long userId = 1; // 임시

    // 질문 리스트 등록
    @PostMapping("/self/questionList")
    public ResponseEntity<?> saveList(@RequestBody @Valid SelfQuestionListDTO.SaveDTO requestDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        QuestionList questionList = selfPracticeService.saveList(userId, requestDto);
        return new ResponseEntity<>(SelfQuestionListDTO.ResponseDTO.of(questionList), HttpStatus.CREATED);
    }

    // 모든 질문 리스트 조회
    @GetMapping("/self/questionList")
    public ResponseEntity<?> findList(HttpSession session) {
        List<QuestionList> lists = selfPracticeService.findAllLists(userId);
        return new ResponseEntity<>(SelfQuestionListDTO.ResponseDTO.of(lists), HttpStatus.OK);
    }

    // 질문 리스트 수정
    @PatchMapping("/self/questionList/{id}")
    public ResponseEntity<?> updateList(@PathVariable Long id,
                                        @RequestBody @Valid SelfQuestionListDTO.UpdateDTO requestDto,
                                        BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        selfPracticeService.updateList(id, requestDto);

        QuestionList updatedList = selfPracticeService.findList(id);
        return new ResponseEntity<>(SelfQuestionListDTO.ResponseDTO.of(updatedList), HttpStatus.OK);
    }

    // 질문 리스트 삭제
    @DeleteMapping("/self/questionList/{id}")
    public ResponseEntity<?> deleteList(@PathVariable Long id) {
        QuestionList deletedList = selfPracticeService.deleteList(id);
        return new ResponseEntity<>(SelfQuestionListDTO.DeleteResponseDTO.of(deletedList), HttpStatus.OK);
    }
}
