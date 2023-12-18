package com.example.Social.Media.Service;

import com.example.Social.Media.Enums.UserStatus;
import com.example.Social.Media.Models.Post;
import com.example.Social.Media.Models.User;
import com.example.Social.Media.Repository.UserRepository;
import com.example.Social.Media.RequestDTOs.AddDummyPost;
import com.example.Social.Media.RequestDTOs.AddDummyUserRequest;
import com.example.Social.Media.RequestDTOs.addUserRequest;
import com.example.Social.Media.Transformers.DummyTransformer;
import com.example.Social.Media.Transformers.PostTransformer;
import com.example.Social.Media.Transformers.UserTransformers;
import com.example.Social.Media.Transformers.userDummyTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.transform.Transformer;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired // By using autowired annotation we create object of another class without using new keyword
    UserRepository userRepository;

    // this function for create user account
    public String createAccount(addUserRequest addUserRequest) {

        // here we have check username already present of not
        if(userRepository.findUserByUserName(addUserRequest.getUserName()) != null)
            return "User Name Already Exist";

        // here we have check user email already present of not
        if(userRepository.findUserByEmail(addUserRequest.getEmail()) != null)
            return "User Email Already Exist";

        String email = addUserRequest.getEmail();
        if(! emailVerification(email))
        {
            return "Please Enter Valid Email Address";
        }

        User user = UserTransformers.convertAddUserRequestToEntity(addUserRequest);

        // here we have save the user on to the DB
        userRepository.save(user);

        return "User Added Successfully";
    }

    public ResponseEntity<?> getAccount(String userName, String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Does Not Exist");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        return ResponseEntity.ok(userDummyTransformer.convertUserRequestToEntity(user));
    }
    private boolean emailVerification(String email)
        {
            int eLength = email.length();
            String str = "@gmail.com";

            if(eLength <= str.length())
            {
                return false;
            }
            int n = eLength - str.length();
            String s = email.substring(n,eLength);

            if(!str.equals(s))
            {
                return false;
            }
            return true;
        }
    public ResponseEntity<?> updateAccount(String userName, String password, addUserRequest addUserRequest) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Does Not Exist");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        String email = addUserRequest.getEmail();
        if(!emailVerification(email))
        {
            return ResponseEntity.ok("Please Enter Valid Email Address");
        }

        if(!user.getUserName().equals(addUserRequest.getUserName()) && userRepository.findUserByUserName(addUserRequest.getUserName()) != null) return ResponseEntity.ok("Please Enter another userName");

        user.setUserName(addUserRequest.getUserName());
        user.setUserStatus(addUserRequest.getUserStatus());
        user.setBio(addUserRequest.getBio());
        user.setEmail(addUserRequest.getEmail());
        user.setPassword(addUserRequest.getPassword());
        user.setProfilePictureUrl(addUserRequest.getProfilePictureUrl());

        userRepository.save(user);

        return ResponseEntity.ok("Your Profile has been Updated!!!");
    }

    // this function for send following request to another user
    public String sendFollowingRequest(String userName,String password,String followingUserName) {
        User user = userRepository.findUserByUserName(userName); // here we can get user account
        User followinguser = userRepository.findUserByUserName(followingUserName); //here we get following user account

        if(followinguser == null) // here we check following user present or not
            return "You Are Following does Not Exist";
        if(user == null) // here we check user present or not
            return "User does Not Exist";

        if(!user.getPassword().equals(password)) return "Password is Wrong!!!";

        List<String> userFollowingList = user.getFollowing(); //here we get following list

        // if user following list is empty then create new list and set the list using user setFollowing function
        // else we can check user already following another user then we return "You Already Following".
        if(userFollowingList == null) {
            userFollowingList = new ArrayList<>();
            user.setFollowing(userFollowingList);
        } else {
           if(userFollowingList.contains(followingUserName)) return "You Already Following";
        }
        // and add the following user name into the list
        userFollowingList.add(followingUserName);

        List<String> followingUserList = followinguser.getFollow();

        if (followingUserList == null) {
            followingUserList = new ArrayList<>();
            followinguser.setFollow(followingUserList);
        }

        followingUserList.add(userName);

        userRepository.save(user);
        userRepository.save(followinguser);

        return "You Following Successfully";
    }

    public ResponseEntity<?> getFollowings(String userName,String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found !!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        List<String> followingList = user.getFollowing();

        return ResponseEntity.ok(followingList);
    }

    public ResponseEntity<?> getFollows(String userName,String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found !!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        List<String> followList = user.getFollow();
        return ResponseEntity.ok(followList);
    }

    public ResponseEntity<?> getAllPost(String userName,String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found !!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        List<Post> postList = user.getPostList();

        List<AddDummyPost> ans = new ArrayList<>();

        for(Post post : postList)
        {
            AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
            ans.add(addDummyPost);
        }
        return ResponseEntity.ok(ans);
    }

    public ResponseEntity<?> getAllAccount(String userName,String password) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found!!!");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!");

        if(!user.getUserStatus().equals(UserStatus.Admin)){
            return ResponseEntity.ok("You are not a Admin");
        }

        List<AddDummyUserRequest> addDummyUserRequestList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for(User user1 : userList)
        {
            if(!user1.getUserName().equals(userName))
            {
                AddDummyUserRequest addDummyUserRequest = userDummyTransformer.convertUserRequestToEntity(user1);
                addDummyUserRequestList.add(addDummyUserRequest);
            }
        }
        return ResponseEntity.ok(addDummyUserRequestList);
    }

    public String deleteAccount(String userName, String password,String deleteUserName) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return "User Not found!!!";
        if(!user.getPassword().equals(password)) return "Password is Wrong!!!";

        if(!user.getUserStatus().equals(UserStatus.Admin)){
            return "You are not a admin You are not able to delete another account";
        }

        User deletedUserName = userRepository.findUserByUserName(deleteUserName);
        if(deletedUserName == null) return "You want to delete account that account Not found!!!";
        if(deletedUserName.getUserStatus().equals(UserStatus.Admin)){
            return "This is a admin Account You not delete Account";
        }

        List<String> followList = deletedUserName.getFollow(); //get the follow list
        List<String> followingList = deletedUserName.getFollowing();  //get the following list

        //if follow list is not empty then
        if(!followList.isEmpty()){
            for(String followUserName : followList)
            {
                User followUser = userRepository.findUserByUserName(followUserName);
                followUser.getFollowing().remove(deleteUserName);
            }
        }
        // if following list is not empty then
        if(!followingList.isEmpty()){
            for(String followingUserName : followingList)
            {
                User followingUser = userRepository.findUserByUserName(followingUserName);
                followingUser.getFollow().remove(deleteUserName);
            }
        }

        userRepository.delete(deletedUserName);

        return "delete account successfully";
    }



}
