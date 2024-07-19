package com.web.travel.security.oauth2;

import com.nimbusds.jose.shaded.gson.Gson;
import com.web.travel.mapper.response.SignInResMapper;
import com.web.travel.model.User;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.SignInResponse;
import com.web.travel.security.jwt.JwtUtils;
import com.web.travel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Value("${travel.app.client.host}")
    String clientHost;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            String email = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";
            String imgUrl = userDetails.getAttribute("picture") != null ? userDetails.getAttribute("picture") : userDetails.getAttribute("avatar_url");
            String fullName = userDetails.getAttribute("name");
            String phone = userDetails.getAttribute("phone");
            SignInResponse signInResponse = new SignInResponse();
            if(!userService.userIsExistsByEmail(email)){
                SignupRequest dto = new SignupRequest();
                dto.setPhone(phone != null ? phone : "");
                dto.setEmail(email);
                dto.setPassword("default-password");
                dto.setAddress("");
                dto.setFullName(fullName);
                dto.setAvatar(imgUrl);

                Set<String> roles = new HashSet<>();
                roles.add("user");
                dto.setRole(roles);

                User savedUser = userService.saveDefaultUser(dto);
                userService.saveUserLoginHistory(request, savedUser);
                signInResponse = (SignInResponse) new SignInResMapper().mapToDTO(savedUser);

            }else{
                User foundUser = userService.getUserObjectByEmail(email);
                userService.saveUserLoginHistory(request, foundUser);
                signInResponse = (SignInResponse) new SignInResMapper().mapToDTO(foundUser);

            }
            String token = jwtUtils.generateJwtToken(email);
            signInResponse.setAccessToken(token);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("utf-8");
            Gson gson = new Gson();
            String json = gson.toJson(signInResponse);



            Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
            String objectToken = encoder.encodeToString(json.getBytes());
            String url = clientHost + "/auth-redirect?token=" + objectToken;

            response.setHeader("Location", url);
            response.setStatus(302);
        }
    }
}
