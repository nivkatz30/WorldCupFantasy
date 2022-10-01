package summer.rest.worldcupfantasy.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import summer.rest.worldcupfantasy.entities.*;
import summer.rest.worldcupfantasy.repos.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Configuration
public class Config {
    GameRepo gameRepo;
    UserRepo userRepo;
    GambleRepo gambleRepo;
    LeagueRepo leagueRepo;
    GameResultRepo gameResultRepo;

    public Config(GameRepo gameRepo, UserRepo userRepo, GambleRepo gambleRepo, LeagueRepo leagueRepo, GameResultRepo gameResultRepo) {
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.gambleRepo = gambleRepo;
        this.leagueRepo = leagueRepo;
        this.gameResultRepo = gameResultRepo;
    }

    @Bean
    public void SeedDB() {
        try {
            SeedGames();
            SeedAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private void SeedAll() {
        Game game = this.gameRepo.findAll().get(0);
        User user = new User("niv katz");
        userRepo.save(user);
        User user2 = new User("david");
        userRepo.save(user2);
        League league = new League("First League");
        league.addNewUser(user);
        leagueRepo.save(league);
        Gamble gamble = new Gamble(4,2,user, game);
        gambleRepo.save(gamble);
    }

    private LocalDateTime parseApiDate(String dateAsString) {
        try {
            return LocalDateTime.parse(dateAsString,DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));
        } catch (Exception e) {
            return LocalDateTime.parse(dateAsString,DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
        }
    }

    private void SeedGames() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MzM2ZTQ4M2RhYTlhZmYzZTc0NjNlMDMiLCJpYXQiOjE2NjQ1NDE4MjgsImV4cCI6MTY2NDYyODIyOH0.-WMhGd4Gg-d6_qB7F9FIUi-Wg3jkGXDpw3HWbKHn3cw");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> response = restTemplate.exchange("http://api.cup2022.ir/api/v1/match", HttpMethod.GET,entity, String.class);
        HashMap result = new ObjectMapper().readValue(response.getBody(), HashMap.class);
        List<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)(result.get("data"));

        data.stream().forEach(game -> {
            Game newGame = new Game(Integer.parseInt(game.get("matchday").toString()),game.get("home_team_en").toString()
                    ,game.get("away_team_en").toString(),this.parseApiDate(game.get("local_date").toString()));
            gameRepo.save(newGame);
        });
    }
}
