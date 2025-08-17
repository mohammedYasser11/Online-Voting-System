package com.sprints.online_voting_system.dto;

import com.sprints.online_voting_system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoterDto {
    private Long id;
    private String name;
    private String city;
    private User user;
    private int assignedElections;
}