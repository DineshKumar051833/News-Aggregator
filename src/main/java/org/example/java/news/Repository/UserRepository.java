package org.example.java.news.Repository;

import org.example.java.news.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUserByEmailAndPassword(String email,String password);
    Optional<User> getUserByEmail(String email);
}
