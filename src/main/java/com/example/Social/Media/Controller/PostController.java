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

    @PostMapping("/addPost")
    public String addPost(@RequestBody addPostRequest addPostRequest)
    {
        return postService.addPost(addPostRequest);
    }
    @GetMapping("/getPost")
    public ResponseEntity<?> getPost(@RequestParam String userName)
    {
        return postService.getPost(userName);
    }
    @GetMapping("/getOtherAccountPost/{otherUserName}")
    public ResponseEntity<?> getOtherAccountPost(@RequestParam String userName, @PathVariable String otherUserName)
    {
        return postService.getOtherAccountPost(userName,otherUserName);
    }
}
