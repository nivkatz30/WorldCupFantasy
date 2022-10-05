package summer.rest.worldcupfantasy.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.dto.GambleDTO;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.models.GambleRequest;
import summer.rest.worldcupfantasy.repos.GambleRepo;
import summer.rest.worldcupfantasy.repos.GameRepo;
import summer.rest.worldcupfantasy.repos.UserRepo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class GambleController {

    private final GambleRepo gambleRepo;
    private final GameRepo gameRepo;
    private final UserRepo userRepo;

    public GambleController(GambleRepo gambleRepo, GameRepo gameRepo, UserRepo userRepo) {
        this.gambleRepo = gambleRepo;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("gamble/game/{gameId}")
    public ResponseEntity<ApiResponse<List<GambleDTO>>> getAllGamblesPerGame(@PathVariable Long gameId) throws ApiErrorResponse {
        Game currentGame = this.gameRepo.findOrThrowById(gameId);
        List<Gamble> gambles = this.gambleRepo.findByGame(currentGame);

        List<GambleDTO> responseData = gambles.stream().map(GambleDTO::new).collect(Collectors.toList());
        return ApiResponse.ok(responseData);
    }

    @PutMapping("gamble/putGamble")
    public ResponseEntity<ApiResponse<GambleDTO>> updateGamble(@RequestBody GambleRequest request) throws ApiErrorResponse {
        Game game = gameRepo.findOrThrowById(request.getGameId());

        if (gameRepo.getNextMatchDay().stream().noneMatch(g -> Objects.equals(g.getGameId(), game.getGameId()))) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "You cannot gamble on this game");
        }

        User user = userRepo.getOrThrowById(request.getUserId());
        Gamble gamble = gambleRepo.findByUserAndGame(user,game).orElse(new Gamble(user,game));

        gamble.setResult(request.getHomeScore(),request.getAwayScore());
        gambleRepo.save(gamble);

        return ApiResponse.ok(new GambleDTO(gamble));
    }



}

