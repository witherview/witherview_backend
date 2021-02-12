package com.witherview.chat;

import com.google.gson.Gson;
import com.witherview.account.AccountSession;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.exception.ErrorResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.witherview.exception.ErrorCode.INVALID_INPUT_VALUE;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final Gson gson;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ModelMapper modelMapper;

    @MessageMapping("/chat.room")
    public void message(@Payload ChatDTO.MessageDTO message) {
        messagingTemplate.convertAndSend("/topic/room." + message.getRoomId(), message);
    }

    @MessageMapping("/chat.feedback")
    public void feedback(@Payload ChatDTO.FeedBackDTO feedback) {
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                chatService.saveRedis(feedback.getStudyHistoryId(), feedback));
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    @ApiOperation(value="그룹스터디 피드백 내용 등록")
    @PostMapping(path = "/api/message/feedback", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveFeedBackMessage(@RequestBody @Valid ChatDTO.SaveDTO requestDto,
                                  BindingResult result) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        List<FeedBackChat> lists = chatService.saveFeedbackMessage(requestDto);
        return new ResponseEntity<>(modelMapper.map(lists, ChatDTO.SavedFeedBackDTO[].class), HttpStatus.CREATED);
    }

    @ApiOperation(value="그룹스터디 피드백 내용 조회")
    @GetMapping(path = "/api/message/feedback")
    public ResponseEntity<?> getFeedBackMessage(@ApiParam(value = "조회할 스터디 내역 id") @RequestParam(value = "historyId") Long historyId,
                                                @ApiParam(value = "조회할 피드백 메세지 idx (디폴트 값 = 0)")
                                                @RequestParam(value = "idx", required = false) Integer current,
                                                @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        List<FeedBackChat> lists = chatService.getFeedbackMessage(historyId, accountSession.getId(), current);
        return new ResponseEntity<>(modelMapper.map(lists, ChatDTO.SavedFeedBackDTO[].class), HttpStatus.OK);
    }
}
