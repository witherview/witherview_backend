package com.witherview.account;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

public class AccountDTO {
    @Getter @Setter
    public static class RegisterDTO {
        @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
        @Email(message = "이메일 형식으로 입력해야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
        @Length(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        @Length(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인란을 입력해 주세요.")
        @Length(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        @Length(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
        private String passwordConfirm;

        @NotBlank(message = "이름은 반드시 입력해야 합니다.")
        @Length(min = 2, message = "이름은 2자 이상이어야 합니다.")
        @Length(max = 20, message = "이름은 20자 이하여야 합니다.")
        private String name;

        @NotBlank(message = "주 관심산업은 반드시 입력해야 합니다.")
        private String mainIndustry;

        @NotBlank(message = "부 관심산업은 반드시 입력해야 합니다.")
        private String subIndustry;

        @NotBlank(message = "주 관심직무는 반드시 입력해야 합니다.")
        private String mainJob;

        @NotBlank(message = "부 관심직무는 반드시 입력해야 합니다.")
        private String subJob;

        @Pattern(regexp = "(^$|[0-9]{11})")
        private String phoneNumber;
    }

    @Getter @Setter
    public static class LoginDTO {
        @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
        @Email(message = "이메일 형식으로 입력해야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
        @Length(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        @Length(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
        private String password;
    }

    @Getter @Setter
    public static class LoginValidateDTO {
        private String userId; // userId값이 들어간다.
        private String password;
    }

    @Getter @Setter
    public static class ResponseRegister {
        private String id;
        private String email;
        private String name;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
        private String phoneNumber;
    }

    @Getter @Setter
    public static class ResponseLogin {
        private String id;
        private String email;
        private String name;
        private String profileImg;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
        private String phoneNumber;
    }

    @Getter @Setter
    public static class ResponseMyInfo {
        private String profileImg;
        private Long groupStudyCnt;
        private Long selfPracticeCnt;
        private Long questionListCnt;
        private String interviewScore;
        private Long passCnt;
        private Long failCnt;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
        private String phoneNumber;
    }

    @Getter @Setter
    public static class UpdateMyInfoDTO {
        private String name;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
        private String phoneNumber;
    }

    @Getter @Setter
    public static class UploadProfileDTO {
        private String profileImg;
    }

    @Getter @Setter
    public static class StudyRoomDTO {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String industry;
        private String job;
        private LocalDate date;
        private LocalTime time;
        private Integer nowUserCnt;
        private Integer maxUserCnt;
    }

    @Getter @Setter
    public static class ResponseTokenDTO {
        private String token_type;
        private String access_token;
        private String expires_in;
        private String refresh_token;
        private String refresh_expires_in;
    }

    @Getter @Setter
    public static class WithdrawUserDTO {
        @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
        private String password;
    }
}
