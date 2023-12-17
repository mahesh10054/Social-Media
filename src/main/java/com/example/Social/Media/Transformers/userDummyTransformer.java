package com.example.Social.Media.Transformers;

import com.example.Social.Media.Models.User;
import com.example.Social.Media.RequestDTOs.AddDummyUserRequest;

public class userDummyTransformer {
    public static AddDummyUserRequest convertUserRequestToEntity(User user){
        AddDummyUserRequest addDummyUserRequest = AddDummyUserRequest.builder()
                .userId(user.getUserId())
                .bio(user.getBio())
                .userName(user.getUserName())
                .userStatus(user.getUserStatus())
                .email(user.getEmail())
                .follow(user.getFollow())
                .following(user.getFollowing())
                //.postList(user.getPostList())
                .profilePictureUrl(user.getProfilePictureUrl())
                .password(user.getPassword())
                .build();

        return addDummyUserRequest;
    }
}
