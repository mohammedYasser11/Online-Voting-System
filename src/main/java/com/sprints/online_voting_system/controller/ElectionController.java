package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.ElectionResultDto;
import com.sprints.online_voting_system.service.ElectionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/elections")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @GetMapping("/{electionId}/results")
    public ResponseEntity<List<ElectionResultDto>> getResults(@PathVariable Long electionId) {
        List<ElectionResultDto> results = electionService.getResults(electionId);
        return ResponseEntity.ok(results);
    }
}