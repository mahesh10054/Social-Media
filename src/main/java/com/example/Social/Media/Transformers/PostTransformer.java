package com.example.Social.Media.Transformers;

import com.example.Social.Media.Models.Post;
import com.example.Social.Media.Models.User;
import com.example.Social.Media.RequestDTOs.addPostRequest;
import com.example.Social.Media.RequestDTOs.addUserRequest;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

public class PostTransformer {
    public static Post convertAddPostRequestToEntity(addPostRequest addPostRequest,User user)
    {
        Post post = Post.builder()
                .text(addPostRequest.getText())
                .url(addPostRequest.getUrl())
                .postStatus(addPostRequest.getPostStatus())
                .disLikeCount(0)
                .likeCount(0)
                .user(user)
                .build();

        return post;
    }
}
