package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.VoterDto;
import com.sprints.online_voting_system.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{voterId}/elections/{electionId}")
    public ResponseEntity<Void> removeFromElection(@PathVariable Long voterId, @PathVariable Long electionId) {
        voterService.removeFromElection(voterId, electionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/elections/{electionId}/assign-by-city/{city}")
    public ResponseEntity<Void> assignVotersByCity(@PathVariable Long electionId,
                                                   @PathVariable String city) {
        voterService.assignVotersByCity(electionId, city);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<VoterDto>> getAllVoters() {
        List<VoterDto> voters = voterService.getAllVoters();
        return ResponseEntity.ok(voters);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<VoterDto>> getVotersByCity(@PathVariable String city) {
        List<VoterDto> voters = voterService.getVotersByCity(city);
        return ResponseEntity.ok(voters);
    }

    @GetMapping("/elections/{electionId}")
    public ResponseEntity<List<VoterDto>> getVotersAssignedToElection(@PathVariable Long electionId) {
        List<VoterDto> voters = voterService.getVotersAssignedToElection(electionId);
        return ResponseEntity.ok(voters);
    }
}