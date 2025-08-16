package com.sprints.online_voting_system.mapper;

import com.sprints.online_voting_system.dto.CandidateDto;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper
{
    public CandidateDto convertToDto(Candidate candidate)
    {
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setName(candidate.getName());
        candidateDto.setElectionId(candidate.getElection().getId());
        return candidateDto;
    }

    public Candidate convertToEntity(CandidateDto candidateDto, Election election)
    {
        Candidate candidate = new Candidate();
        candidate.setName(candidateDto.getName());
        candidate.setElection(election);
        return candidate;
    }
}
