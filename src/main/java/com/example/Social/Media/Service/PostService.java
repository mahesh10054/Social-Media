package com.example.Social.Media.Service;

import com.example.Social.Media.Enums.PostStatus;
import com.example.Social.Media.Enums.UserStatus;
import com.example.Social.Media.Models.Post;
import com.example.Social.Media.Models.User;
import com.example.Social.Media.Repository.PostRepository;
import com.example.Social.Media.Repository.UserRepository;
import com.example.Social.Media.RequestDTOs.AddDummyPost;
import com.example.Social.Media.RequestDTOs.addPostRequest;
import com.example.Social.Media.Transformers.DummyTransformer;
import com.example.Social.Media.Transformers.PostTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired // By using autowired annotation we create object of another class without using new keyword
    PostRepository postRepository;
    @Autowired // By using autowired annotation we create object of another class without using new keyword
    UserRepository userRepository;

    public String addPost(addPostRequest addPostRequest) {
         User user = userRepository.findUserByUserName(addPostRequest.getUserName());
         String password = addPostRequest.getPassword();

         if(user == null) return "User Not Found";
        if(!user.getPassword().equals(password)) return "Password is Wrong!!!";

         Post post = PostTransformer.convertAddPostRequestToEntity(addPostRequest,user);

         user.getPostList().add(post);

         userRepository.save(user);

         return "Post Added Successfully";
    }

    public ResponseEntity<?> getPost(String userName,String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not Found !!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        List<Post> postList = user.getPostList();
        if(postList == null) return ResponseEntity.ok("User dont Have Post !!!");

        List<AddDummyPost> ans = new ArrayList<>();

        for(Post post : postList)
        {
            PostStatus postStatus = post.getPostStatus();
            AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
            ans.add(addDummyPost);
        }

        return ResponseEntity.ok(ans);
    }

    public ResponseEntity<?> getOtherAccountPost(String userName, String password,String otherUserName) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not Found !!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        User otherUser = userRepository.findUserByUserName(otherUserName);
        if(otherUser == null) return ResponseEntity.ok("Other User Not Found !!!");

        List<Post> postList = otherUser.getPostList();
        if(postList == null) return ResponseEntity.ok("User dont Have Post !!!");

        List<AddDummyPost> ans = new ArrayList<>();

        for(Post post : postList)
        {
            PostStatus postStatus = post.getPostStatus();
            if(user.getUserStatus().equals(UserStatus.Admin) || postStatus.equals(PostStatus.Public)) {
                AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
                ans.add(addDummyPost);
            }
        }

        return ResponseEntity.ok(ans);
    }
}
