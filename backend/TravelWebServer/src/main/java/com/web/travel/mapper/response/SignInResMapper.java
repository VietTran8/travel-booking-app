package com.web.travel.mapper.response;

import com.web.travel.mapper.Mapper;
import com.web.travel.model.User;
import com.web.travel.model.enums.EUserStatus;
import com.web.travel.payload.response.SignInResponse;

import java.util.List;

public class SignInResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        User savedUser = (User) obj;
        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setId(savedUser.getId());
        signInResponse.setActive(savedUser.getActive().equals(EUserStatus.STATUS_ACTIVATED));
        signInResponse.setEmail(savedUser.getEmail());
        List<String> rolesResponse = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
        signInResponse.setRoles(rolesResponse);
        signInResponse.setPhone(savedUser.getPhone());
        signInResponse.setAvatar(savedUser.getAvatar());
        signInResponse.setAddress(savedUser.getAddress());
        signInResponse.setFullName(savedUser.getFullName());
        return signInResponse;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
