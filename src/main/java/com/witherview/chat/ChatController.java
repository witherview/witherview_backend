package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.witherview.utils.AuthTokenParsing;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Api(value = "채팅 API", description = "서버에 있는 채팅기록 DB로 저장 및 조회", tags = "채팅 API")
public class ChatController {
    private final ChatService chatService;
    private final ChatProducer chatProducer;

    @MessageMapping("/chat.room")
    public void message(@Payload @Valid ChatDTO.MessageDTO message, @Header("Authorization") String tokenString) throws JsonProcessingException {
        String userId = chatService.getUserIdFromTokenString(tokenString);
        message.setUserId(userId);
        chatProducer.sendChat(message);
    }

    @MessageMapping("/chat.feedback")
    public void feedback(@Payload @Valid ChatDTO.FeedBackDTO feedback, @Header("Authorization") String tokenString) throws JsonProcessingException {
        String userId = chatService.getUserIdFromTokenString(tokenString);
        feedback.setSendUserId(userId);
        chatProducer.sendFeedback(feedback);
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    @ApiOperation(value="그룹스터디 피드백 내용 조회. 현재 버전은 세션에서 userId값을 가져와 사용한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header"),
            @ApiImplicitParam(name = "studyHistoryId", value = "조회할 스터디 내역 Id"),
            @ApiImplicitParam(name = "idx", value = "조회할 피드백 메세지 pagination idx (디폴트 값 = 0), 현재 10개 단위")
    })
    @GetMapping(path = "/api/messages/feedbacks")
    public ResponseEntity<List<ChatDTO.FeedBackDTO>> getFeedBackMessage(
            @ApiIgnore Authentication authentication,
            @RequestParam(value = "studyHistoryId") Long historyId,
            @RequestParam(value = "idx", required = false) Integer idx
            ) {

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");

        var lists = chatService.getFeedbackMessageByReceivedUserId(userId, historyId, idx);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @ApiOperation(value="그룹스터디 채팅 내용 조회. 현재 버전은 세션에서 userId값을 가져와 사용한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header"),
            @ApiImplicitParam(name = "studyRoomId", value = "조회할 스터디방 Id"),
            @ApiImplicitParam(name = "idx", value = "조회할 채팅 메세지 pagination idx (디폴트 값 = 0), 현재 20개 단위")
    })
    @GetMapping(path = "/api/messages/chats")
    public ResponseEntity<List<ChatDTO.MessageDTO>> getChatMessage(
            @ApiIgnore Authentication authentication,
            @RequestParam(value = "studyRoomId") Long studyRoomId,
            @RequestParam(value = "idx", required = false) Integer idx
            ) {

        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");

        var lists = chatService.getChatMessageByStudyRoomId(userId, studyRoomId, idx);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
}
