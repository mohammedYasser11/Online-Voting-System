package com.sprints.online_voting_system.mapper;

import com.sprints.online_voting_system.dto.VoterDto;
import com.sprints.online_voting_system.model.Voter;
import org.springframework.stereotype.Component;

@Component
public class VoterMapper
{

    public VoterDto convertToDto(Voter voter)
    {
        VoterDto voterDto = new VoterDto();
        voterDto.setName(voter.getName());
        voterDto.setId(voter.getId());
        voterDto.setCity(voter.getCity());
        voterDto.setEmail(voter.getUser().getEmail());
        voterDto.setPassword(voter.getUser().getPassword());
        voterDto.setRole(voter.getUser().getRole().name());
        voterDto.setAssignedElections(voter.getElections().size());
        return voterDto;
    }
}
