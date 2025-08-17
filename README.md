## Online Voting System
A secure, modular, and testable RESTful API built with Spring Boot for managing and conducting electronic elections.

## Features
- User authentication (Admin and Voters) with JWT
- Election management with time-based restrictions
- Candidate registration
- Voter assignment by city
- Secure vote casting (one vote per voter per election)
- Real-time result calculation
- Role-based access control

## User Types:
- **Admin**:  Manages elections, registers candidates, assigns voters, and monitors results.
- **Voters**: Authenticated users who can cast one secure vote per election session

## Technology Stack
- **Backend**: Spring Boot 3.x
- **Database**: H2
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Bean Validation (Jakarta)
- **API**: RESTful Web Services
- **Build Tool**: Maven
- **Java Version**: 17+

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

## Getting Started
- Clone the repository
``` bash
git clone https://github.com/mohammedYasser11/Online-Voting-System.git
cd Online-Voting-System
```
- Build the project
``` bash
mvn clean install
```
- Run the application
``` bash
mvn spring-boot:run
```

## Authentication
All API endpoints (except registration and login) require JWT authentication.

## Project Structure

```
src/main/java/com/bankapp/
├── model/
│   ├── Candidate.java
│   ├── Election.java
│   ├── User.java
│   ├── Role.java (enum)
│   ├── Vote.java
│   └── Voter.java
│
├── repository/
│   ├── CandidateRepository.java
│   ├── ElectionRepository.java
│   ├── UserRepository.java
│   ├── VoteRepository.java
│   └── VoterRepository.java
│
├── service/
│   ├── AuthService.java
│   ├── CandidateService.java
│   ├── ElectionService.java
│   ├── VoteService.java
│   └── VoterService.java
│
├── mappers/
│   ├── CandidateMapper.java
│   ├── ElectionMapper.java
│   ├── VoterMapper.java
│   └── UserMapper.java
│
├── controller/
│   ├── AuthController.java
│   ├── CandidateController.java
│   ├── ElectionController.java
│   ├── PublicElectionController.java
│   ├── VoteController.java
│   └── VoterController.java
│
├── dto/
│   ├── AuthResponseDTO.java
│   ├── CandidateDTO.java
│   ├── ElectionDTO.java
│   ├── ElectionResponseDTO.java
│   ├── ElectionResultDTO.java
│   ├── LoginRequestDTO.java
│   ├── RegisterUserDTO.java
│   ├── VoteDTO.java
│   ├── VoterDTO.java
│   └── VoteStatusDTO.java
│   
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── DoubleVotingException.java
│   ├── EmailAlreadyExistsException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedVoterException.java
│   └── VotingClosedException.java
│
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtUtil.java
│   ├── SecurityConfig.java
│   ├── CustomUserDetailsService.java
│   └── TokenBlacklistService.java
│
└── BankaccountssytemApplication.java
```

### Auth Endpoints
- **POST `/api/auth/register`**: register voter ![Endpoint Example](docs/images/Auth/register_user.PNG)
- **POST `/api/auth/register`**: register admin ![Endpoint Example](docs/images/Auth/register_admin.PNG)
- **POST `/api/auth/login`**: login voter/admin ![Endpoint Example](docs/images/Auth/login_user.PNG)
- **POST `/api/auth/logout`**: logout voter/admin ![Endpoint Example](docs/images/Auth/logout_user.PNG)

### Election Endpoints
- **POST `/api/admin/elections`**: Create a new election ![Endpoint Example](docs/images/Election/create_election.PNG)
- **GET `/api/admin/elections`**: Get all elections ![Endpoint Example](docs/images/Election/get_all_elections.PNG)
- **GET `/api/admin/elections/active`**: Get active elections ![Endpoint Example](docs/images/Election/get_active_elections.PNG)
- **GET `/api/admin/elections/upcoming`**: Get upcoming elections ![Endpoint Example](docs/images/Election/get_upcoming_elections.PNG)
- **GET `/api/admin/elections/{electionId}`**: Get election by ID ![Endpoint Example](docs/images/Election/get_election_by_ID.PNG)
- **GET `/api/admin/elections/{electionId}/results`**: Get election results by ID ![Endpoint Example](docs/images/Election/get_election_results_by_ID_after_vote.PNG)
- **DELETE `/api/admin/elections/{electionId}`**: Delete election by ID ![Endpoint Example](docs/images/Election/delete_election_by_ID.PNG)

### PublicElection Endpoints
- **GET `/api/elections/my-elections`**: Get user's elections ![Endpoint Example](docs/images/PublicElection/get_user's_elections.PNG)
- **GET `/api/elections/my-elections`**: Get user's active elections ![Endpoint Example](docs/images/PublicElection/get_user's_active_elections.PNG)

### Candidate Endpoints
- **POST `/api/admin/candidates`**: Create a candidate for an election ![Endpoint Example](docs/images/Candidate/create_candidate.PNG)
- **GET `/api/elections/{electionId}/candidates`**: Get candidates for an election ![Endpoint Example](docs/images/Candidate/get_candidates_by_election.PNG)

### Vote Endpoints
- **POST `/api/votes`**: Cast a vote by voter in an election ![Endpoint Example](docs/images/Vote/cast_vote_to_election.PNG)
- **GET `/api/votes/status/{electionId}`**: Get vote status of a voter in an election ![Endpoint Example](docs/images/Vote/get_vote_status.PNG)

### Voter Endpoints
- **POST `/api/admin/voters/{voterId}/elections/{electionId}`**: Assign a voter to an election ![Endpoint Example](docs/images/Voter/assign_voter_to_election.PNG)
- **DELETE `/api/admin/voters/{voterId}/elections/{electionId}`**: Delete a voter from an election ![Endpoint Example](docs/images/Voter/remove_voter_from_election.PNG)
- **POST `/api/admin/voters/elections/{electionId}/assign-by-city/{city}`**: Assign voters to an election by city ![Endpoint Example](docs/images/Voter/assign_voters_to_election_by_city.PNG)
- **GET `/api/admin/voters`**: Get all voters ![Endpoint Example](docs/images/Voter/get_all_voters.PNG)
- **GET `/api/admin/voters/city/{city}`**: Get voters by city ![Endpoint Example](docs/images/Voter/get_voters_by_city.PNG)
- **GET `/api/admin/voters/elections/{electionId}`**: Get voters assigned to an election ![Endpoint Example](docs/images/Voter/get_voters_assigned_to_election.PNG)

## Validation Rules
- `Role`: enum in user model with values ['ADMIN', 'VOTER']
- Most fields should be not blank 
- An election start time must be in the future
- An election end time must be after the start time
- A voter must vote only once per election

Invalid input results in HTTP 400 with descriptive error messages.

## Exception Handling
- **Validation errors** return `400 BAD REQUEST` with detailed error messages.
- **Resource not found** returns `404 NOT FOUND`.
- **Double vote in an election** returns `409 CONFLICT`.
- All exceptions are handled by a global `@ControllerAdvice`.
- Screenshots of handled exceptions are attached in docs/images/exceptionHandling folder
