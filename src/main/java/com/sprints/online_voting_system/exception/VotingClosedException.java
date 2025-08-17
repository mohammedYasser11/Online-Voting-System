package com.sprints.online_voting_system.exception;

public class VotingClosedException extends RuntimeException {
    public VotingClosedException(String message) {
        super(message);
    }
}
