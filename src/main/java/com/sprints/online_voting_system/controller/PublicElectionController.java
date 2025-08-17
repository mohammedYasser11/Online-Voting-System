package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.ElectionResponseDto;
import com.sprints.online_voting_system.service.ElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class PublicElectionController {

    private final ElectionService electionService;

    @PreAuthorize("hasRole('VOTER')")
    @GetMapping("/my-elections")
    public ResponseEntity<List<ElectionResponseDto>> getMyElections(Authentication authentication) {
        String voterEmail = authentication.getName();
        List<ElectionResponseDto> elections = electionService.getElectionsByVoterEmail(voterEmail);
        return ResponseEntity.ok(elections);
    }

    @PreAuthorize("hasRole('VOTER')")
    @GetMapping("/active")
    public ResponseEntity<List<ElectionResponseDto>> getActiveElections() {
        List<ElectionResponseDto> elections = electionService.getActiveElections();
        return ResponseEntity.ok(elections);
    }
}