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

    // this function for create user account
    @PostMapping("/createAccount")
    public String createAccount(@RequestBody addUserRequest addUserRequest)
    {
        return userService.createAccount(addUserRequest);
    }
    @PostMapping("/getAccount")
    public ResponseEntity<?> getAccount(@RequestParam String userName,@RequestParam String password)
    {
        return userService.getAccount(userName,password);
    }
    @PostMapping("/updateAccount")
    public ResponseEntity<?> updateAccount(@RequestParam String userName,@RequestParam String password, @RequestBody addUserRequest addUserRequest)
    {
        return userService.updateAccount(userName,password,addUserRequest);
    }
    // this function for send following request to another user
    @PostMapping("/sendFollowingRequest")
    public String sendFollowingRequest(@RequestParam String userName,@RequestParam String password,@RequestParam String followingUserName)
    {
        return userService.sendFollowingRequest(userName, password,followingUserName);
    }

    // this function for get following list
    @GetMapping("/getFollowings")
    public ResponseEntity<?> getFollowings(@RequestParam String userName,@RequestParam String password)
    {
        return userService.getFollowings(userName,password);
    }

    // this function for get follow list
    @GetMapping("/getFollows")
    public ResponseEntity<?> getFollows(@RequestParam String userName, @RequestParam String password)
    {
        return userService.getFollows(userName,password);
    }

    // this function for see the post of another user
    @GetMapping("/getAllPostOfUser")
    public ResponseEntity<?> getAllPostOfUser(@RequestParam String userName, @RequestParam String password)
    {
        return userService.getAllPost(userName,password);
    }

    // this function for only admin which can get all user account
    @GetMapping("/getAllAccount")
    public ResponseEntity<?> getAllAccount(@RequestParam String userName, @RequestParam String password)
    {
        return userService.getAllAccount(userName,password);
    }
    // this account for admin. if admin want to delete user account
    @DeleteMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String userName, @RequestParam String password, @RequestParam String deleteUserName)
    {
        return userService.deleteAccount(userName,password,deleteUserName);
    }
}
