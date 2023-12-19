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

    // this function for add post
    public String addPost(addPostRequest addPostRequest) {
         User user = userRepository.findUserByUserName(addPostRequest.getUserName()); // we get the user from db

        if(user == null) return "User Not Found"; // if user not found then return "User Not Found"

         String password = addPostRequest.getPassword(); // we get the password for check it is correct or not

        if(!user.getPassword().equals(password)) return "Password is Wrong!!!"; // here we check password is correct ir not

         Post post = PostTransformer.convertAddPostRequestToEntity(addPostRequest,user); // here we convert post request to entity

         user.getPostList().add(post); // in the list we add the post

         userRepository.save(user); // then save in to the db

         return "Post Added Successfully";
    }

    // this function for get the post
    public ResponseEntity<?> getPost(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // we get the user from db

        if(user == null) return ResponseEntity.ok("User Not Found !!!"); // if user not found then return "User Not Found"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // here we check password is correct ir not

        List<Post> postList = user.getPostList(); // here we get the post list
        if(postList.isEmpty()) return ResponseEntity.ok("User dont Have Post !!!"); // post list is empty then return "User dont Have Post !!!"

        List<AddDummyPost> ans = new ArrayList<>(); // here we create empty list

        // here we go to the all post and add into the list
        for(Post post : postList)
        {
            PostStatus postStatus = post.getPostStatus();
            AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
            ans.add(addDummyPost);
        }

        return ResponseEntity.ok(ans); // return the post of list
    }

    // this function admin. to see the post of anyone
    public ResponseEntity<?> getOtherAccountPost(String userName, String password,String otherUserName) {
        User user = userRepository.findUserByUserName(userName);// we get the user from db

        if(user == null) return ResponseEntity.ok("User Not Found !!!"); // here we check user present in db
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // here we check password is correct or not

        User otherUser = userRepository.findUserByUserName(otherUserName);// here we get other user from db
        if(otherUser == null) return ResponseEntity.ok("Other User Not Found !!!"); // // here we check other user present in db

        List<Post> postList = otherUser.getPostList(); // get the post list
        if(postList.isEmpty()) return ResponseEntity.ok("User dont Have Post !!!");// check list is empty or not

        List<AddDummyPost> ans = new ArrayList<>();// create empty list

        // going to all post then we can check this is admin user
        // if yes then add into the list
        // if no then we check post status is public
        // if yes then add into the list
        // if no then dont add into list

        for(Post post : postList)
        {
            PostStatus postStatus = post.getPostStatus();
            UserStatus userStatus = user.getUserStatus();
            if(userStatus.equals(UserStatus.Admin) || postStatus.equals(PostStatus.Public)) {
                AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
                ans.add(addDummyPost);
            }
        }

        return ResponseEntity.ok(ans);
    }
}
