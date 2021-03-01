package com.witherview.groupPractice.GroupStudy;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.*;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.utils.AuthTokenParsing;
import com.witherview.utils.GroupStudyMapper;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "GroupStudy API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupStudyController {
    private final ModelMapper modelMapper;
    private final GroupStudyMapper groupStudyMapper;
    private final GroupStudyService groupStudyService;

    // todo: 여기 어떻게 쓰이고 있는지. + 로그인 안해도 볼 수 있는 영역이어야 하는지?
    @ApiOperation(value="특정 스터디방 조회")
    @GetMapping("/room/{id}") // 방 조회인데 room은 없고
    public ResponseEntity<?> findSpecificRoom(@ApiParam(value = "조회할 방 id") @PathVariable("id") Long id) {
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

        // 스터디방 생성자도 방 참여자로 등록
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
            @ApiParam(value = "조회할 방 id") @PathVariable("id") Long roomId,
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

    @ApiOperation(value="스터디방 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping(path = "/room/{id}")
    public ResponseEntity<?> deleteRoom(@ApiParam(value = "삭제할 방 id", required = true) @PathVariable("id") Long roomId,
                                        @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom deletedRoom = groupStudyService.deleteRoom(roomId, userId);
        // todo: 삭제되었으므로 방 정보를 굳이 리턴할 필요 없음. -> 프런트랑 이야기해야 함.
        return ResponseEntity.ok("");
    }

    // interceptor 예외이도록 처리
    // todo: 여기에 카테고리 requestParam에 false두고 처리.
    //  '전체'를 호출하는 카테고리는 현재 없는 것으로 보이는데, 처리를 어떻게 해야 하는지. -> 프론트랑 회의.
    @ApiOperation(value="전체 / 카테고리별 스터디룸 데이터 조회.")
    @GetMapping("/room")
    public ResponseEntity<?> findAllRooms(
            @ApiParam(value = "조회할 방 카테고리")
            @RequestParam(value = "category", required = false) String category,
            @ApiParam(value = "조회할 page (디폴트 값 = 0)")
            @RequestParam(value = "page", required = false) Integer current
    ) {
        List<StudyRoom> lists;

        if (category != null ) {
            lists = groupStudyService.findCategoryRooms(category, current);
        } else {
            lists = groupStudyService.findRooms(current);
        }
        return ResponseEntity.ok(groupStudyMapper.toResponseDtoArray(lists));
    }

    // todo: 참여자 조회는 Authorization이 필요한가?
    @ApiOperation(value="해당 스터디방 참여자 조회") // 방 상세페이지 -> 들어온 사용자 데이터.
    @GetMapping(path = "/room/{id}/participants") // 이건 왜 참여자 조회? 방 조회 아닌가?
    public ResponseEntity<?> findParticipants(
            @ApiParam(value = "참여 조회할 방 id") @PathVariable Long id
    ) {
        List<GroupStudyDTO.ParticipantDTO> lists = groupStudyService.findParticipatedUsers(id);
        return new ResponseEntity<>(modelMapper.map(lists, GroupStudyDTO.ParticipantDTO[].class), HttpStatus.OK);
    }

    @ApiOperation(value="스터디방에 참여")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path = "/room/{id}/participants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> joinRoom(
            @PathVariable("id") Long roomId,
            BindingResult error,
            @ApiIgnore Authentication authentication) {

        if(error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom studyRoom = groupStudyService.joinRoom(roomId, userId);
        return ResponseEntity.ok(groupStudyMapper.toResponseDto(studyRoom));
    }

    @ApiOperation(value="스터디방 나가기")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    // todo: 삭제로직에서 리턴값 필요한지??
    @DeleteMapping(path = "/room/{id}/participants")
    public ResponseEntity<?> leaveRoom(@ApiParam(value = "나갈 방 id", required = true) @PathVariable Long roomId,
                                       @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyRoom leftRoom = groupStudyService.leaveRoom(roomId, userId);
        return ResponseEntity.ok("");
    }

    @ApiOperation(value="스터디 피드백")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> feedback(@RequestBody @Valid GroupStudyDTO.StudyFeedBackDTO requestDto,
                                      BindingResult result,
                                      @ApiIgnore Authentication authentication) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        StudyFeedback studyFeedback = groupStudyService.createFeedBack(userId, requestDto);
        return new ResponseEntity<>(groupStudyMapper.toFeedBackResponseDto(studyFeedback), HttpStatus.CREATED);
    }
}
