package com.example.Social.Media.Repository;

import com.example.Social.Media.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUserName(String userName);
    User findUserByEmail(String email);
}
