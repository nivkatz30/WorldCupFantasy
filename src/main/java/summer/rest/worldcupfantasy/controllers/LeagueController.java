package summer.rest.worldcupfantasy.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.dto.LeagueDTO;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.entities.League;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.repos.LeagueRepo;
import summer.rest.worldcupfantasy.repos.UserRepo;
import summer.rest.worldcupfantasy.services.UserService;


import java.util.List;
import java.util.Objects;

@RestController
public class LeagueController {
    LeagueRepo leagueRepo;
    UserRepo userRepo;
    UserService userService;

    public LeagueController(LeagueRepo leagueRepo, UserRepo userRepo,UserService userService) {
        this.leagueRepo = leagueRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/league")
    public ResponseEntity<ApiResponse<List<League>>> getAllLeagues() {
        return ApiResponse.ok(this.leagueRepo.findAll());
    }

    @GetMapping("/leagues/{id}")
    public ResponseEntity<ApiResponse<LeagueDTO>> getSingleLeague(@PathVariable Long id) throws ApiErrorResponse {
        League league = leagueRepo.getOrThrowById(id);
        List<UserDTO> leagueUsers = this.userService.usersToUsersDTO(league.getUsers());
        return ApiResponse.ok(new LeagueDTO(league,leagueUsers));
    }

    @GetMapping("/league/name/{name}")
    public ResponseEntity<ApiResponse<LeagueDTO>> getLeagueByName(@PathVariable String name) throws ApiErrorResponse {
        League league = leagueRepo.findByName(name).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "League with given name is not found"));
        List<UserDTO> leagueUsers = this.userService.usersToUsersDTO(league.getUsers());
        return ApiResponse.ok(new LeagueDTO(league,leagueUsers));
    }

    @PostMapping("/leagues/addUser")
    public ResponseEntity<ApiResponse<Object>> addUserToLeague(@RequestParam Long leagueId, @RequestParam Long userId) throws ApiErrorResponse {
        User user = this.userRepo.getOrThrowById(userId);
        League league = this.leagueRepo.getOrThrowById(leagueId);

        if (league.getUsers().stream().anyMatch(u -> Objects.equals(u.getUserId(), userId))) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "User is already exist in this league");
        }

        league.addNewUser(user);
        leagueRepo.save(league);
        return ApiResponse.respond(true,HttpStatus.OK, "User is successfully added to this league");
    }

    @PostMapping("/league")
    public ResponseEntity<ApiResponse<League>> addNewLeague(@RequestParam String leagueName) throws ApiErrorResponse {
        if (leagueRepo.findByName(leagueName).isPresent()) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Given league name is already taken. please try again with another name");
        }

        League league = new League(leagueName);
        leagueRepo.save(league);

        return ApiResponse.ok(league);
    }

    @DeleteMapping("/league/removeUser")
    public ResponseEntity<ApiResponse<Object>> removeUserFromLeague(@RequestParam Long leagueId, @RequestParam Long userId) throws ApiErrorResponse {
        League league = leagueRepo.getOrThrowById(leagueId);
        league.removeUser(userId);

        leagueRepo.save(league);
        return ApiResponse.respond(true,HttpStatus.OK,"User remove successfully from the league");
    }


}
