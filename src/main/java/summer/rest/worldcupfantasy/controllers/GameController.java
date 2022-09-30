package summer.rest.worldcupfantasy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    GameRepo gameRepo;
    GameResultRepo gameResultRepo;


    public GameController(GameRepo gameRepo, GameResultRepo gameResultRepo) {
        this.gameRepo = gameRepo;
        this.gameResultRepo = gameResultRepo;
    }

    @GetMapping("/game")
    public ResponseEntity<ApiResponse<List<Game>>> getGames(@RequestParam(defaultValue = "2020-01-01 06:00:00") String fromDate
            , @RequestParam(defaultValue = "2024-01-01 06:00:00") String toDate) {

        return ApiResponse.ok(this.gameRepo.findAll(fromDate,toDate));
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<ApiResponse<Game>> getGameById(@PathVariable Long id) throws ApiErrorResponse {
        return ApiResponse.ok(this.gameRepo.findOrThrowById(id));
    }

    @GetMapping("/game/nextGameDay")
    public ResponseEntity<ApiResponse<List<Game>>> getNextGameDay() {
        return ApiResponse.ok(this.gameRepo.getNextMatchDay());
    }

    @PutMapping("/game/updateResult")
    public ResponseEntity<ApiResponse<Game>> updateGameResult(@RequestBody GameResultRequest gameResultRequest) throws ApiErrorResponse {
        Game updatedGame = this.gameRepo.findOrThrowById(gameResultRequest.getGameId());
        GameResult gameResult = new GameResult(updatedGame, gameResultRequest.getHomeScore(), gameResultRequest.getAwayScore());

        updatedGame.setGameResult(gameResult);

        this.gameResultRepo.save(gameResult);
        this.gameRepo.save(updatedGame);
        return ApiResponse.ok(updatedGame);
    }
}
