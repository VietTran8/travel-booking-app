package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.SaveRecentActivityRequestDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.model.User;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.request.UpdateUserStatusRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserService {
    UserByEmailResDTO getUserByEmail(String email);

    void saveUserLoginHistory(HttpServletRequest request, User user);

    boolean userIsExistsByEmail(String email);

    User saveDefaultUser(SignupRequest signUpRequest);

    User getUserObjectByEmail(String email);

    User getUserObjectById(Long id);

    ResDTO updateUserInfo(Principal principal, UserUpdateReqDTO userDto);

    ResDTO updateUserStatus(Principal principal, UpdateUserStatusRequest request);

    ResDTO updateUserAvatar(Principal principal, MultipartFile avatar);

    boolean verifyPassword(Principal principal, String rawPassword);

    ResDTO getLoginHistory(Principal principal);

    ResDTO saveRecentActivity(Principal principal, SaveRecentActivityRequestDTO requestDTO);

    ResDTO getRecentActivity(Principal principal, boolean isGetAll);

    ResDTO clearRecentActivity(Principal principal);

    ResDTO deleteRecentActivity(Principal principal, Long id);
}
