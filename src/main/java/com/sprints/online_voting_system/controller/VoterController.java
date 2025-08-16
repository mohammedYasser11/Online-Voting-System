package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/voters")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class VoterController {

    private final VoterService voterService;

    @PostMapping("/{voterId}/elections/{electionId}")
    public ResponseEntity<Void> assignToElection(@PathVariable Long voterId, @PathVariable Long electionId) {
        voterService.assignToElection(voterId, electionId);
        return ResponseEntity.ok().build();
    }
}