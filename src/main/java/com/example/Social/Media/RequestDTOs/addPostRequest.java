package com.example.Social.Media.RequestDTOs;

import com.example.Social.Media.Enums.PostStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class addPostRequest {
    private String text;
    private String url;
    private PostStatus postStatus;
    private String userName;
    private String password;

}
