package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.SaveRecentActivityRequestDTO;
import com.web.travel.dto.request.common.UserByEmailReqDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.UserByEmailResDTO;
import com.web.travel.payload.request.UpdateUserStatusRequest;
import com.web.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/get-by-email")
    public ResponseEntity<ResDTO> getUserById(@RequestBody UserByEmailReqDTO user){
        UserByEmailResDTO foundUser = service.getUserByEmail(user.getEmail());
        return foundUser != null ? ResponseEntity.ok(
                new ResDTO(HttpStatus.OK.value(),
                        true,
                        "User found",
                        foundUser)
        ) : ResponseEntity.badRequest().body(
                new ResDTO(HttpStatus.BAD_REQUEST.value(),
                        false,
                        "User not found with email: " + user.getEmail(),
                        null)
        );
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserInfo(Principal principal, @RequestBody UserUpdateReqDTO userDto){
        ResDTO response = service.updateUserInfo(principal, userDto);

        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateUserStatus(Principal principal, @RequestBody UpdateUserStatusRequest request){
        ResDTO response = service.updateUserStatus(principal, request);
        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(Principal principal, @RequestParam MultipartFile avatar){
        ResDTO response = service.updateUserAvatar(principal, avatar);
        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("login-history")
    public ResponseEntity<?> getLoginHistory(Principal principal){
        ResDTO response = service.getLoginHistory(principal);

        return response.isStatus() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("activity")
    public ResponseEntity<?> getRecentActivity(Principal principal){
        ResDTO response = service.getRecentActivity(principal, false);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("activity/all")
    public ResponseEntity<?> getAllRecentActivity(Principal principal){
        ResDTO response = service.getRecentActivity(principal, true);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("activity/clear")
    public ResponseEntity<?> clearAllRecentActivity(Principal principal){
        ResDTO response = service.clearRecentActivity(principal);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("activity/delete/{id}")
    public ResponseEntity<?> deleteRecentActivity(Principal principal, @PathVariable("id") Long id){
        ResDTO response = service.deleteRecentActivity(principal, id);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("record")
    public ResponseEntity<?> saveRecentActivity(Principal principal, @RequestBody SaveRecentActivityRequestDTO requestDTO){
        ResDTO response = service.saveRecentActivity(principal, requestDTO);
        return response.isStatus() ? ResponseEntity.ok(
                response
        ) : ResponseEntity.badRequest().body(
                response
        );
    }
}
