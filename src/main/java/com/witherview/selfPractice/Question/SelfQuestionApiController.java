package com.witherview.selfPractice.Question;

import com.witherview.database.entity.Question;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.selfPractice.CustomValidator;
import com.witherview.utils.SelfQuestionMapper;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Question API")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SelfQuestionApiController {
    private final SelfQuestionMapper selfQuestionMapper;
    private final SelfQuestionService selfQuestionService;
    private final CustomValidator customValidator;

    // 질문 등록
    @ApiOperation(value="질문 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/question", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveQuestion(
            @RequestBody @Valid SelfQuestionDTO.QuestionSaveDTO requestDto,
            BindingResult error,
            @ApiIgnore Errors errors,
            @ApiIgnore Authentication authentication
            ) {
        // requestDTO 객체 검사
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // 질문 객체 하나하나 검사
        customValidator.validate(requestDto.getQuestions(), errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Question> lists = selfQuestionService.save(requestDto);
        return new ResponseEntity<>(selfQuestionMapper.toResponseDtoArray(lists), HttpStatus.CREATED);
    }

    // 질문 수정
    @ApiOperation(value="질문 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @PatchMapping(path = "/api/self/question", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateQuestion(
            @RequestBody List<SelfQuestionDTO.QuestionUpdateDTO> requestDto,
            @ApiIgnore Errors errors,
            @ApiIgnore Authentication authentication
    ) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = selfQuestionService.update(requestDto);
        return new ResponseEntity<>(selfQuestionMapper.toResponseDtoArray(result), HttpStatus.OK);
    }

    // 모든 질문 조회
    @ApiOperation(value="질문 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    // todo: QuestionList API와 중복됨. 로직 변경 필요.
    //  이 부분은 프론트엔드가 어떤 식으로 사용하고 있는지 + 질문 수정과 삭제로직을 어떻게 쓰는지 확인하고 작업해야 한다.
    @GetMapping(path = "/api/self/question/{id}")
    public ResponseEntity<?> findAllQuestion(
            @ApiParam(value = "조회할 질문리스트 id", required = true) @PathVariable("id") Long listId) {
        List<Question> lists = selfQuestionService.findAllQuestions(listId);
        return new ResponseEntity<>(selfQuestionMapper.toResponseDtoArray(lists), HttpStatus.OK);
    }

    // 질문 삭제
    @ApiOperation(value="질문 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", paramType = "header")
    })
    @DeleteMapping(path = "/api/self/question/{id}")
    public ResponseEntity<?> deleteQuestion(@ApiParam(value = "삭제할 질문 id", required = true) @PathVariable Long id) {
        Question deletedQuestion = selfQuestionService.delete(id);
        return new ResponseEntity<>(deletedQuestion.getId(), HttpStatus.OK);
    }
}

