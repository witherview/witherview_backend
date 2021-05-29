package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.selfPractice.CustomValidator;
import com.witherview.utils.AuthTokenParsing;
import com.witherview.utils.SelfQuestionListMapper;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
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

@Api(tags = "QuestionList API")
@RequiredArgsConstructor
@RestController
public class SelfQuestionListApiController {
    private final SelfQuestionListMapper selfQuestionListMapper;
    private final SelfQuestionListService selfQuestionListService;
    private final CustomValidator customValidator;

    // 질문 리스트 등록
    @ApiOperation(value="질문리스트 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path = "/api/self/questionList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveList(
            @RequestBody @Valid SelfQuestionListDTO.QuestionListSaveDTO requestDto,
            BindingResult error,
            @ApiIgnore Authentication authentication) {
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        QuestionList questionList = selfQuestionListService.saveList(userId, requestDto);
        return new ResponseEntity<>(selfQuestionListMapper.toResponseDto(questionList), HttpStatus.CREATED);
    }

    // 토큰 없으면 해당 사용자가 등록한 리스트 전체, 토큰 있으면 해당 사용자 소유의 리스트.
    @ApiOperation(value="질문리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/questionList/{id}")
    public ResponseEntity<?> findList(
            @ApiParam(value = "조회할 질문리스트 id") @PathVariable(value = "id") Long listId,
            @ApiIgnore Authentication authentication) {
        String userId = authentication != null ? AuthTokenParsing.getAuthClaimValue(authentication, "userId") : null;
        List<QuestionList> lists = selfQuestionListService.findLists(userId, listId);
        return new ResponseEntity<>(selfQuestionListMapper.toResponseDtoArray(lists), HttpStatus.OK);
    }

    @ApiOperation(value="질문리스트 전체 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization - 값 없을 시 전체 질문리스트 조회.", paramType = "header")
    })
    // todo: pagination과 같은, 리스트 전체를 내리는 방법 대신 적용할 게 뭔지 고민해봐야 함.
    @GetMapping(path = "/api/self/questionList")
    public ResponseEntity<?> findAllList(
            @ApiIgnore Authentication authentication) {
        String userId = authentication != null ? AuthTokenParsing.getAuthClaimValue(authentication, "userId") : null;
        List<QuestionList> lists = selfQuestionListService.findLists(userId, null);
        return new ResponseEntity<>(selfQuestionListMapper.toResponseDtoArray(lists), HttpStatus.OK);
    }

    @ApiOperation(value="기본 질문리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping(path = "/api/self/questionList/basic")
    public ResponseEntity<?> finSampleList(
            @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<QuestionList> lists = selfQuestionListService.findSampleList(userId);
        return new ResponseEntity<>(selfQuestionListMapper.toResponseDtoArray(lists), HttpStatus.OK);
    }

    // 질문 리스트 수정
    @ApiOperation(value="질문리스트 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PatchMapping(path = "/api/self/questionList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateList(
            @RequestBody List<SelfQuestionListDTO.QuestionListUpdateDTO> requestDto,
            @ApiIgnore Errors errors) {

        customValidator.validate(requestDto, errors);

        if(errors.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = selfQuestionListService.updateList(requestDto);
        return new ResponseEntity<>(selfQuestionListMapper.toResponseDtoArray(result), HttpStatus.OK);
    }

    // 질문 리스트 삭제
    @ApiOperation(value="질문리스트 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping(path = "/api/self/questionList/{id}")
    public ResponseEntity<?> deleteList(@ApiParam(value = "삭제할 질문리스트 id", required = true) @PathVariable Long id) {
        QuestionList deletedList = selfQuestionListService.deleteList(id);
        return new ResponseEntity<>(selfQuestionListMapper.toDeleteDTo(deletedList), HttpStatus.OK);
    }
}
