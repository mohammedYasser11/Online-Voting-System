package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.VoteStatusDto;
import com.sprints.online_voting_system.exception.ResourceNotFoundException;
import com.sprints.online_voting_system.exception.VotingClosedException;
import com.sprints.online_voting_system.exception.DoubleVotingException;
import com.sprints.online_voting_system.exception.UnauthorizedVoterException;
import com.sprints.online_voting_system.model.*;
import com.sprints.online_voting_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;
    private final ElectionService electionService;
    private final VoteRepository voteRepository;

    @Transactional
    public void castVote(String voterEmail, Long electionId, Long candidateId) {

        User user = userRepository.findByEmail(voterEmail);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (user.getRole() != Role.ROLE_VOTER) {
            throw new UnauthorizedVoterException("User is not authorized to vote");
        }

        Voter voter = voterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Voter profile not found"));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(election.getStartTime()) || now.isAfter(election.getEndTime())) {
            throw new VotingClosedException("Voting is currently closed.");
        }

        if (!election.getAssignedVoters().contains(voter)) {
            throw new UnauthorizedVoterException("Voter is not assigned to this election");
        }

        if (voteRepository.existsByVoterIdAndElectionId(voter.getId(), electionId)) {
            throw new DoubleVotingException("Voter has already cast a vote in this election");
        }

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (!candidate.getElection().getId().equals(electionId)) {
            throw new RuntimeException("Candidate does not belong to this election");
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        vote.setElection(election);
        vote.setTimestamp(now);

        voteRepository.save(vote);
    }

    public boolean hasUserVoted(String voterEmail, Long electionId) {
        User user = userRepository.findByEmail(voterEmail);
        if (user == null) {
            return false;
        }

        Voter voter = voterRepository.findByUserId(user.getId())
                .orElse(null);

        if (voter == null) {
            return false;
        }

        return voteRepository.existsByVoterIdAndElectionId(voter.getId(), electionId);
    }

    public VoteStatusDto getVoteStatus(String voterEmail, Long electionId) {
        VoteStatusDto dto = new VoteStatusDto();
        dto.setHasVoted(hasUserVoted(voterEmail, electionId));
        dto.setElectionName(electionService.getElectionById(electionId).getName());
        dto.setElectionId(electionId);
        dto.setElectionActive(electionService.isElectionActive(electionId));
        return dto;
    }
}