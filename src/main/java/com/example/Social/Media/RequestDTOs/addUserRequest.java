package com.example.Social.Media.RequestDTOs;

import com.example.Social.Media.Enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class addUserRequest {

    private String userName;
    private String email;
    private String password;
    private String bio;
    private String profilePictureUrl;
    private UserStatus userStatus;
}
