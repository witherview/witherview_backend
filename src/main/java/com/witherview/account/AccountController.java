package com.witherview.account;

import com.witherview.database.entity.User;
import com.witherview.exception.ErrorCode;
import com.witherview.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final ModelMapper modelMapper;
    private final AccountService accountService;

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

}
