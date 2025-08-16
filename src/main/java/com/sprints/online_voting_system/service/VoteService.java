package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.model.Candidate;
import com.sprints.online_voting_system.model.User;
import com.sprints.online_voting_system.repository.CandidateRepository;
import com.sprints.online_voting_system.repository.ElectionRepository;
import com.sprints.online_voting_system.repository.UserRepository;
import com.sprints.online_voting_system.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteService {
    private UserRepository userRepository;
    private CandidateRepository candidateRepository;
    private ElectionRepository electionRepository;
    private VoteRepository voteRepository;

    /*
    public void castVote(String voterEmail,Long electionId, Long CandidateId)
    {
        if(!userRepository.existsByEmail(voterEmail))
            throw new RuntimeException("User doesn't exist");
        else if(!candidateRepository.existsById(CandidateId))
            throw new RuntimeException("Candidate doesn't exist");
        else if(!electionRepository.existsById(electionId))
            throw new RuntimeException("Election doesn't exist");

        User user = userRepository.findByEmail(voterEmail);
        Candidate candidate = candidateRepository.findById(CandidateId).get();
    }
     */
}
