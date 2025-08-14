package com.sprints.online_voting_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteDto {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Election ID is required")
    private Long electionId;
}
