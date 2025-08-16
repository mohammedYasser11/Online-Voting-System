package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByEmailAndPassword(String email, String password);
}