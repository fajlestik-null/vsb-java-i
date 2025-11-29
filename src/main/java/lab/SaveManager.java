package lab;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {
    public static void saveGame(GameScene scene, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(scene);
            System.out.println("Game was successfully saved under: " + filename);

        } catch (IOException e) {
            System.err.println("Error while saving your game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static GameScene loadGame(String filename) {
        GameScene loadedScene = null;

        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            loadedScene = (GameScene) in.readObject();
            System.out.println("Game was successfully loaded, prepare for action!");

        } catch (FileNotFoundException e) {
            System.err.println("No game to load/file under " + filename + " was found.");
        } catch (IOException e) {
            System.err.println("Error while reading: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }

        return loadedScene;
    }
}
