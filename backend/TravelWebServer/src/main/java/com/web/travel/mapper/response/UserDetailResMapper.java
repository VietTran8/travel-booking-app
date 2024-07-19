package com.web.travel.mapper.response;

import com.web.travel.dto.response.UserResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.User;
import com.web.travel.model.enums.EUserStatus;

import java.util.List;

public class UserDetailResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        User user = (User) obj;
        UserResDTO userResDTO = new UserResDTO();
        userResDTO.setId(user.getId());
        userResDTO.setFullName(user.getFullName());
        userResDTO.setEmail(user.getEmail());
        userResDTO.setAvatar(user.getAvatar());
        userResDTO.setAddress(user.getAddress());
        userResDTO.setActive(user.getActive().equals(EUserStatus.STATUS_ACTIVATED));
        userResDTO.setPhone(user.getPhone());
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
        userResDTO.setRoles(roles);
        return userResDTO;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
