package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);
    boolean existsByEmail(String email);

}