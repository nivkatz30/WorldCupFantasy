package summer.rest.worldcupfantasy.controllers;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.assemblers.LeagueAssembler;
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
import java.util.stream.Collectors;

@RestController
public class LeagueController {
    private final LeagueRepo leagueRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    private final LeagueAssembler leagueAssembler;

    public LeagueController(LeagueRepo leagueRepo, UserRepo userRepo, UserService userService, LeagueAssembler leagueAssembler) {
        this.leagueRepo = leagueRepo;
        this.userRepo = userRepo;
        this.userService = userService;
        this.leagueAssembler = leagueAssembler;
    }

    @GetMapping("/leagues")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<LeagueDTO>>>> getAllLeagues() {
        List<League> leagues = this.leagueRepo.findAll();
        List<LeagueDTO> dtoLeagues = leagues.stream().map(league -> new LeagueDTO(league,this.userService.usersToUsersDTO(league.getUsers()))).collect(Collectors.toList());
        return ApiResponse.ok(this.leagueAssembler.toCollectionModel(dtoLeagues));
    }

    @GetMapping("/leagues/{id}")
    public ResponseEntity<ApiResponse<EntityModel<LeagueDTO>>> getSingleLeague(@PathVariable Long id) throws ApiErrorResponse {
        League league = leagueRepo.getOrThrowById(id);
        List<UserDTO> leagueUsers = this.userService.usersToUsersDTO(league.getUsers());
        return ApiResponse.ok(this.leagueAssembler.toModel(new LeagueDTO(league,leagueUsers)));
    }

    @GetMapping("/leagues/name/{name}")
    public ResponseEntity<ApiResponse<EntityModel<LeagueDTO>>> getLeagueByName(@PathVariable String name) throws ApiErrorResponse {
        League league = leagueRepo.findByName(name).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "League with given name is not found"));
        List<UserDTO> leagueUsers = this.userService.usersToUsersDTO(league.getUsers());
        return ApiResponse.ok(this.leagueAssembler.toModel(new LeagueDTO(league,leagueUsers)));
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

    @PostMapping("/leagues/addNewLeague")
    public ResponseEntity<ApiResponse<League>> addNewLeague(@RequestParam String leagueName) throws ApiErrorResponse {
        if (leagueRepo.findByName(leagueName).isPresent()) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Given league name is already taken. please try again with another name");
        }

        League league = new League(leagueName);
        leagueRepo.save(league);

        return ApiResponse.ok(league);
    }

    @DeleteMapping("/leagues/removeUser")
    public ResponseEntity<ApiResponse<Object>> removeUserFromLeague(@RequestParam Long leagueId, @RequestParam Long userId) throws ApiErrorResponse {
        League league = leagueRepo.getOrThrowById(leagueId);
        league.removeUser(userId);

        leagueRepo.save(league);
        return ApiResponse.respond(true,HttpStatus.OK,"User remove successfully from the league");
    }


}
