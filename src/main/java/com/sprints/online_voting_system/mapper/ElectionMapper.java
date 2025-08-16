package com.sprints.online_voting_system.mapper;

import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import org.springframework.stereotype.Component;

@Component
public class ElectionMapper
{

    public ElectionResultDto convertToElectionResultDto(Candidate candidate, Long voteCount)
    {
        ElectionResultDto electionResultDto = new ElectionResultDto();
        electionResultDto.setCandidateId(candidate.getId());
        electionResultDto.setCandidateName(candidate.getName());
        electionResultDto.setVoteCount(voteCount);
        return electionResultDto;
    }

}
