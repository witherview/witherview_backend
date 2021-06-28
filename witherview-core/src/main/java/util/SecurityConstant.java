package util;

//import org.springframework.context.annotation.Configuration;

//@Configuration
public class SecurityConstant {
    public static final long EXPIRATION_TIME = 3600 * 1000; // 1시간 (ms)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/login";
    // todo: 참조방식 변경 필요. 최소 32글자 이상
    public static final String SECRET = "12345678901234567890123456789012";
}
