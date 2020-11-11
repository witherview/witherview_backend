package com.witherview.account;

import com.witherview.account.exception.DuplicateEmail;
import com.witherview.account.exception.InvalidLogin;
import com.witherview.account.exception.NotEqualPassword;
import com.witherview.database.entity.User;
import com.witherview.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(AccountDTO.RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new NotEqualPassword();
        }
        User findUser = userRepository.findByEmail(dto.getEmail());
        if (findUser != null) {
            throw new DuplicateEmail();
        }
        return userRepository.save(new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName()));
    }

    public User login(AccountDTO.LoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidLogin();
        }
        return user;
    }
}
