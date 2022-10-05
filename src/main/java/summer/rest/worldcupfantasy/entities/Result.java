package summer.rest.worldcupfantasy.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Result {

    Integer homeScore;
    Integer awayScore;
    String result;

    public Result(Integer homeScore, Integer awayScore) {
        this.setResult(homeScore, awayScore);
    }

    private void setResult() {
        this.result = homeScore > awayScore ? "1" : awayScore > homeScore ? "2" : "X";
    }

    public void setResult(Integer homeScore, Integer awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        setResult();
    }

}