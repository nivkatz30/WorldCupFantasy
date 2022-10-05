package summer.rest.worldcupfantasy.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import summer.rest.worldcupfantasy.entities.*;
import summer.rest.worldcupfantasy.repos.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Configuration
public class Config  {
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
            runAsync();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private LocalDateTime parseApiDate(String dateAsString) {
        return LocalDateTime.parse(dateAsString,DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm"));
    }


    @Async
    private void runAsync() {
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MzNiZmY4N2RhYTlhZmYzZTc0ZGVmOTEiLCJpYXQiOjE2NjQ4NzY0MjQsImV4cCI6MTY2NDk2MjgyNH0.KVPU51YrtlXzTHRE7b6YTGI56mwZry9Qj4u8_JxHJZ0";
        CompletableFuture.supplyAsync(() -> this.callApi(token, true)).thenAccept(System.out::println);
    }

    public String callApi(String token, boolean tryAgain) {
        try {
            if (token != null) {
                this.SeedGames(token);
            }
        } catch (Exception e) {
            if (tryAgain) {
                String t = "Bearer" + this.getToken().replace('"',' ');
                System.out.println("fetch new token from the server...");
                System.out.println("new token: " + t);
                return this.callApi(("Bearer" + this.getToken()).replace('"',' '),false);
            }
            return "Bad";
        }

        return "Good";
    }

    private String getToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject json = new JSONObject();
            json.put("name", "a");
            json.put("email", String.format("%s@%s", UUID.randomUUID(), "israel.com"));
            json.put("password", "12345678");
            json.put("passwordConfirm", "12345678");

            String response = restTemplate.
                    postForObject("http://api.cup2022.ir/api/v1/user", new HttpEntity<>(json.toString(), headers), String.class);
            JsonNode root = new ObjectMapper().readTree(response);
            return root.path("data").path("token").toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void SeedGames(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> response = restTemplate.exchange("http://api.cup2022.ir/api/v1/match", HttpMethod.GET,entity, String.class);
        JsonNode root = new ObjectMapper().readTree(response.getBody()).path("data");

        for (int i = 0; i<48; i++) {
            Game newGame = new Game(Integer.parseInt(root.path(i).path("matchday").toString())
                    ,root.path(i).path("home_team_en").textValue()
                    ,root.path(i).path("away_team_en").textValue()
                    ,this.parseApiDate(root.path(i).path("local_date").textValue()));
            gameRepo.save(newGame);
        }
    }
}
