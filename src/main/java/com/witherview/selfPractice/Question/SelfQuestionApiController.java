package com.witherview.selfPractice.Question;

import com.witherview.database.entity.Question;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SelfQuestionApiController {
    private final ModelMapper modelMapper;
    private final SelfQuestionService selfQuestionService;
    private final CustomValidator customValidator;

    // 질문 등록
    @PostMapping(path = "/self/question/{listId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveQuestion(@PathVariable Long listId,
                                  @RequestBody List<SelfQuestionDTO.SaveDTO> requestDto,
                                  Errors errors) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Question> lists = selfQuestionService.save(listId, requestDto);
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionDTO.ResponseDTO[].class), HttpStatus.CREATED);
    }

    // 질문 수정
    @PatchMapping(path = "/self/question/{listId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateQuestion(@PathVariable Long listId,
                                  @RequestBody List<SelfQuestionDTO.UpdateDTO> requestDto,
                                  Errors errors) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        selfQuestionService.update(listId, requestDto);

        SelfQuestionDTO.UpdateResponseDTO responseDTO = new SelfQuestionDTO.UpdateResponseDTO("수정 성공");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 모든 질문 조회
    @GetMapping(path = "/self/question/{listId}")
    public ResponseEntity<?> findAllQuestion(@PathVariable Long listId) {
        List<Question> lists = selfQuestionService.findAllQuestions(listId);
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    // 질문 삭제
    @DeleteMapping(path = "/self/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        Question deletedQuestion = selfQuestionService.delete(id);
        return new ResponseEntity<>(modelMapper.map(deletedQuestion, SelfQuestionDTO.DeleteResponseDTO.class), HttpStatus.OK);
    }
}

