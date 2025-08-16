package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long>
{
    public boolean existsElectionById(Long id);

    public Election findElectionById(Long id);
}
