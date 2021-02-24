package com.witherview.account;

import com.witherview.database.entity.User;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.utils.AccountMapper;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(tags = "Account API")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;


    @ApiOperation(value="회원가입")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
                                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid AccountDTO.RegisterDTO registerDTO,
                                      BindingResult error) throws URISyntaxException {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        User user = accountService.register(registerDTO);
        // todo: 생성된 데이터에 접근할 수 있는 uri를 반환해야 함.
        var uri = new URI("/api/myInfo");
        return ResponseEntity.created(uri).body(accountMapper.toRegister(user));
    }

    @ApiOperation(value="로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, responseHeaders = {
                    @ResponseHeader(name = "Authorization", description = "Bearer <JWT Token here>", response = String.class)
            }, message = "Response Header"),
            @ApiResponse(code = 401, message = "UnAuthorized")
        }
    )
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
                                  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody @Valid AccountDTO.LoginDTO loginDTO,
                                   BindingResult error) {
        // 이곳의 로직은 작동하지 않습니다. Swagger를 위해 등록해놓은 인터페이스입니다.
        // CustomAuthentication Filter를 참고하세요.
        if (error.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer + JWT Token Value");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @ApiOperation(value="내 정보 조회")
    @GetMapping(path = "/api/myinfo")
    // todo: 어디서 쓰고 있는지?
    public ResponseEntity<AccountDTO.ResponseMyInfo> myInfo(
            Authentication authentication) {
        String email = getAuthenticationValue(authentication);
        System.out.println("controller : " + email);
        var result = accountService.myInfo(email);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="내 정보 수정")
    @PutMapping(path = "/api/myinfo")
    public ResponseEntity<?> updateMyInfo(
            Authentication authentication,
            @RequestBody @Valid AccountDTO.UpdateMyInfoDTO updateMyInfoDTO,
            BindingResult error) {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String email = getAuthenticationValue(authentication);
        User user = accountService.updateMyInfo(email, updateMyInfoDTO);
        var result = accountMapper.toUpdateMyInfo(user);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "프로필 이미지 업로드")
    @PostMapping(path="/api/myinfo/profile")
    public ResponseEntity<?> uploadProfile(
            Authentication authentication,
            @RequestParam("profileImg") MultipartFile profileImg) throws URISyntaxException {

        String email = getAuthenticationValue(authentication);
        User user = accountService.uploadProfile(email, profileImg);
        var result = accountMapper.toUploadProfile(user);
        URI uri = new URI(result.getProfileImg());
        return ResponseEntity.created(uri).body("");
    }

    private String getAuthenticationValue(Authentication auth) {
        Claims claims = (Claims) auth.getPrincipal();
        String email = claims.get("email", String.class);
        return email;
    }
}
