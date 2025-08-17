package com.sprints.online_voting_system.exception;

public class UnauthorizedVoterException extends RuntimeException {
    public UnauthorizedVoterException(String message) {
        super(message);
    }
}
