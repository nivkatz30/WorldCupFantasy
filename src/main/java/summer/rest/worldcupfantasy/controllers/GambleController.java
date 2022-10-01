package summer.rest.worldcupfantasy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import summer.rest.worldcupfantasy.dto.GambleDTO;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.ApiResponse;
import summer.rest.worldcupfantasy.repos.GambleRepo;
import summer.rest.worldcupfantasy.repos.GameRepo;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GambleController {

    private GambleRepo gambleRepo;
    private GameRepo gameRepo;

    public GambleController(GambleRepo gambleRepo, GameRepo gameRepo) {
        this.gambleRepo = gambleRepo;
        this.gameRepo = gameRepo;
    }

    @GetMapping("gamble/game/{gameId}")
    public ResponseEntity<ApiResponse<List<GambleDTO>>> getAllGamblesPerGame(@PathVariable Long gameId) throws ApiErrorResponse {
        Game currentGame = this.gameRepo.findOrThrowById(gameId);
        List<Gamble> gambles = this.gambleRepo.findByGame(currentGame);
        List<GambleDTO> responseData = gambles.stream().map(gamble -> new GambleDTO(gamble)).collect(Collectors.toList());
        return ApiResponse.ok(responseData);
    }

}

