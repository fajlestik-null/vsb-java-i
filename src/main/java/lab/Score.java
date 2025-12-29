package lab;

import java.io.Serializable;

public record Score(String nickName, int scorePoints) implements Serializable{

    public String getNickName() {
        return nickName();
    }

    public int getScore() {
        return scorePoints();
    }
}
