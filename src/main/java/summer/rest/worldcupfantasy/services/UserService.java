package summer.rest.worldcupfantasy.services;


import org.springframework.stereotype.Service;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.interfaces.Score;
import summer.rest.worldcupfantasy.repos.UserRepo;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService  {
    UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserDTO> usersToUsersDTO(List<User> users) {
        List<Score> usersScores = this.userRepo.getUsersScore(users.stream().map(User::getUserId).collect(Collectors.toList()));
        HashMap<Long,Integer> userToScore = new HashMap<>();


        usersScores.forEach(score -> {
            Integer extraScore = 3;
            if (score.getGambleHomeScore() == score.getResultHomeScore() && score.getGambleAwayScore() == score.getResultAwayScore()) {
                extraScore += 3;
            }
            userToScore.put((score.getUserId()), userToScore.getOrDefault(score.getUserId(), 0) + extraScore);
        });

        return users.stream()
                .map(user -> new UserDTO(user,userToScore.getOrDefault(user.getUserId(),0)))
                .sorted((a,b) -> b.getScore() - a.getScore())
                .collect(Collectors.toList());
    }
}
