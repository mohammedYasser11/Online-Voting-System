package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.CandidateDto;
import com.sprints.online_voting_system.mapper.CandidateMapper;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import com.sprints.online_voting_system.repository.CandidateRepository;
import com.sprints.online_voting_system.repository.ElectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CandidateService
{
    private CandidateRepository candidateRepository;
    private ElectionRepository electionRepository;
    private CandidateMapper candidateMapper;

    public CandidateDto createCandidate(CandidateDto candidateDto)
    {
        if(candidateRepository.existsByName(candidateDto.getName()))
            throw new RuntimeException("Candidate already exists");
        if(!electionRepository.existsElectionById(candidateDto.getElectionId()))
            throw new RuntimeException("Election does not exist");

        Election election = electionRepository.findElectionById(candidateDto.getElectionId());
        Candidate candidate = candidateMapper.convertToEntity(candidateDto, election);
        candidateRepository.save(candidate);

        return candidateMapper.convertToDto(candidate);
    }

    public List<CandidateDto> getCandidatesByElection(Long electionId)
    {
        Election election = electionRepository.findElectionById(electionId);
        List<Candidate> candidates = election.getCandidates();
        List<CandidateDto> candidateDtos = new ArrayList<>();
        for( Candidate candidate : candidates )
        {
            candidateDtos.add(candidateMapper.convertToDto(candidate));
        }
        return candidateDtos;
    }
}
