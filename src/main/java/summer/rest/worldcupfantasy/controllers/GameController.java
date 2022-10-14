package summer.rest.worldcupfantasy.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.rest.worldcupfantasy.assemblers.GameAssembler;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.GameResult;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.models.GameResultRequest;
import summer.rest.worldcupfantasy.repos.GameRepo;
import summer.rest.worldcupfantasy.repos.GameResultRepo;

import java.util.List;

@RestController
public class GameController {
    private final GameRepo gameRepo;
    private final GameResultRepo gameResultRepo;

    private final GameAssembler gameAssembler;


    public GameController(GameRepo gameRepo, GameResultRepo gameResultRepo, GameAssembler gameAssembler) {
        this.gameRepo = gameRepo;
        this.gameResultRepo = gameResultRepo;
        this.gameAssembler = gameAssembler;
    }

    @GetMapping("/games")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<Game>>>> getAllGames(){
        return ApiResponse.ok(this.gameAssembler.toCollectionModel(this.gameRepo.findAll()));
    }

    @GetMapping("/gamesByRange")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<Game>>>> getGames(@RequestParam(defaultValue = "2020-01-01 06:00:00") String fromDate
            , @RequestParam(defaultValue = "2024-01-01 06:00:00") String toDate) {

        return ApiResponse.ok(this.gameAssembler.toCollectionModel(this.gameRepo.findAll(fromDate,toDate)));
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<ApiResponse<EntityModel<Game>>> getGameById(@PathVariable Long id) throws ApiErrorResponse {
        return ApiResponse.ok(this.gameAssembler.toModel(this.gameRepo.findOrThrowById(id)));
    }

    @GetMapping("/games/nextGameDay")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<Game>>>> getNextGameDay() {
        return ApiResponse.ok(this.gameAssembler.toCollectionModel(this.gameRepo.getNextMatchDay()));
    }

    @PutMapping("/games/updateResult")
    public ResponseEntity<ApiResponse<Game>> updateGameResult(@RequestBody GameResultRequest request) throws ApiErrorResponse {
        Game game = this.gameRepo.findOrThrowById(request.getGameId());
        GameResult gameResult = this.gameResultRepo.findByGame(game).orElse(new GameResult(game));

        gameResult.setResult(request.getHomeScore(),request.getAwayScore());
        game.setGameResult(gameResult);

        this.gameResultRepo.save(gameResult);
        this.gameRepo.save(game);
        return ApiResponse.ok(game);
    }
}
