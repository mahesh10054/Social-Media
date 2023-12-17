package com.example.Social.Media.Controller;

import com.example.Social.Media.RequestDTOs.addUserRequest;
import com.example.Social.Media.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Controller Layer Annotation
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired // By using autowired annotation we create object of another class without using new keyword
    UserService userService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody addUserRequest addUserRequest)
    {
        return userService.addUser(addUserRequest);
    }
    @PostMapping("/followingRequest")
    public String followingRequest(@RequestParam String userName,@RequestParam String followerName)
    {
        return userService.followingRequest(userName,followerName);
    }
    @GetMapping("/getFollowings")
    public ResponseEntity<?> getFollowings(@RequestParam String userName)
    {
        return userService.getFollowings(userName);
    }
    @GetMapping("/getFollows")
    public ResponseEntity<?> getFollows(@RequestParam String userName)
    {
        return userService.getFollows(userName);
    }
    @GetMapping("/getAllPost")
    public ResponseEntity<?> getAllPost(@RequestParam String userName)
    {
        return userService.getAllPost(userName);
    }

    @GetMapping("/getAllAccount")
    public ResponseEntity<?> getAllAccount(@RequestParam String userName)
    {
        return userService.getAllAccount(userName);
    }
    @DeleteMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String userName, @RequestParam String deleteUserName)
    {
        return userService.deleteAccount(userName,deleteUserName);
    }
}
