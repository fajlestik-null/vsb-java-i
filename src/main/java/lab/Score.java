package lab;

public class Score {
    private String nickName;
    private int score;
    public Score(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }
    public int getScore() {
        return score;
    }
}
