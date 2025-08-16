package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.VoteDto;
import com.sprints.online_voting_system.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PreAuthorize("hasRole('VOTER')")
    @PostMapping
    public ResponseEntity<Void> castVote(@Valid @RequestBody VoteDto dto, Authentication authentication) {
        String voterEmail = authentication.getName();
        voteService.castVote(voterEmail, dto.getElectionId(), dto.getCandidateId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}