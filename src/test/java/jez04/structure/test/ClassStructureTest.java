package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.ClassExist;
import cz.vsb.fei.kelvin.unittest.ContainsInnerClasses;
import cz.vsb.fei.kelvin.unittest.HasMethod;
import cz.vsb.fei.kelvin.unittest.HasProperty;
import cz.vsb.fei.kelvin.unittest.SrcContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testGameControllerExists() {
        assertThat(ClassStructureTest.class, new ClassExist("GameController"));
    }

    @Test
    void testFxControllerInFxmlExists() {
        assertThat(Paths.get(""), new TextFileContains(".*\\.fxml", "fx:controller=\"lab\\.GameController\"").useRegExpForName(true));
    }

    @Test
    void testCanvasInFxmlExists() {
        assertThat(Paths.get(""), new TextFileContains(".*\\.fxml", "<Canvas").useRegExpForName(true));
    }

    @Test
    void testButtonInFxmlExists() {
        assertThat(Paths.get(""), new TextFileContains(".*\\.fxml", "<Button").useRegExpForName(true));
    }

    @Test
    void testGameControllerFxmlLoader() throws ClassNotFoundException {
        Class<?> app = helper.getClass("App");
        assertThat(app, new SrcContains("FXMLLoader"));
        assertThat(app, new SrcContains(".fxml"));
    }

    @Test
    void testGameControllerProperty2() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasProperty(".*", Canvas.class, false));
    }

    @Test
    void testGameControllerProperty3() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasProperty(".*", Slider.class, false));
    }

    @Test
    void testGameControllerProperty4() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasProperty(".*", Slider.class, false).count(2));
    }

    @Test
    void testGameControllerProperty5() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        Class<?> gameScene = helper.getClass("GameScene");
        Class<?> drawingThread = helper.getClass("DrawingThread");
        assertThat(gameController, new HasProperty(".*", gameScene));
        assertThat(gameController, new HasProperty(".*", drawingThread));
    }

    @Test
    void testGameControllerMethod() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasMethod(".*", void.class, ActionEvent.class).useRegExp(true));
    }

    @Test
    void testGameControllerMethod2() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new HasMethod("stop", void.class));
    }

    @Test
    void testGameControllerSliderChange() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new ContainsInnerClasses(1));
    }

    @Test
    void testGameControllerSliderChange2() throws ClassNotFoundException {
        Class<?> gameController = helper.getClass("GameController");
        assertThat(gameController, new ContainsInnerClasses(2));
    }

    @Test
    void testBoatMethod() throws ClassNotFoundException {
        Class<?> boat = helper.getClass("Boat");
        assertThat(boat, new HasMethod("setPos.*", void.class, double.class).useRegExp(true));
    }
    @Test
    void testRockMethod() throws ClassNotFoundException {
        Class<?> rock = helper.getClass("Rock");
        assertThat(rock, new HasMethod("changePosition", void.class));
    }
    @Test
    void testGameSceneMethod() throws ClassNotFoundException {
        Class<?> gameScene = helper.getClass("GameScene");
        assertThat(gameScene, new HasMethod("moveRocks", void.class));
    }
    @Test
    void testBackgroundMethod() throws ClassNotFoundException {
        Class<?> background = helper.getClass("Background");
        assertThat(background, new HasMethod("setSpeed", void.class, double.class));
    }


}
