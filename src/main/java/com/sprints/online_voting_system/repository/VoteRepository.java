package com.sprints.online_voting_system.repository;

import com.sprints.online_voting_system.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>
{
    @Query(value = "select count(*) " +
                    "from votes " +
                    "where candidate_Id = :candidate_Id and election_Id = :election_Id"
                    ,nativeQuery = true)
    public Long countByCandidate_IdAndElection_Id(@Param("candidate_Id") Long candidate_Id,
                                                  @Param("election_Id") Long election_Id);
}
