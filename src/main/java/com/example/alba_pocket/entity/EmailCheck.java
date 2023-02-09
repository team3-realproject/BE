package com.example.alba_pocket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@RequiredArgsConstructor
public class EmailCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authCode;

    public EmailCheck(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }

    public void codeUpdate(String authCode) {
        this.authCode = authCode;
    }
}
