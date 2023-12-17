package com.example.Social.Media.RequestDTOs;

import com.example.Social.Media.Enums.UserStatus;
import com.example.Social.Media.Models.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class AddDummyUserRequest {
    private Integer userId;
    private String userName;
    private String email;
    private String password;
    private String bio;
    private String profilePictureUrl;
    private UserStatus userStatus;
    private List<String> follow = new ArrayList<>();
    private List<String> following = new ArrayList<>();
    //List<Post> postList = new ArrayList<>();
}
