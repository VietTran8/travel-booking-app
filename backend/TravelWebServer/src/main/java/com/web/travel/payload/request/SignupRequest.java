package com.web.travel.payload.request;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String fullName;
    private String address;
    private String email;
    @Size(min = 6, max = 40)
    private String password;
    private String phone;
    private Set<String> role;
    private String avatar;
}
