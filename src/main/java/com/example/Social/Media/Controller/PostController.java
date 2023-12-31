package com.example.Social.Media.Controller;

import com.example.Social.Media.RequestDTOs.addPostRequest;
import com.example.Social.Media.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired // By using autowired annotation we create object of another class without using new keyword
    PostService postService;

    // this function for add post
    @PostMapping("/addPost")
    public String addPost(@RequestBody addPostRequest addPostRequest)
    {
        return postService.addPost(addPostRequest);
    }

    // this function for get the post
    @GetMapping("/getPost")
    public ResponseEntity<?> getPost(@RequestParam String userName, @RequestParam String password)
    {
        return postService.getPost(userName,password);
    }
    // this function admin. to see the post of anyone
    @GetMapping("/getOtherAccountPost/{otherUserName}")
    public ResponseEntity<?> getOtherAccountPost(@RequestParam String userName,@RequestParam String password,@PathVariable String otherUserName)
    {
        return postService.getOtherAccountPost(userName,password,otherUserName);
    }
}
