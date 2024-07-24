package com.web.travel.service.interfaces;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.ChangePasswordRequest;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Map;

public interface AuthService {
    boolean userIsExistsByEmail(String email);

    Map<String, Object> saveUser(SignupRequest signUpRequest);

    ResDTO changePassword(Principal principal, ChangePasswordRequest request);

    ResDTO signIn(HttpServletRequest request, LoginRequest loginRequest);

    boolean loginVerify(String token);

    ResDTO resetPassword(String password, String token);

    String getEmailFromToken(String token);

    String getEmailFromTokenForReset(String token);

    String createResetPasswordToken(String email);

    boolean resetPasswordTokenIsValid(String token);

    String encodeResetPasswordToken(String token);

    String decodeResetPasswordToken(String encodedToken);

    String getUserFullNameFromEmail(String email);

    String createConfirmationCodeToken(String email, String confirmationCode);

    String generateConfirmationCode();

    ResDTO confirmationCodeValidate(String confirmCode, String token);

    ResDTO googleAuthWithTokenId(HttpServletRequest request, String tokenId) throws GeneralSecurityException, IOException;

    ResDTO sendResetPasswordConfirmCode(String email);

    ResDTO changePassword(String email, String newPassword);
}
