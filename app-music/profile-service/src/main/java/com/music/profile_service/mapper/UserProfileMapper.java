package com.music.profile_service.mapper;

import com.music.profile_service.dto.response.UserProfileResponse;
import com.music.profile_service.dto.resquest.ProfileCreationResquest;
import com.music.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfile toUserProfile(ProfileCreationResquest resquest);
    UserProfileResponse toUserProfileResponse(UserProfile entity);

}
