package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.VoterDto;
import com.sprints.online_voting_system.exception.DoubleVotingException;
import com.sprints.online_voting_system.exception.ResourceNotFoundException;
import com.sprints.online_voting_system.mapper.VoterMapper;
import com.sprints.online_voting_system.model.Election;
import com.sprints.online_voting_system.model.Voter;
import com.sprints.online_voting_system.repository.ElectionRepository;
import com.sprints.online_voting_system.repository.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoterService {

    private final VoterRepository voterRepository;
    private final VoterMapper voterMapper;
    private final ElectionRepository electionRepository;

    @Transactional
    public void assignToElection(Long voterId, Long electionId) {

        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new ResourceNotFoundException("Voter not found"));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        if (election.getAssignedVoters().contains(voter)) {
            throw new DoubleVotingException("Voter is already assigned to this election");
        }

        election.getAssignedVoters().add(voter);
        voter.getElections().add(election);

        electionRepository.save(election);
    }

    @Transactional
    public void removeFromElection(Long voterId, Long electionId) {
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new ResourceNotFoundException("Voter not found"));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        election.getAssignedVoters().remove(voter);
        voter.getElections().remove(election);

        electionRepository.save(election);
    }

    public List<VoterDto> getAllVoters() {
        return voterRepository.findAll()
                .stream()
                .map(voterMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VoterDto> getVotersByCity(String city) {
        return voterRepository.findByCity(city)
                .stream()
                .map(voterMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VoterDto> getVotersAssignedToElection(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        return election.getAssignedVoters()
                .stream()
                .map(voterMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public void assignVotersByCity(Long electionId, String city) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        List<Voter> votersInCity = voterRepository.findByCity(city);

        for (Voter voter : votersInCity) {
            if (!election.getAssignedVoters().contains(voter)) {
                election.getAssignedVoters().add(voter);
                voter.getElections().add(election);
            }
        }

        electionRepository.save(election);
    }
}