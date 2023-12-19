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

        // here we have check username already present or not
        if(userRepository.findUserByUserName(addUserRequest.getUserName()) != null)
            return "User Name Already Exist";

        // here we have check user email already present or not
        if(userRepository.findUserByEmail(addUserRequest.getEmail()) != null)
            return "User Email Already Exist";

        // here take user email
        String email = addUserRequest.getEmail();

        // here we check email is valid or not
        if(! emailVerification(email))
        {
            return "Please Enter Valid Email Address";
        }

        // here we get the user
        User user = UserTransformers.convertAddUserRequestToEntity(addUserRequest);

        // here we have save the user on to the DB
        userRepository.save(user);

        return "User Added Successfully";
    }

    // this function return all information of user account
    public ResponseEntity<?> getAccount(String userName, String password) {
        User user = userRepository.findUserByUserName(userName); //here we get user

        if(user == null) return ResponseEntity.ok("User Does Not Exist"); // if user not present then return "User Does Not Exist"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // here we check password is correct or not

        return ResponseEntity.ok(userDummyTransformer.convertUserRequestToEntity(user)); // here we return user
    }

    // this function for user enter email is correct or not
    private boolean emailVerification(String email)
        {
            int eLength = email.length(); // we get user email length
            String str = "@gmail.com"; // here we already define string

            if(eLength <= str.length()) //here we check user email length is less then equal to already define string then return false
            {
                return false;
            }

            int n = eLength - str.length();
            String s = email.substring(n,eLength);//

            if(!str.equals(s))
            {
                return false;
            }
            return true;
        }

    // this function use to update user information
    public ResponseEntity<?> updateAccount(String userName, String password, addUserRequest addUserRequest) {
        User user = userRepository.findUserByUserName(userName); // firstly we find in db user present

        if(user == null) return ResponseEntity.ok("User Does Not Exist"); //if not then return "User Does Not Exist"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); //we check password if wrong then return "Password is Wrong!!!"

        String email = addUserRequest.getEmail(); // we get user email

        // if user email not valid then return "Please Enter Valid Email Address"
        if(!emailVerification(email))
        {
            return ResponseEntity.ok("Please Enter Valid Email Address");
        }

        // here we can check updated userName already present in db
        if(!user.getUserName().equals(addUserRequest.getUserName()) && userRepository.findUserByUserName(addUserRequest.getUserName()) != null)
            return ResponseEntity.ok("Please Enter another userName");

        // here we set the all user information
        user.setUserName(addUserRequest.getUserName());
        user.setUserStatus(addUserRequest.getUserStatus());
        user.setBio(addUserRequest.getBio());
        user.setEmail(addUserRequest.getEmail());
        user.setPassword(addUserRequest.getPassword());
        user.setProfilePictureUrl(addUserRequest.getProfilePictureUrl());

        userRepository.save(user); // we save the user into the db;

        return ResponseEntity.ok("Your Profile has been Updated!!!");// and return the message
    }

    // this function for send following request to another user
    public String sendFriendRequest(String userName,String password,String followingUserName) {
        User user = userRepository.findUserByUserName(userName); // here we can get user account
        User followinguser = userRepository.findUserByUserName(followingUserName); //here we get following user account

        if(followinguser == null) // here we check following user present or not
            return "You Are Following User does Not Exist";
        if(user == null) // here we check user present or not
            return "User does Not Exist";

        if(!user.getPassword().equals(password)) return "Password is Wrong!!!"; //we check password if wrong then return "Password is Wrong!!!"

        List<String> friendRequestList = followinguser.getAcceptFriendRequest(); //here we get accept friend request list from anotherUser

        // if user AcceptFriendRequest list is empty then create new list and set that list
        // else we can check userName is already present in then we return "You already send a friend request!!!".
        if(friendRequestList == null) {
            friendRequestList = new ArrayList<>();
            followinguser.setAcceptFriendRequest(friendRequestList);
        }
        else {
                if(friendRequestList.contains(userName)) return "You already send a friend request!!!";
        }

        // and add the following user name into the list
        friendRequestList.add(userName);

        followinguser.setAcceptFriendRequest(friendRequestList); // here we set the list
        userRepository.save(followinguser); // save into the db

        return "You Following Request Send Successfully"; // return message
    }
    // in this function you can see who can send friend request
    public ResponseEntity<?> getFriendRequest(String userName, String password) {
        User user = userRepository.findUserByUserName(userName); // here we can get user account

        if(user == null) // here we check user preset or not
            return ResponseEntity.ok("User does Not Exist");
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // if password is wrong then return "Password is Wrong!!!"
        List<String> friendRequestList = user.getAcceptFriendRequest(); // here we get accept friend request list

        if(friendRequestList.isEmpty()) return ResponseEntity.ok("Dont Have a Friend Request!!!"); // if lis is empty then return "Dont Have a Friend Request!!!"

        return ResponseEntity.ok(friendRequestList); // if list not empty then return that list
    }

    // here we can accept friend request
    public String acceptFriendRequest(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // here we can get user account

        if(user == null) // here we check user present or not
            return "User does Not Exist";
        if(!user.getPassword().equals(password)) return "Password is Wrong!!!";// if password is wrong then return "Password is Wrong!!!"
        List<String> getFriendRequestList = user.getAcceptFriendRequest(); // here we get accept friend request list

        if(getFriendRequestList.isEmpty()) return "Dont Have a Friend Request!!!"; // if lis is empty then return "Dont Have a Friend Request!!!"

        List<String> userFollowList = user.getFollow();

        if(userFollowList == null) {
            userFollowList = new ArrayList<>();
            user.setFollow(userFollowList);
        }

        for(String friend : getFriendRequestList)
        {
            User friendUser = userRepository.findUserByUserName(friend);

            userFollowList.add(friend);

            List<String> followingUserList = friendUser.getFollowing();

            if (followingUserList == null) {
                followingUserList = new ArrayList<>();
                friendUser.setFollowing(followingUserList);
            }

            followingUserList.add(userName);

            friendUser.setFollowing(followingUserList);
            userRepository.save(friendUser);
        }

        user.setAcceptFriendRequest(new ArrayList<>());
        user.setFollow(userFollowList);

        userRepository.save(user);

        return "Friend Request Accepted";
    }
    // this function for get following list
    public ResponseEntity<?> getFollowings(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // we get user account from db

        if(user == null) return ResponseEntity.ok("User Not found !!!"); // if not found then return "User Not found!!!"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // if password is wrong then return "Password is Wrong!!!"

        List<String> followingList = user.getFollowing(); // here we get following list

        return ResponseEntity.ok(followingList); // here we return following list
    }

    // this function for get follow list
    public ResponseEntity<?> getFollows(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // we get user account from db

        if(user == null) return ResponseEntity.ok("User Not found !!!");// if not found then return "User Not found!!!"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // if password is wrong then return "Password is Wrong!!!"

        List<String> followList = user.getFollow(); // here we get follow list

        return ResponseEntity.ok(followList); // return follow list
    }

    // this function for see the post of another user
    public ResponseEntity<?> getAllPost(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // we get user account from db

        if(user == null) return ResponseEntity.ok("User Not found !!!");// if not found then return "User Not found!!!"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // if password is wrong then return "Password is Wrong!!!"

        List<Post> postList = user.getPostList(); //here we get the list of user post

        List<AddDummyPost> ans = new ArrayList<>();// here we create a empty list

        // using for loop we go all the post and store in to the list then return that list
        for(Post post : postList)
        {
            AddDummyPost addDummyPost = DummyTransformer.convertAddDummyRequestRequestToEntity(post);
            ans.add(addDummyPost);
        }
        return ResponseEntity.ok(ans);
    }

    // this function only for admin which can get all user account
    public ResponseEntity<?> getAllAccount(String userName,String password) {
        User user = userRepository.findUserByUserName(userName); // we get user account from db

        if(user == null) return ResponseEntity.ok("User Not found!!!"); // if not found then return "User Not found!!!"
        if(!user.getPassword().equals(password)) return ResponseEntity.ok("Password is Wrong!!!"); // if password is wrong then return "Password is Wrong!!!"

        // here we check this account status is admin if not then return "You are not a Admin"
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
    // this function for admin. if admin want to delete any user account
    public String deleteAccount(String userName, String password,String deleteUserName) {
        User user = userRepository.findUserByUserName(userName);// we get user

        if(user == null) return "User Not found!!!"; //user not present then return "User Not found!!!"
        if(!user.getPassword().equals(password)) return "Password is Wrong!!!"; // if password is wrong then return "Password is Wrong!!!"

        // we check this admin account if not then return "You are not a admin You are not able to delete another account"
        if(!user.getUserStatus().equals(UserStatus.Admin)){
            return "You are not a admin You are not able to delete another account";
        }

        // we get another user account
        User deletedUserName = userRepository.findUserByUserName(deleteUserName);
        if(deletedUserName == null) return "You want to delete account that account Not found!!!"; // this account not found then return "You want to delete account that account Not found!!!"

        List<String> followList = deletedUserName.getFollow(); //get the follow list
        List<String> followingList = deletedUserName.getFollowing();  //get the following list

        //if follow list is not empty then remove his name from following list
        if(!followList.isEmpty()){
            for(String followUserName : followList)
            {
                User followUser = userRepository.findUserByUserName(followUserName);
                followUser.getFollowing().remove(deleteUserName);
            }
        }
        // if following list is not empty then remove his name from follow list
        if(!followingList.isEmpty()){
            for(String followingUserName : followingList)
            {
                User followingUser = userRepository.findUserByUserName(followingUserName);
                followingUser.getFollow().remove(deleteUserName);
            }
        }
        userRepository.delete(deletedUserName); // we delete that user account

        return "delete account successfully"; //return message
    }
}
