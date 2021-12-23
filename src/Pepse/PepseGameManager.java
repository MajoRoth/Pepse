package Pepse;

import Pepse.world.Sky;
import Pepse.world.daynight.Night;
import Pepse.world.daynight.Sun;
import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

public class PepseGameManager extends GameManager {


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), 1); //TODO set sky layer
        Night.create(gameObjects(), windowController.getWindowDimensions(), 10, Layer.FOREGROUND);
        Sun.create(windowController.getWindowDimensions(), 10, gameObjects(), Layer.FOREGROUND);

    }
}
