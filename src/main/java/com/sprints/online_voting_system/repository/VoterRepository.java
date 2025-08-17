package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

    Optional<Voter> findByUserId(Long userId);
    List<Voter> findByCity(String city);

}