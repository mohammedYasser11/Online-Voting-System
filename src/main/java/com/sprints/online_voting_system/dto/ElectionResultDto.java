package com.sprints.online_voting_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElectionResultDto {
    private Long candidateId;
    private String candidateName;
    private long voteCount;
}
