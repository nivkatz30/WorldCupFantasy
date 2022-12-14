package summer.rest.worldcupfantasy.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
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

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class has been scanned before the application runs and define the necessaries.
 */
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

    /**
     * This method runs before the application running, and initialize the data in the DB.
     */
    @Bean
    public void SeedDB() {
        try {
            runAsync();
            SeedUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method initialize the games in the DB as a result the api connection was failed.
     * @return
     */
    private String getGamesFromBackup() {
        try {
            File myObj = new File("src/main/resources/static/Games.json");
            Scanner myReader = new Scanner(myObj);
            StringBuilder json = new StringBuilder();
            while (myReader.hasNextLine()) {
                json.append(myReader.nextLine());
            }

            myReader.close();
            return json.toString();

        } catch (Exception e) {
            System.out.println("An error occurred.");
            return null;
        }
    }

    /**
     * This class defines the firsts users in the DB.
     */
    private void SeedUsers() {
        PasswordEncoder encoder = encoder();
        User user = new User("niv katz", encoder.encode("nivkatz30"), UserRole.ADMIN);
        User user2 = new User("david rimon", encoder.encode("davidrimon241194"), UserRole.ADMIN);
        userRepo.save(user);
        userRepo.save(user2);
    }

    /**
     * This method encode the user password.
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method convert the date to the necessary format.
     * @param dateAsString
     * @return
     */
    private LocalDateTime parseApiDate(String dateAsString) {
        return LocalDateTime.parse(dateAsString,DateTimeFormatter.ofPattern("MM/d/yyyy HH:mm"));
    }

    /**
     * This method define the URI that needed an authorization.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(this.tokenService))
                .excludePathPatterns("/users/sign-up", "/users/sign-in", "/swagger-ui/**", "/api.html", "/v3/**", "/favicon.ico")
                .pathMatcher(new AntPathMatcher());
        registry.addInterceptor(new AdminInterceptor(this.tokenService, this.userRepo)).addPathPatterns("/games/updateResult");
    }

    /**
     * this method runs asynchronous for load the games from the Api.
     */
    @Async
    void runAsync() {
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MzRkMGZmNWRhYTlhZmYzZTc1YjdkMGIiLCJpYXQiOjE2NjU5OTQ3NDIsImV4cCI6MTY2NjA4MTE0Mn0.Yg22qdI8djhdrih1KHw-Zqa_tWOX-zH2j2tiptBhljM";
        CompletableFuture.supplyAsync(() -> this.callApi(token, true)).thenAccept(System.out::println);
    }

    /**
     * This method take the data about the games from the Api.
     * @param token
     * @param tryAgain
     * @return
     */
    public String callApi(String token, boolean tryAgain) {
        try {
            this.SeedGames(token);

        } catch (Exception e) {
            if (tryAgain) {
                String newToken = getToken();
                if (newToken != null) {
                    String t = "Bearer" + newToken.replace('"',' ');
                    System.out.println("fetch new token from the server...");
                    System.out.println("new token: " + t);
                    return this.callApi(("Bearer" + this.getToken()).replace('"',' '),false);
                } else {
                    this.insertGamesToDB(this.getGamesFromBackup());
                    return "Load games from backup successfully";
                }
            }
            return "Cannot read the games from the api";
        }

        return "Load the games from the api successfully";
    }

    /**
     * This method get the necessary token from the Api.
     * @return
     */
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

    /**
     * @param token
     * This method take the games from the Api.
     * @throws JsonProcessingException
     */
    private void SeedGames(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://api.cup2022.ir/api/v1/match", HttpMethod.GET,entity, String.class);
        insertGamesToDB(response.getBody());
    }

    /**
     * This method initialize the games we have got in the DB.
     * @param json
     */
    private void insertGamesToDB(String json) {
       try {
           JsonNode root = new ObjectMapper().readTree(json).path("data");

           for (int i = 0; i<48; i++) {
               Game newGame = new Game(Integer.parseInt(root.path(i).path("matchday").toString())
                       ,root.path(i).path("home_team_en").textValue()
                       ,root.path(i).path("away_team_en").textValue()
                       ,this.parseApiDate(root.path(i).path("local_date").textValue()));
               gameRepo.save(newGame);
           }
       } catch (Exception e) {}
    }
}
