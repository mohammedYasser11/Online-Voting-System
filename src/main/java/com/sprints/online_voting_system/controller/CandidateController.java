package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.CandidateDto;
import com.sprints.online_voting_system.service.CandidateService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    // for admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/candidates")
    public ResponseEntity<CandidateDto> createCandidate(@Valid @RequestBody CandidateDto dto) {
        CandidateDto created = candidateService.createCandidate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // for voters
    @PreAuthorize("hasRole('VOTER')")
    @GetMapping("/elections/{electionId}/candidates")
    public ResponseEntity<List<CandidateDto>> listByElection(@PathVariable Long electionId) {
        List<CandidateDto> list = candidateService.getCandidatesByElection(electionId);
        return ResponseEntity.ok(list);
    }
}