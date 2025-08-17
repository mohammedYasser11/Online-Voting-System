package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = "SELECT COUNT(*) " +
            "FROM votes " +
            "WHERE candidate_id = :candidateId AND election_id = :electionId",
            nativeQuery = true)
    Long countByCandidate_IdAndElection_Id(@Param("candidateId") Long candidateId,
                                           @Param("electionId") Long electionId);

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
}