package com.witherview.account;

import com.witherview.database.repository.UserRepository;
import com.witherview.support.MockMvcSupporter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AccountSupporter extends MockMvcSupporter {
    final String email = "hohoho@witherview.com";
    final String password = "123456";
    final String passwordConfirm = "123456";
    final String name = "위더뷰";

    @Autowired
    UserRepository userRepository;
}
