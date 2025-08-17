package com.sprints.online_voting_system.mapper;

import com.sprints.online_voting_system.dto.ElectionResponseDto;
import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ElectionMapper
{

    public ElectionResultDto convertToElectionResultDto(Candidate candidate, Long voteCount)
    {
        ElectionResultDto dto = new ElectionResultDto();
        dto.setCandidateId(candidate.getId());
        dto.setCandidateName(candidate.getName());
        dto.setVoteCount(voteCount);
        return dto;
    }

    public ElectionResponseDto mapToResponseDto(Election election) {
        LocalDateTime now = LocalDateTime.now();
        boolean isActive = now.isAfter(election.getStartTime()) && now.isBefore(election.getEndTime());

        return new ElectionResponseDto(
                election.getId(),
                election.getName(),
                election.getStartTime(),
                election.getEndTime(),
                isActive,
                election.getCandidates().size(),
                election.getAssignedVoters().size()
        );
    }

}
