package lab;
//RuntimeException - donuté mě použít try..catch block
public class ScoreException extends Exception {
    public ScoreException(String message) {
        super(message);
    }
    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
