package com.example.Social.Media.Transformers;

import com.example.Social.Media.Models.Post;
import com.example.Social.Media.Models.User;
import com.example.Social.Media.RequestDTOs.AddDummyPost;
import com.example.Social.Media.RequestDTOs.addPostRequest;

public class DummyTransformer {
    public static AddDummyPost convertAddDummyRequestRequestToEntity(Post post)
    {
        AddDummyPost addDummyPost = AddDummyPost.builder()
                .url(post.getUrl())
                .comments(post.getComments())
                .disLikeCount(post.getDisLikeCount())
                .likeCount(post.getLikeCount())
                .postStatus(post.getPostStatus())
                .text(post.getText())
                .build();
        return addDummyPost;
    }
}
