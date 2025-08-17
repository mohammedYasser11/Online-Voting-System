package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.ElectionDto;
import com.sprints.online_voting_system.dto.ElectionResponseDto;
import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.service.ElectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/elections")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping
    public ResponseEntity<ElectionResponseDto> createElection(@Valid @RequestBody ElectionDto dto) {
        ElectionResponseDto created = electionService.createElection(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ElectionResponseDto>> getAllElections() {
        List<ElectionResponseDto> elections = electionService.getAllElections();
        return ResponseEntity.ok(elections);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ElectionResponseDto>> getActiveElections() {
        List<ElectionResponseDto> elections = electionService.getActiveElections();
        return ResponseEntity.ok(elections);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ElectionResponseDto>> getUpcomingElections() {
        List<ElectionResponseDto> elections = electionService.getUpcomingElections();
        return ResponseEntity.ok(elections);
    }

    @GetMapping("/{electionId}")
    public ResponseEntity<ElectionResponseDto> getElectionById(@PathVariable Long electionId) {
        ElectionResponseDto election = electionService.getElectionById(electionId);
        return ResponseEntity.ok(election);
    }

    @GetMapping("/{electionId}/results")
    public ResponseEntity<List<ElectionResultDto>> getResults(@PathVariable Long electionId) {
        List<ElectionResultDto> results = electionService.getResults(electionId);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{electionId}")
    public ResponseEntity<Void> deleteElection(@PathVariable Long electionId) {
        electionService.deleteElection(electionId);
        return ResponseEntity.noContent().build();
    }
}
