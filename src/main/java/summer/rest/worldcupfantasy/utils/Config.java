package summer.rest.worldcupfantasy.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import summer.rest.worldcupfantasy.entities.*;
import summer.rest.worldcupfantasy.interceptors.AdminInterceptor;
import summer.rest.worldcupfantasy.interceptors.JwtInterceptor;
import summer.rest.worldcupfantasy.models.UserRole;
import summer.rest.worldcupfantasy.repos.*;
import summer.rest.worldcupfantasy.services.TokenService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Configuration
public class Config implements WebMvcConfigurer {

     private final GameRepo gameRepo;
     private final UserRepo userRepo;
     private final TokenService tokenService;

    public Config(GameRepo gameRepo, UserRepo userRepo, TokenService tokenService) {
        this.gameRepo = gameRepo;
        this.userRepo = userRepo;
        this.tokenService = tokenService;
    }

    @Bean
    public void SeedDB() {
        try {
            runAsync();
            SeedUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void SeedUsers() {
        PasswordEncoder encoder = encoder();
        User user = new User("niv katz", encoder.encode("nivkatz30"), UserRole.ADMIN);
        User user2 = new User("david rimon", encoder.encode("davidrimon241194"), UserRole.ADMIN);
        userRepo.save(user);
        userRepo.save(user2);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private LocalDateTime parseApiDate(String dateAsString) {
        return LocalDateTime.parse(dateAsString,DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm"));
    }

    /**
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new JwtInterceptor(this.tokenService)).excludePathPatterns("/user/sign-up", "/user/sign-in", "/swagger-ui/**", "/api.html", "/v3/**").pathMatcher(new AntPathMatcher());
        //registry.addInterceptor(new AdminInterceptor(this.tokenService, this.userRepo)).addPathPatterns("/game/updateResult");
    }

    @Async
    void runAsync() {
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MzQ5MTc3YmRhYTlhZmYzZTc1YWE4MTEiLCJpYXQiOjE2NjU3MzQ1MjQsImV4cCI6MTY2NTgyMDkyNH0.3ogIsf8zdrZz4jXfkIllZkx3vbDtl8NsQfMKMBmf5DY";
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
