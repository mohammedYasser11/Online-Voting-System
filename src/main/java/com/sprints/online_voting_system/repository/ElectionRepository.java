package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

    boolean existsElectionById(Long id);

    Election findElectionById(Long id);

    @Query("SELECT e FROM Election e " +
            "JOIN e.assignedVoters v " +
            "JOIN v.user u " +
            "WHERE u.email = :email")
    List<Election> getElectionsByVoterEmail(@Param("email") String email);

    boolean existsByName(String name);

    @Query("SELECT e FROM Election e WHERE :now BETWEEN e.startTime AND e.endTime")
    List<Election> findActiveElections(@Param("now") LocalDateTime now);

    @Query("SELECT e FROM Election e WHERE e.startTime > :now")
    List<Election> findUpcomingElections(@Param("now") LocalDateTime now);
}