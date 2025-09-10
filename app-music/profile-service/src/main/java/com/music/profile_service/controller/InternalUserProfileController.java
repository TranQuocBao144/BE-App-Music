package com.music.profile_service.controller;


import com.music.profile_service.dto.response.UserProfileResponse;
import com.music.profile_service.dto.resquest.ProfileCreationResquest;
import com.music.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {

    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    UserProfileResponse createProfile(@RequestBody ProfileCreationResquest resquest) {
        return userProfileService.createProfile(resquest);
    }
}
