package summer.rest.worldcupfantasy.controllers;


import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.assemblers.UserAssembler;
import summer.rest.worldcupfantasy.dto.UserSignUpDTO;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.League;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.models.SignUpRequest;
import summer.rest.worldcupfantasy.models.UserRole;
import summer.rest.worldcupfantasy.repos.UserRepo;
import summer.rest.worldcupfantasy.services.TokenService;
import summer.rest.worldcupfantasy.services.UserService;

import java.util.Collections;
import java.util.List;

/**
 * This class manage all the related endpoints of Users.
 */
@RestController
public class UserController {
    private final UserRepo userRepo;
    private final UserService userService;
    private final UserAssembler userAssembler;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public UserController(UserRepo userRepo, UserAssembler userAssembler, UserService userService, PasswordEncoder encoder, TokenService tokenService) {
        this.userRepo = userRepo;
        this.userAssembler = userAssembler;
        this.userService = userService;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    /**
     * This method find all the existing users.
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<UserDTO>>>> getAllUsers() {
        return ApiResponse.ok(userAssembler.toCollectionModel(this.userService.usersToUsersDTO(this.userRepo.findAll())));
    }

    /**
     * This method create a new user.
     * @param request
     * @return
     * @throws ApiErrorResponse
     */
    @PostMapping("/users/sign-up")
    public ResponseEntity<ApiResponse<EntityModel<UserDTO>>> signUp(@RequestBody SignUpRequest request) throws ApiErrorResponse {
        if (userRepo.findByNickname(request.getNickname()).isPresent()) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                    "The given nickname is already taken by another user. please try again with other nickname");
        }

        User user = new User(request.getNickname(), encoder.encode(request.getPassword()), UserRole.USER);
        return ApiResponse.respond(true,HttpStatus.CREATED, userAssembler.toModel(new UserSignUpDTO(userRepo.save(user),
                0, tokenService.generateJwtToken(user))));
    }

    /**
     * This method lets the user log in.
     * @param request
     * @return
     * @throws ApiErrorResponse
     */
    @PostMapping("/users/sign-in")
    public ResponseEntity<ApiResponse<EntityModel<UserDTO>>> signIn(@RequestBody SignUpRequest request) throws ApiErrorResponse {
        User user = userRepo.findByNickname(request.getNickname())
                .orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "User with given nickname is not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Password is incorrect");
        }


        Integer userScore = this.userService.usersToUsersDTO(Collections.singletonList(user)).get(0).getScore();
        return ApiResponse.ok(userAssembler.toModel(new UserSignUpDTO(user, userScore, tokenService.generateJwtToken(user))));
    }

    /**
     * This method find a specific user by his ID.
     * @param id
     * @return
     * @throws ApiErrorResponse
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<EntityModel<UserDTO>>> getUser(@PathVariable Long id) throws ApiErrorResponse {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(userAssembler.toModel(this.userService.usersToUsersDTO(Collections.singletonList(user)).get(0)));
    }

    /**
     * This method find all the related league of a specific user.
     * @param id
     * @return
     */
    @SneakyThrows
    @GetMapping("/users/leagues/{id}")
    public ResponseEntity<ApiResponse<List<League>>> getUserLeagues(@PathVariable Long id) {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(user.getLeagues());
    }

    /**
     * this method find all the related gambles od a specific user.
     * @param id
     * @return
     */
    @SneakyThrows
    @GetMapping("/users/gambles/{id}")
    public ResponseEntity<ApiResponse<List<Gamble>>> getUserGambling(@PathVariable Long id) {
        User user = this.userRepo.getOrThrowById(id);
        return ApiResponse.ok(user.getGambles());
    }
}
