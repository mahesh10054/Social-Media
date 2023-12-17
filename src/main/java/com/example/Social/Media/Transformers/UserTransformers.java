package com.example.Social.Media.Transformers;

import com.example.Social.Media.Models.User;
import com.example.Social.Media.RequestDTOs.addUserRequest;

import java.util.ArrayList;

public class UserTransformers {
    public static User convertAddUserRequestToEntity(addUserRequest addUserRequest)
    {
        User user = User.builder()
                .userName(addUserRequest.getUserName())
                .userStatus(addUserRequest.getUserStatus())
                .email(addUserRequest.getEmail())
                .password(addUserRequest.getPassword())
                .bio(addUserRequest.getBio())
                .profilePictureUrl(addUserRequest.getProfilePictureUrl())
                .build();

        return user;
    }
}
