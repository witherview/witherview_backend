package com.witherview.study.controller;

import com.witherview.mysql.entity.StudyFeedback;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.study.dto.GroupStudyDTO;
import com.witherview.study.mapper.GroupStudyMapper;
import com.witherview.study.service.GroupStudyService;
import com.witherview.study.util.AuthTokenParsing;
import exception.ErrorCode;
import exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "GroupStudy API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupStudyController {
    private final GroupStudyMapper groupStudyMapper;
    private final GroupStudyService groupStudyService;

    @ApiOperation(value="특정 스터디방 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping("/room/{id}")
    public ResponseEntity<?> findSpecificRoom(
            @ApiParam(value = "조회할 방 id") @PathVariable("id") Long id) {
        StudyRoom studyRoom = groupStudyService.findRoom(id);
        return ResponseEntity.ok(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디방 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(value = "/room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStudy(
            @RequestBody @Valid GroupStudyDTO.StudyCreateDTO requestDto,
            BindingResult error,
            @ApiIgnore Authentication authentication) throws URISyntaxException {

        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom studyRoom = groupStudyService.saveRoom(userId, requestDto);

        groupStudyService.joinRoom(studyRoom.getId(), userId);
        var uri = new URI("/api/group/room/"+studyRoom.getId());
        return ResponseEntity.created(uri).body(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디방 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PatchMapping(value = "/room/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRoom(
            @ApiParam(value = "수정할 방 id") @PathVariable("id") Long roomId,
            @RequestBody @Valid GroupStudyDTO.StudyUpdateDTO requestDto,
            BindingResult error,
            @ApiIgnore Authentication authentication) {

        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        groupStudyService.updateRoom(userId, roomId, requestDto);

        StudyRoom studyRoom = groupStudyService.findRoom(roomId);
        return ResponseEntity.ok(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디방 삭제 - 방의 마지막 참여자가 나갈 때 호출")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping(path = "/room/{id}")
    public ResponseEntity<?> deleteRoom(@ApiParam(value = "삭제할 방 id", required = true) @PathVariable("id") Long roomId,
                                        @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom studyRoom = groupStudyService.deleteRoom(roomId, userId);
        return ResponseEntity.ok(groupStudyMapper.toDeleteResponseDto(studyRoom));
    }


    @ApiOperation(value="스터디 방 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping("/room")
    public ResponseEntity<?> findAllRooms(
            @ApiParam(value = "조회할 방 산업")
            @RequestParam(value = "industry", required = false) String industry,
            @ApiParam(value = "조회할 방 직무")
            @RequestParam(value = "job", required = false) String job,
            @ApiParam(value = "조회할 방 키워드")
            @RequestParam(value = "keyword", required = false) String keyword,
            @ApiParam(value = "마지막으로 조회된 스터디룸 id (페이징 처리를 위해 사용)")
            @RequestParam(value = "lastStudyRoomId", required = false) Long lastId,
            @ApiIgnore Authentication authentication
    ) {

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<StudyRoom> lists = groupStudyService.findRooms(userId, industry, job, keyword, lastId);
        return ResponseEntity.ok(groupStudyMapper.toResponseDtoArray(lists));
    }

    @ApiOperation(value="해당 스터디방 참여자 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping(path = "/room/{id}/participants")
    public ResponseEntity<?> findParticipants(
            @ApiParam(value = "참여 조회할 방 id") @PathVariable Long id,
            @ApiIgnore Authentication authentication
    ) {
        List<GroupStudyDTO.ParticipantDTO> lists = groupStudyService.findParticipatedUsers(id);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @ApiOperation(value="스터디방에 참여")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path = "/room/{id}/participants")
    public ResponseEntity<?> joinRoom(
            @ApiParam(value = "참여할 방 id", required = true) @PathVariable("id") Long roomId,
            @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom studyRoom = groupStudyService.joinRoom(roomId, userId);
        return ResponseEntity.ok(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디방 나가기")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping(path = "/room/{id}/participants")
    public ResponseEntity<?> leaveRoom(@ApiParam(value = "나갈 방 id", required = true) @PathVariable("id") Long roomId,
                                       @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom leftRoom = groupStudyService.leaveRoom(roomId, userId);
        return ResponseEntity.ok(groupStudyMapper.toDeleteResponseDto(leftRoom));
    }

    @ApiOperation(value = "스터디방 호스트 권한 넘기기")
    @ApiImplicitParams({
        @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PatchMapping(path = "/room/{id}/host")
    public ResponseEntity<?> changeHost(@ApiParam(value = "권한바꿀 방 id", required = true) @PathVariable("id") Long roomId,
                                        @RequestBody @Valid GroupStudyDTO.StudyHostDTO requestDto,
                                        BindingResult error,
                                        @ApiIgnore Authentication authentication) {
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom studyRoom = groupStudyService.changeRoomHost(roomId, userId, requestDto.getNewHostId());
        return ResponseEntity.ok(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디 피드백")
    @ApiImplicitParams({
        @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> feedback(@RequestBody @Valid GroupStudyDTO.StudyFeedBackDTO requestDto,
                                      BindingResult error,
                                      @ApiIgnore Authentication authentication) {
        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyFeedback studyFeedback = groupStudyService.createFeedBack(userId, requestDto);
        return new ResponseEntity<>(groupStudyMapper.toFeedBackResponseDto(studyFeedback), HttpStatus.CREATED);
    }
}
