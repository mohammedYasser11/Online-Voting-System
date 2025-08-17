package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // Check if candidate name already exists (globally)
    boolean existsByName(String name);
}