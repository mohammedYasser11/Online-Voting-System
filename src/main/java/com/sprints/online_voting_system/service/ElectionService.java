package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.mapper.ElectionMapper;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import com.sprints.online_voting_system.repository.ElectionRepository;
import com.sprints.online_voting_system.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ElectionService
{
    private ElectionRepository electionRepository;
    private VoteRepository voteRepository;
    private ElectionMapper electionMapper;

    public List<ElectionResultDto> getResults(Long electionId)
    {
        if(!electionRepository.existsById(electionId))
            throw new RuntimeException("Election does not exist");

        Election election = electionRepository.findElectionById(electionId);
        List<Candidate> candidates = election.getCandidates();

        List<ElectionResultDto> results = new ArrayList<>();
        for(Candidate candidate : candidates)
        {
            Long voteCountPerCandidate = voteCount(candidate.getId(),election.getId());
            results.add(electionMapper.convertToElectionResultDto(candidate,voteCountPerCandidate));
        }
        return results;

    }

    public Long voteCount(Long candidateId, Long ElectionId)
    {
        return voteRepository.countByCandidate_IdAndElection_Id(candidateId,ElectionId);
    }
}
