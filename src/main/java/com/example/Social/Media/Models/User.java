package com.example.Social.Media.Models;


import com.example.Social.Media.Enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    private String bio;
    private String profilePictureUrl;
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;
    @ElementCollection
    @CollectionTable(name = "follow", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follow")
    private List<String> follow;
    @ElementCollection
    @CollectionTable(name = "following", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following")
    private List<String> following;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Post> postList = new ArrayList<>();

}
