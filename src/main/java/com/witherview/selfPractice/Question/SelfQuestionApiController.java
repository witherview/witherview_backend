package com.witherview.selfPractice.Question;

import com.witherview.database.entity.Question;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class SelfQuestionApiController {
    private final ModelMapper modelMapper;
    private final SelfQuestionService selfQuestionService;
    private final CustomValidator customValidator;

    // 질문 등록
    @PostMapping(path = "/self/question", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveQuestion(@RequestBody @Valid SelfQuestionDTO.QuestionSaveDTO requestDto,
                                          BindingResult result,
                                          Errors errors) {
        // requestDTO 객체 검사
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // 질문 객체 하나하나 검사
        customValidator.validate(requestDto.getQuestions(), errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Question> lists = selfQuestionService.save(requestDto);
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionDTO.ResponseDTO[].class), HttpStatus.CREATED);
    }

    // 질문 수정
    @PatchMapping(path = "/self/question", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateQuestion(@RequestBody List<SelfQuestionDTO.QuestionUpdateDTO> requestDto,
                                  Errors errors) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        selfQuestionService.update(requestDto);

        List<Question> lists = requestDto.stream()
                .map(dto -> selfQuestionService.findQuestion(dto.getId()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(modelMapper.map(lists, SelfQuestionDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    // 모든 질문 조회
    @GetMapping(path = "/self/question")
    public ResponseEntity<?> findAllQuestion(@RequestParam("listId") Long listId) {
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

