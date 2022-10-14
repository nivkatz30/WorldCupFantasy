package summer.rest.worldcupfantasy.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.assemblers.GambleAssembler;
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
import java.util.stream.Collectors;

@RestController
public class GambleController {

    private final GambleRepo gambleRepo;
    private final GameRepo gameRepo;
    private final UserRepo userRepo;

    private final GambleAssembler gambleAssembler;

    public GambleController(GambleRepo gambleRepo, GameRepo gameRepo, UserRepo userRepo, GambleAssembler gambleAssembler) {
        this.gambleRepo = gambleRepo;
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.gambleAssembler = gambleAssembler;
    }

    @GetMapping("gambles/{id}")
    public ResponseEntity<ApiResponse<EntityModel<GambleDTO>>> getGambleById(@PathVariable Long gambleId) throws ApiErrorResponse{
        Gamble gamble = this.gambleRepo.findById(gambleId).orElseThrow(() -> new ApiErrorResponse(HttpStatus.NOT_FOUND, "There is no gamble"));
        return ApiResponse.ok(this.gambleAssembler.toModel(new GambleDTO(gamble)));
    }

    @GetMapping("gambles")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<GambleDTO>>>> getAllGambles(){
        return ApiResponse.ok(this.gambleAssembler.toCollectionModel(this.gambleRepo.findAll().stream().map(gamble -> new GambleDTO(gamble)).collect(Collectors.toList())));
    }

    @GetMapping("gambles/game/{gameId}")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<GambleDTO>>>> getAllGamblesPerGame(@PathVariable Long gameId) throws ApiErrorResponse {
        Game currentGame = this.gameRepo.findOrThrowById(gameId);
        List<Gamble> gambles = this.gambleRepo.findByGame(currentGame);
        List<GambleDTO> responseData = gambles.stream().map(GambleDTO::new).collect(Collectors.toList());
        return ApiResponse.ok(this.gambleAssembler.toCollectionModel(responseData));
    }

    @PutMapping("gambles/putGamble")
    public ResponseEntity<ApiResponse<GambleDTO>> updateGamble(@RequestBody GambleRequest request) throws ApiErrorResponse {
        Game game = gameRepo.findOrThrowById(request.getGameId());

        if (game.getGameResult() != null || gameRepo.getNextMatchDay().stream().noneMatch(g -> Objects.equals(g.getGameId(), request.getGameId()))) {
            throw new ApiErrorResponse(HttpStatus.BAD_REQUEST, "You cannot gamble on this game");
        }

        User user = userRepo.getOrThrowById(request.getUserId());
        Gamble gamble = gambleRepo.findByUserAndGame(user,game).orElse(new Gamble(user,game));

        gamble.setResult(request.getHomeScore(),request.getAwayScore());
        gambleRepo.save(gamble);

        return ApiResponse.ok(new GambleDTO(gamble));
    }
}

