package com.web.travel.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LoginVerifyRequest {
    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }
}
