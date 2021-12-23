package Pepse;

import Pepse.world.Sky;
import Pepse.world.Terrain;
import Pepse.world.daynight.Night;
import Pepse.world.daynight.Sun;
import Pepse.world.daynight.SunHalo;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import java.awt.*;

public class PepseGameManager extends GameManager {


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND); //TODO set sky layer
        Night.create(gameObjects(), windowController.getWindowDimensions(), 10, Layer.FOREGROUND);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 10, gameObjects(), Layer.BACKGROUND+1);
        SunHalo.create(gameObjects(), sun, new Color(255, 255, 0, 20), Layer.BACKGROUND+2);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), 1);
        terrain.createInRange(0,(int) windowController.getWindowDimensions().x());

    }

    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }
}
