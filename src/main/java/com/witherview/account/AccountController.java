package com.witherview.account;

import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import com.witherview.utils.AccountMapper;
import com.witherview.utils.AuthTokenParsing;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = "Account API")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final GroupStudyService groupStudyService;

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
        // 이곳의 로직은 작동하지 않습니다. Swagger를 위해 등록해놓은 인터페이스입니다.
        // CustomAuthentication Filter를 참고하세요.
        if (error.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer + JWT Token Value");
        return new ResponseEntity<>(headers, HttpStatus.OK);
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

        System.out.println("controller : " + email);
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
        String email = AuthTokenParsing.getAuthClaimValue(authentication, "email");
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

        String email = AuthTokenParsing.getAuthClaimValue(authentication, "email");
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

        String email = AuthTokenParsing.getAuthClaimValue(authentication, "email");
        String userId = AuthTokenParsing.getAuthClaimValue(authentication, "userId");
        User user = accountService.uploadProfile(email, profileImg);
        var result = accountMapper.toUploadProfile(user);
        URI uri = new URI(result.getProfileImg());
        return ResponseEntity.created(uri).body("");
    }

}
