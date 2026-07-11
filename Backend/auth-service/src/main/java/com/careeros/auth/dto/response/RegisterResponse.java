package com.careeros.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RegisterResponse {


    private final UUID userId;
    private final String email;
    private final String firstName;
    private final String message;


}
