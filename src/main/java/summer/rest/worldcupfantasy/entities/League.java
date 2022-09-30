package summer.rest.worldcupfantasy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long leagueId;

    String name;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
             name = "users_leagues",
             joinColumns = @JoinColumn(name = "leagues_id"),
             inverseJoinColumns = @JoinColumn(name = "user_id"),
             uniqueConstraints = {
                     @UniqueConstraint(
                             columnNames = {"user_id", "leagues_id"}
                     )
            }

     )
    List<User> users = new ArrayList<>();

    public League(String name) {
        this.name = name;
    }

    public void addNewUser(User user) {
        users.add(user);
    }

    public void removeUser(Long userId) {
        this.users = this.users.stream().filter(user -> !Objects.equals(user.getUserId(), userId)).collect(Collectors.toList());
    }
}
