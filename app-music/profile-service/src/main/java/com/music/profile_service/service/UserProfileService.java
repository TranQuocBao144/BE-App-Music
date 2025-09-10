package com.music.profile_service.service;


import com.music.profile_service.dto.response.UserProfileResponse;
import com.music.profile_service.dto.resquest.ProfileCreationResquest;
import com.music.profile_service.entity.UserProfile;
import com.music.profile_service.mapper.UserProfileMapper;
import com.music.profile_service.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationResquest resquest){
        UserProfile userProfile = userProfileMapper.toUserProfile(resquest);

        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
    public UserProfileResponse getUserProfile(String id){
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("profile not found")) ;
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();

        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }
}
