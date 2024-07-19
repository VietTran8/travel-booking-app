package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.SaveRecentActivityRequestDTO;
import com.web.travel.dto.request.common.UserUpdateReqDTO;
import com.web.travel.dto.response.*;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.response.DestinationBlogResMapper;
import com.web.travel.mapper.response.TourGeneralResMapper;
import com.web.travel.mapper.response.UserDetailResMapper;
import com.web.travel.model.*;
import com.web.travel.model.enums.ERecentActivityType;
import com.web.travel.model.enums.ERole;
import com.web.travel.model.enums.EUserStatus;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.request.UpdateUserStatusRequest;
import com.web.travel.repository.*;
import com.web.travel.service.interfaces.FileUploadService;
import com.web.travel.utils.DateHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TourRepository tourRepository;
    @Autowired
    DestinationBlogRepository destinationBlogRepository;
    @Autowired
    RecentActivityRepository recentActivityRepository;
    @Autowired
    FileUploadService fileUploadService;
    public UserByEmailResDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ?
                new UserByEmailResDTO(user.getFullName(), user.getEmail()) :
                null;
    }

    public void saveUserLoginHistory(HttpServletRequest request, User user){
        String userAgentString = request.getHeader("User-Agent");
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent userAgent = parser.parse(userAgentString);
        String deviceName = userAgent.getName() + ", " + userAgent.getOperatingSystem().getName();

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setIpAddress(request.getRemoteAddr());
        loginHistory.setUserDevice(deviceName);
        loginHistory.setLoggedDate(DateHandler.getCurrentDateTime());

        List<LoginHistory> userLoginHistories = user.getLoginHistories();
        if(userLoginHistories != null){
            userLoginHistories.add(loginHistory);
        }else {
            userLoginHistories = new ArrayList<>();
            userLoginHistories.add(loginHistory);
        }
        userRepository.save(user);
    }
    public boolean userIsExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User saveDefaultUser(SignupRequest signUpRequest){
        // Create new user's account
        User user = new User(signUpRequest.getFullName(),
                signUpRequest.getAddress(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPhone());

        user.setAvatar(signUpRequest.getAvatar());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole = roleRepository.findByName(ERole.valueOf("ROLE_" + role.toUpperCase()))
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                roles.add(userRole);
            });
        }

        user.setRoles(roles);
        user.setActive(EUserStatus.STATUS_ACTIVATED);

        return userRepository.save(user);
    }
    public User getUserObjectByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
    public User getUserObjectById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public ResDTO updateUserInfo(Principal principal, UserUpdateReqDTO userDto){
        if(principal != null){
            User foundUser = getUserObjectByEmail(principal.getName());
            foundUser.setAddress(userDto.getAddress());
            foundUser.setFullName(userDto.getFullName());
            foundUser.setPhone(userDto.getPhone());

            UserDetailResMapper mapper = new UserDetailResMapper();
            UserResDTO response = (UserResDTO) mapper.mapToDTO(userRepository.save(foundUser));
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Cập nhật thông tin thành công!",
                    response
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy tài khoản người dùng!",
            null
        );
    }

    public ResDTO updateUserStatus(Principal principal, UpdateUserStatusRequest request){
        if(principal != null){
            if(verifyPassword(principal, request.getPassword())){
                User foundUser = getUserObjectByEmail(principal.getName());
                EUserStatus userStatus = request.isStatus() ? EUserStatus.STATUS_ACTIVATED : EUserStatus.STATUS_NOT_ACTIVATED;
                foundUser.setActive(userStatus);

                UserDetailResMapper mapper = new UserDetailResMapper();
                UserResDTO response = (UserResDTO) mapper.mapToDTO(userRepository.save(foundUser));
                return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        request.isStatus() ? "Mở tài khoản thành công" : "Vô hiệu hóa tài khoản thành công!",
                        response
                );
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Mật khẩu không đúng!",
                    null
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy người dùng!",
                null
        );
    }

    public ResDTO updateUserAvatar(Principal principal, MultipartFile avatar){
        if(principal != null){
            String avatarName = null;
            try {
                avatarName = fileUploadService.uploadFile(avatar);
            } catch (IOException e) {
                e.printStackTrace();
            }

            User foundUser = getUserObjectByEmail(principal.getName());
            foundUser.setAvatar(avatarName);
            User savedUser = userRepository.save(foundUser);
            Mapper mapper = new UserDetailResMapper();
            UserResDTO response = (UserResDTO) mapper.mapToDTO(savedUser);
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Cập nhật ảnh đai diện thành công!",
                    response
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy người dùng!",
                null
        );
    }

    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public boolean verifyPassword(Principal principal, String rawPassword){
        if(principal != null){
            User foundUser = userRepository.findByEmail(principal.getName()).orElse(null);
            if(foundUser != null){
                String encodedPassword = foundUser.getPassword();
                return getPasswordEncoder().matches(rawPassword, encodedPassword);
            }
        }
        return false;
    }

    public ResDTO getLoginHistory(Principal principal){
        User foundUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if(foundUser != null)
            return new ResDTO(
                    HttpServletResponse.SC_OK,
                    true,
                    "Login histories fetched successfully",
                    foundUser.getLoginHistories().stream().sorted(new Comparator<LoginHistory>() {
                        @Override
                        public int compare(LoginHistory o1, LoginHistory o2) {
                            return (int) (o2.getLoggedDate().getTime() - o1.getLoggedDate().getTime());
                        }
                    }).map(history -> {
                        LoginHistory loginHistory = new LoginHistory(history);
                        loginHistory.setAvatar(history.getUser().getAvatar());
                        return loginHistory;
                    }).toList()
            );
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "User not found!",
                null
        );
    }

    public ResDTO saveRecentActivity(Principal principal, SaveRecentActivityRequestDTO requestDTO){
        if(principal != null){
            User foundUser = userRepository.findByEmail(principal.getName()).orElse(null);
            if(foundUser != null){
                RecentActivity recentActivity = new RecentActivity();
                recentActivity.setUser(foundUser);
                recentActivity.setTime(DateHandler.getCurrentDateTime());

                if(requestDTO.getBlogId() != null){
                    recentActivity.setType(ERecentActivityType.BLOG);
                    DestinationBlog blog = destinationBlogRepository.findById(requestDTO.getBlogId()).orElse(null);
                    if(blog != null){
                        if(!recentActivityRepository.existsByBlog(blog)){
                            recentActivity.setBlog(blog);
                            recentActivity.setTour(null);

                            recentActivityRepository.save(recentActivity);
                        }else{
                            recentActivityRepository.findByBlog(blog).ifPresent(activity -> {
                                activity.setTime(DateHandler.getCurrentDateTime());
                                recentActivityRepository.save(activity);
                            });
                        }
                        return new ResDTO(
                                HttpServletResponse.SC_OK,
                                true,
                                "Lưu hoạt động thành công",
                                null
                        );
                    }
                    return new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Không tìm thấy bài viết",
                            null
                    );
                }
                if(requestDTO.getTourId() != null){
                    recentActivity.setType(ERecentActivityType.TOUR);
                    Tour tour = tourRepository.findById(requestDTO.getTourId()).orElse(null);
                    if(tour != null){
                        if(!recentActivityRepository.existsByTour(tour)){
                            recentActivity.setBlog(null);
                            recentActivity.setTour(tour);

                            recentActivityRepository.save(recentActivity);
                        }else{
                            recentActivityRepository.findByTour(tour).ifPresent(activity -> {
                                activity.setTime(DateHandler.getCurrentDateTime());
                                recentActivityRepository.save(activity);
                            });
                        }
                        return new ResDTO(
                                HttpServletResponse.SC_OK,
                                true,
                                "Lưu hoạt động thành công",
                                null
                        );
                    }
                    return new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Không tìm thấy tour",
                            null
                    );
                }
            }
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy tài khoản",
                null
        );
    }

    public ResDTO getRecentActivity(Principal principal, boolean isGetAll){
        if(principal != null){
            User foundUser = userRepository.findByEmail(principal.getName()).orElse(null);
            if(foundUser != null){
                List<RecentActivity> recentActivities = foundUser.getRecentActivities();
                List<TourGeneralResDTO> tourTypeActivities = recentActivities
                        .stream()
                        .filter(activity -> activity.getType().equals(ERecentActivityType.TOUR))
                        .map(activity -> {
                            Mapper mapper = new TourGeneralResMapper();
                            TourGeneralResDTO resDTO = (TourGeneralResDTO) mapper.mapToDTO(activity.getTour());
                            resDTO.setActivityTime(activity.getTime());
                            resDTO.setActivityId(activity.getId());
                            return resDTO;
                        })
                        .sorted(new Comparator<TourGeneralResDTO>() {
                            @Override
                            public int compare(TourGeneralResDTO o1, TourGeneralResDTO o2) {
                                return (int) o2.getActivityTime().getTime() - (int) o1.getActivityTime().getTime();
                            }
                        })
                        .toList();
                List<DestinationBlogResDTO> blogTypeActivities = recentActivities
                        .stream()
                        .filter(activity -> activity.getType().equals(ERecentActivityType.BLOG))
                        .map(activity -> {
                            Mapper mapper = new DestinationBlogResMapper();
                            DestinationBlogResDTO resDTO = (DestinationBlogResDTO) mapper.mapToDTO(activity.getBlog());
                            resDTO.setActivityTime(activity.getTime());
                            resDTO.setActivityId(activity.getId());
                            return resDTO;
                        })
                        .sorted(new Comparator<DestinationBlogResDTO>() {
                            @Override
                            public int compare(DestinationBlogResDTO o1, DestinationBlogResDTO o2) {
                                return (int) o2.getActivityTime().getTime() - (int) o1.getActivityTime().getTime();
                            }
                        })
                        .toList();

                if(!isGetAll){
                    tourTypeActivities = tourTypeActivities.stream().limit(5).toList();
                    blogTypeActivities = blogTypeActivities.stream().limit(5).toList();
                }

                Map<String, Object> response = new HashMap<>();
                response.put("recentTours", tourTypeActivities);
                response.put("recentPosts", blogTypeActivities);

                return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Recent activities fetched successfully",
                        response
                );
            }
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy tài khoản",
                null
        );
    }

    public ResDTO clearRecentActivity(Principal principal){
        if(principal != null){
            User foundUser = userRepository.findByEmail(principal.getName()).orElse(null);
            if(foundUser != null){
                foundUser.getRecentActivities().clear();
                userRepository.save(foundUser);
                return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Xóa thành công!",
                        null
                );
            }
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy tài khoản",
                null
        );
    }

    public ResDTO deleteRecentActivity(Principal principal, Long id){
        if(principal != null){
            RecentActivity recentActivity = recentActivityRepository.findById(id).orElse(null);
            if(userRepository.existsByEmail(principal.getName())){
                if(recentActivity != null){
                    if(recentActivity.getUser().getEmail().equals(principal.getName())){
                        recentActivityRepository.deleteRecentById(id);
                        return new ResDTO(
                                HttpServletResponse.SC_OK,
                                true,
                                "Xóa thành công!",
                                null
                        );
                    }
                    return new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Không thể xóa hoạt động của người khác!",
                            null
                    );
                }
                return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Không tìm thấy hoạt động!",
                        null
                );
            }
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Không tìm thấy tài khoản",
                null
        );
    }
}
