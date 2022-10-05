package summer.rest.worldcupfantasy.controllers;


import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.assemblers.UserAssembler;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.League;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.repos.UserRepo;
import summer.rest.worldcupfantasy.services.UserService;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {
    UserRepo userRepo;
    UserService userService;
    UserAssembler userAssembler;


    public UserController(UserRepo userRepo, UserAssembler userAssembler,UserService userService) {
        this.userRepo = userRepo;
        this.userAssembler = userAssembler;
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<UserDTO>>>> getAllUsers() {
        return ApiResponse.ok(userAssembler.toCollectionModel(this.userService.usersToUsersDTO(this.userRepo.findAll())));
    }

    @PostMapping("/user/sign-up")
    public ResponseEntity<ApiResponse<EntityModel<UserDTO>>> signUp(@RequestParam String nickname) throws ApiErrorResponse {
        if (userRepo.findByNickname(nickname).isPresent()) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                    "The given nickname is already taken by another user. please try again with other nickname");
        }

        User user = new User(nickname);
        return ApiResponse.respond(true,HttpStatus.CREATED, userAssembler.toModel(new UserDTO(userRepo.save(user), 0)));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<EntityModel<UserDTO>>> getUser(@PathVariable Long id) throws ApiErrorResponse {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(userAssembler.toModel(this.userService.usersToUsersDTO(Collections.singletonList(user)).get(0)));
    }

    @SneakyThrows
    @GetMapping("/user/leagues/{id}")
    public ResponseEntity<ApiResponse<List<League>>> getUserLeagues(@PathVariable Long id) {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(user.getLeagues());
    }

    @SneakyThrows
    @GetMapping("/user/gambles/{id}")
    public ResponseEntity<ApiResponse<List<Gamble>>> getUserGambling(@PathVariable Long id) {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(user.getGambles());
    }


}
