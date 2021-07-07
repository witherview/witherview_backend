package com.witherview.account.controller;

import com.witherview.account.dto.AccountDTO;
import com.witherview.account.mapper.AccountMapper;
import com.witherview.account.service.AccountService;
import com.witherview.account.util.AuthTokenParsing;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.mysql.entity.User;
import exception.ErrorCode;
import exception.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Account API")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @ApiOperation(value="회원가입")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
                                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(
            @RequestBody @Valid AccountDTO.RegisterDTO registerDTO,
            BindingResult error) throws URISyntaxException {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        User user = accountService.register(registerDTO);
        return new ResponseEntity<>(accountMapper.toRegister(user), HttpStatus.CREATED);
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
        if (error.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        var returnValue = accountService.login(loginDTO.getEmail(), loginDTO.getPassword());
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @ApiOperation("내 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping("/api/user")
    public ResponseEntity<AccountDTO.ResponseLogin> getUser(
            @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        var user = accountService.findUserById(userId);
        return ResponseEntity.ok(accountMapper.toResponseLogin(user));
    }

    @ApiOperation(value="내 통계정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping(path = "/api/myinfo")
    public ResponseEntity<AccountDTO.ResponseMyInfo> myInfo(
            @ApiIgnore Authentication authentication) {
        String email = AuthTokenParsing.getAuthClaimValue(authentication, "email");
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        var result = accountService.myInfo(userId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value="내가 참여한 스터디방 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @GetMapping(path = "/api/myinfo/rooms")
    // todo: pagination으로 추후에 변경될 여지가 있는지? - 프론트엔드에게 질문.
    //      -> 프론트에서는 무한스크롤 처리한다고 했고, 페이지네이션 방식 적용이 필요함.
    //      페이지네이션 적용은 아직 되지 않았음.
    public ResponseEntity<AccountDTO.StudyRoomDTO[]> myInfoRoom(
            @ApiParam(value = "조회할 page (디폴트 값 = 0)")
            @RequestParam(value = "page", required = false) Integer current,
            @ApiIgnore Authentication authentication) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        List<StudyRoom> lists = accountService.findRooms(userId);
        return ResponseEntity.ok(accountMapper.toResponseDtoArray(lists));
    }

    @ApiOperation(value="내 정보 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PutMapping(path = "/api/myinfo")
    public ResponseEntity<?> updateMyInfo(
            @ApiIgnore Authentication authentication,
            @RequestBody @Valid AccountDTO.UpdateMyInfoDTO updateMyInfoDTO,
            BindingResult error) {
        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        User user = accountService.updateMyInfo(userId, updateMyInfoDTO);
        var result = accountMapper.toUpdateMyInfo(user);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "프로필 이미지 업로드")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @PostMapping(path="/api/myinfo/profile")
    public ResponseEntity<?> uploadProfile(
            @ApiIgnore Authentication authentication,
            @RequestParam("profileImg") MultipartFile profileImg) throws URISyntaxException {
        // todo: 이미지 파일이 이미 있는 경우 -> 기존 이미지 삭제 후 업로드하는 로직 필요.
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        User user = accountService.uploadProfileOnAWS(userId, profileImg);
        var result = accountMapper.toUploadProfile(user);
        URI uri = new URI(result.getProfileImg());
        return ResponseEntity.created(uri).body("");
    }

    @ApiOperation(value="프로필 이미지파일 받기")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    // todo: 프론트에서 파일을 받는 형태가 어떠면 좋을지 협의 필요. (현재는 바이트 스트림.)
    @GetMapping("/api/myinfo/profile/image")
    public ResponseEntity<ByteArrayResource> downloadProfileImage(
            @ApiIgnore Authentication authentication
    ) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        byte[] data = accountService.getFileFromS3(userId);
        ByteArrayResource resource = new ByteArrayResource(data);
        String file = "profile_image";
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);
    }

    @ApiOperation(value="프로필 이미지 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping("/api/myinfo/profile/image")
    public ResponseEntity<?> deleteProfileImage(
            @ApiIgnore Authentication authentication
    ) {
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        accountService.deleteFileFromS3(userId);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "회원 탈퇴")
    @ApiImplicitParams({
        @ApiImplicitParam(name="authorization", paramType = "header")
    })
    @DeleteMapping(path="/withdraw")
    public ResponseEntity<?> withdrawUser(
        @ApiIgnore Authentication authentication,
        @RequestBody @Valid AccountDTO.WithdrawUserDTO withdrawUserDTO,
        BindingResult error) {

        if (error.hasErrors()) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        accountService.withdrawUser(userId, withdrawUserDTO.getPassword());
        return ResponseEntity.ok("회원 탈퇴 성공");

    }

    // for keycloak Authentication API only.
    @ApiIgnore
    @GetMapping("/oauth/user/{email}")
    public AccountDTO.ResponseLogin getUserByEmail(@PathVariable("email") String email){
        var user = accountService.findUserByEmail(email);
        return accountMapper.toResponseLogin(user);
    }
    @ApiIgnore
    @PostMapping("/oauth/user")
    public boolean isPasswordEquals(@RequestBody @Valid AccountDTO.LoginValidateDTO loginDTO) {
        var result = accountService.isPasswordEquals(loginDTO.getUserId(), loginDTO.getPassword());
        return result;
    }
}
