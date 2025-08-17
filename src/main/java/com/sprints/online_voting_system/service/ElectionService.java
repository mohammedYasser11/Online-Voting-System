package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.ElectionDto;
import com.sprints.online_voting_system.dto.ElectionResponseDto;
import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.exception.ResourceNotFoundException;
import com.sprints.online_voting_system.mapper.ElectionMapper;
import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.Election;
import com.sprints.online_voting_system.repository.ElectionRepository;
import com.sprints.online_voting_system.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final VoteRepository voteRepository;
    private final ElectionMapper electionMapper;

    @Transactional
    public ElectionResponseDto createElection(ElectionDto electionDto) {
        // Validate election name is unique
        if (electionRepository.existsByName(electionDto.getName())) {
            throw new RuntimeException("Election with this name already exists");
        }

        // Validate time logic
        if (electionDto.getEndTime().isBefore(electionDto.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        if (electionDto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start time cannot be in the past");
        }

        // Create election
        Election election = new Election();
        election.setName(electionDto.getName());
        election.setStartTime(electionDto.getStartTime());
        election.setEndTime(electionDto.getEndTime());

        Election savedElection = electionRepository.save(election);
        return electionMapper.mapToResponseDto(savedElection);
    }

    public List<ElectionResultDto> getResults(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        List<Candidate> candidates = election.getCandidates();
        List<ElectionResultDto> results = new ArrayList<>();

        for (Candidate candidate : candidates) {
            Long voteCount = voteRepository.countByCandidate_IdAndElection_Id(candidate.getId(), election.getId());
            results.add(electionMapper.convertToElectionResultDto(candidate, voteCount));
        }

        // Sort results by vote count in descending order
        results.sort(Comparator.comparing(ElectionResultDto::getVoteCount, Comparator.reverseOrder()));

        return results;
    }

    public List<ElectionResponseDto> getAllElections() {
        List<Election> elections = electionRepository.findAll();
        return elections.stream()
                .map(electionMapper::mapToResponseDto)
                .toList();
    }

    public List<ElectionResponseDto> getElectionsByVoterEmail(String email) {
        return electionRepository.getElectionsByVoterEmail(email)
                .stream()
                .map(electionMapper::mapToResponseDto)
                .toList();
    }

    public List<ElectionResponseDto> getActiveElections() {
        LocalDateTime now = LocalDateTime.now();
        List<Election> activeElections = electionRepository.findActiveElections(now);
        return activeElections.stream()
                .map(electionMapper::mapToResponseDto)
                .toList();
    }

    public List<ElectionResponseDto> getUpcomingElections() {
        LocalDateTime now = LocalDateTime.now();
        List<Election> upcomingElections = electionRepository.findUpcomingElections(now);
        return upcomingElections.stream()
                .map(electionMapper::mapToResponseDto)
                .toList();
    }

    public ElectionResponseDto getElectionById(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));
        return electionMapper.mapToResponseDto(election);
    }

    @Transactional
    public void deleteElection(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        // Check if election has already started
        if (LocalDateTime.now().isAfter(election.getStartTime())) {
            throw new RuntimeException("Cannot delete an election that has already started");
        }

        electionRepository.delete(election);
    }

    public boolean isElectionActive(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(election.getStartTime()) && now.isBefore(election.getEndTime());
    }
}