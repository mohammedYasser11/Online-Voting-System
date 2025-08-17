package com.sprints.online_voting_system.exception;

public class DoubleVotingException extends RuntimeException {
    public DoubleVotingException(String message) {
        super(message);
    }
}
