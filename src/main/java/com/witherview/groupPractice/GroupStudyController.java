package com.witherview.groupPractice;

import com.witherview.account.AccountDTO;
import com.witherview.account.AccountSession;
import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "GroupStudy API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupStudyController {
    private final ModelMapper modelMapper;
    private final GroupStudyService groupStudyService;

    @ApiOperation(value="스터디방 생성")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStudy(@RequestBody @Valid GroupStudyDTO.StudyCreateDTO requestDto,
                                         BindingResult result,
                                         @ApiIgnore HttpSession session) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyRoom studyRoom = groupStudyService.saveRoom(accountSession.getId(), requestDto);

        // 스터디방 생성자도 방 참여자로 등록
        groupStudyService.joinRoom(studyRoom.getId(), accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(studyRoom, GroupStudyDTO.ResponseDTO.class), HttpStatus.CREATED);
    }

    @ApiOperation(value="스터디방 수정")
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateQuestion(@RequestBody @Valid GroupStudyDTO.StudyUpdateDTO requestDto,
                                            BindingResult result) {

        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        groupStudyService.updateRoom(requestDto);
        StudyRoom studyRoom = groupStudyService.findRoom(requestDto.getId());
        return new ResponseEntity<>(modelMapper.map(studyRoom, GroupStudyDTO.ResponseDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디방 조회")
    @GetMapping
    public ResponseEntity<?> findAllRooms() {
        List<StudyRoom> lists = groupStudyService.findAllRooms();
        return new ResponseEntity<>(modelMapper.map(lists, GroupStudyDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    @ApiOperation(value="특정 유저가 참여한 스터디방 조회")
    @GetMapping(path = "/participation")
    public ResponseEntity<?> findParticipatedRooms(@ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        List<StudyRoom> lists = groupStudyService.findParticipatedRooms(accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(lists, GroupStudyDTO.ResponseDTO[].class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디방 삭제")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteRoom(@ApiParam(value = "삭제할 질문 id", required = true) @PathVariable Long id) {
        StudyRoom deletedRoom = groupStudyService.deleteRoom(id);
        return new ResponseEntity<>(modelMapper.map(deletedRoom, GroupStudyDTO.DeleteResponseDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디방 참여")
    @PostMapping(path = "/room/{id}")
    public ResponseEntity<?> joinRoom(@ApiParam(value = "참여할 방 id", required = true) @PathVariable Long id,
                                      @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyRoom studyRoom = groupStudyService.joinRoom(id, accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(studyRoom, GroupStudyDTO.ResponseDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value="해당 스터디방 참여자 조회")
    @GetMapping(path = "/room/{id}")
    public ResponseEntity<?> findParticipants(@ApiParam(value = "참여할 방 id", required = true) @PathVariable Long id) {
        List<User> lists = groupStudyService.findParticipants(id);
        return new ResponseEntity<>(modelMapper.map(lists, GroupStudyDTO.ParticipantDTO[].class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디방 나가기")
    @DeleteMapping(path = "/room/{id}")
    public ResponseEntity<?> leaveRoom(@ApiParam(value = "나갈 방 id", required = true) @PathVariable Long id,
                                       @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyRoom leftRoom = groupStudyService.leaveRoom(id, accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(leftRoom, GroupStudyDTO.DeleteResponseDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디 피드백")
    @PostMapping(path = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> feedback(@RequestBody @Valid GroupStudyDTO.StudyFeedBackDTO requestDto,
                                      BindingResult result,
                                      @ApiIgnore HttpSession session) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        StudyFeedback studyFeedback = groupStudyService.createFeedBack(accountSession.getId(), requestDto);
        return new ResponseEntity<>(modelMapper.map(studyFeedback, GroupStudyDTO.FeedBackResponseDTO.class), HttpStatus.OK);
    }
}
