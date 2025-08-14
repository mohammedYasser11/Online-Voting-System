package com.sprints.online_voting_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateDto {
    @NotBlank(message = "Candidate name cannot be empty")
    private String name;

    @NotNull(message = "Election ID must be provided")
    private Long electionId;

}
