package com.witherview.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AccountSession {
    private Long id;
    private String email;
    private String name;
}
