package com.example.Social.Media.Models;

import com.example.Social.Media.Enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String text;
    private String url;
    private String comments;
    @Enumerated(value = EnumType.STRING)
    private PostStatus postStatus;
    private Integer likeCount;
    private Integer disLikeCount;

    @ManyToOne
    @JoinColumn
    private User user;
}
