package com.witherview.account;

import com.witherview.database.entity.User;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Api(tags = "Account API")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final int SESSION_TIMEOUT = 1 * 60 * 60;

    @ApiOperation(value="회원가입")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
                                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid AccountDTO.RegisterDTO registerDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        User user = accountService.register(registerDTO);
        return new ResponseEntity<>(modelMapper.map(user, AccountDTO.ResponseRegister.class), HttpStatus.CREATED);
    }

    @ApiOperation(value="로그인")
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
                                  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid AccountDTO.LoginDTO loginDTO,
                                   BindingResult result, @ApiIgnore HttpSession session) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        User user = accountService.login(loginDTO);
        AccountSession accountSession = new AccountSession(user.getId(), user.getEmail(), user.getName());
        session.setAttribute("user", accountSession);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        return new ResponseEntity<>(modelMapper.map(user, AccountDTO.ResponseLogin.class), HttpStatus.OK);
    }

    @ApiOperation(value="내 정보 조회")
    @GetMapping(path = "/api/myinfo")
    public ResponseEntity<?> myInfo(@ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        AccountDTO.ResponseMyInfo info = accountService.myInfo(accountSession.getId());
        return new ResponseEntity<>(modelMapper.map(info, AccountDTO.ResponseMyInfo.class), HttpStatus.OK);
    }

    @ApiOperation(value="내 정보 수정")
    @PutMapping(path = "/api/myinfo")
    public ResponseEntity<?> updateMyInfo(@RequestBody @Valid AccountDTO.UpdateMyInfoDTO updateMyInfoDTO,
                                          BindingResult result, @ApiIgnore HttpSession session) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, result);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        Long userId = accountSession.getId();

        accountService.updateMyInfo(userId, updateMyInfoDTO);
        User user = accountService.findUser(userId);
        return new ResponseEntity<>(modelMapper.map(user, AccountDTO.UpdateMyInfoDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value = "프로필 이미지 업로드")
    @PostMapping(path="/api/myinfo/profile")
    public ResponseEntity<?> uploadProfile(@RequestParam("profileImg") MultipartFile profileImg,
                                           @ApiIgnore HttpSession session) {
        AccountSession accountSession = (AccountSession) session.getAttribute("user");
        User user = accountService.uploadProfile(accountSession.getId(), profileImg);
        return new ResponseEntity<>(modelMapper.map(user, AccountDTO.UploadProfileDTO.class), HttpStatus.CREATED);
    }
}
