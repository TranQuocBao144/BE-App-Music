package com.example.admin_service.mapper;


import com.example.admin_service.dto.request.UserupdateRequest;
import com.example.admin_service.dto.response.UserResponse;
import com.example.admin_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    UserResponse toUserResponse(User user);
    void updateStatusUser(@MappingTarget User user, UserupdateRequest request);
}