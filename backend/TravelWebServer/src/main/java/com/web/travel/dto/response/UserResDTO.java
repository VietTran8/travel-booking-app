package com.web.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResDTO {
    private Long id;
    private String email;
    private String avatar;
    private String address;
    private String fullName;
    private String phone;
    private boolean active;
    private List<String> roles;
}
