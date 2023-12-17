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

    public String addUser(addUserRequest addUserRequest) {
        if(userRepository.findUserByUserName(addUserRequest.getUserName()) != null)
            return "User Name Already Exist";

        if(userRepository.findUserByEmail(addUserRequest.getEmail()) != null)
            return "User Email Already Exist";

        User user = UserTransformers.convertAddUserRequestToEntity(addUserRequest);

        userRepository.save(user);

        return "User Added Successfully";
    }

    public String followingRequest(String userName,String followingName) {
        User user = userRepository.findUserByUserName(userName);
        User followinguser = userRepository.findUserByUserName(followingName);
        if(followinguser == null)
            return "You Are Following does Not Exist";
        if(user == null)
            return "User does Not Exist";

        List<String> userFollowingList = user.getFollowing();

        if(userFollowingList == null) {
            userFollowingList = new ArrayList<>();
            user.setFollowing(userFollowingList);
        } else {
            for(String followingName1 : userFollowingList)
            {
                if (followingName1.equals(followingName)) return "You Already Following";
            }
        }
        userFollowingList.add(followingName);

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

    public ResponseEntity<?> getFollowings(String userName) {
        User user = userRepository.findUserByUserName(userName);
        if(user == null) return ResponseEntity.ok("User Not found !!!");

        List<String> followingList = user.getFollowing();

        return ResponseEntity.ok(followingList);
    }

    public ResponseEntity<?> getFollows(String userName) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found !!!");

        List<String> followList = user.getFollow();
        return ResponseEntity.ok(followList);
    }

    public ResponseEntity<?> getAllPost(String userName) {
        User user = userRepository.findUserByUserName(userName);

        if(user == null) return ResponseEntity.ok("User Not found !!!");

        List<Post> postList = user.getPostList();

        List<AddDummyPost> ans = new ArrayList<>();

        for(Post post : postList)
        {
            AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
            ans.add(addDummyPost);
        }
        return ResponseEntity.ok(ans);
    }

    public ResponseEntity<?> getAllAccount(String userName) {
        User user = userRepository.findUserByUserName(userName);
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

    public String deleteAccount(String userName, String deleteUserName) {
        User user = userRepository.findUserByUserName(userName);
        if(user == null) return "User Not found!!!";
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
