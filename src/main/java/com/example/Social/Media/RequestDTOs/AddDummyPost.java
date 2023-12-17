package com.example.Social.Media.RequestDTOs;

import com.example.Social.Media.Enums.PostStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddDummyPost {
    private String text;
    private String url;
    private String comments;
    private PostStatus postStatus;
    private Integer likeCount;
    private Integer disLikeCount;
}
