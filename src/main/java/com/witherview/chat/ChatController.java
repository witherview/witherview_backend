package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.witherview.account.AccountSession;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Api(value = "채팅 API", description = "서버에 있는 채팅기록 DB로 저장 및 조회", tags = "채팅 API")
public class ChatController {
    private final ChatService chatService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatProducer chatProducer;

    @MessageMapping("/chat.room")
    public void message(@Payload @Valid ChatDTO.MessageDTO message) throws JsonProcessingException, MethodArgumentNotValidException {
        chatProducer.sendChat(message);
    }

    @MessageMapping("/chat.feedback")
    public void feedback(@Payload @Valid ChatDTO.FeedBackDTO feedback) throws JsonProcessingException {
        chatProducer.sendFeedback(feedback);
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    @ApiOperation(value="그룹스터디 피드백 내용 조회. 현재 버전은 세션에서 userId값을 가져와 사용한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "historyId", value = "조회할 스터디 내역 Id"),
            @ApiImplicitParam(name = "idx", value = "조회할 피드백 메세지 pagination idx (디폴트 값 = 0), 현재 10개 단위")
    })
    @GetMapping(path = "/api/messages/feedbacks")
    public ResponseEntity<List<ChatDTO.FeedBackDTO>> getFeedBackMessage(
            @RequestParam(value = "historyId") Long historyId,
            @RequestParam(value = "idx", required = false) Integer idx,
            @ApiIgnore HttpSession session) {

        AccountSession accountSession = (AccountSession) session.getAttribute("user");

        var lists = chatService.getFeedbackMessageByReceivedUserId(accountSession.getId(), historyId, idx);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
}
