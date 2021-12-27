package Pepse;

import Pepse.world.Avatar;
import Pepse.world.Block;
import Pepse.world.Sky;
import Pepse.world.Terrain;
import Pepse.world.daynight.Night;
import Pepse.world.daynight.Sun;
import Pepse.world.daynight.SunHalo;
import Pepse.world.trees.Tree;
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

    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int HALO_LAYER = Layer.BACKGROUND + 2;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TRUNK_LAYER = Layer.BACKGROUND + 10;
    private static final int LEAVES_LAYER = Layer.BACKGROUND + 11;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), SKY_LAYER);
        Night.create(gameObjects(), windowController.getWindowDimensions(), 10, NIGHT_LAYER);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 10, gameObjects(),
                SUN_LAYER);
        SunHalo.create(gameObjects(), sun, new Color(255, 255, 0, 20), HALO_LAYER);
        Terrain terrain = new Terrain(gameObjects(), TERRAIN_LAYER,
                windowController.getWindowDimensions(), 1);
        terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        new Tree(gameObjects(), TRUNK_LAYER, LEAVES_LAYER, 1, Block.SIZE, terrain::groundHeightAt).createInRange(0,
                (int) windowController.getWindowDimensions().x()); // Dan debugs tree creation
        Vector2 initialAvatarLocation = Vector2.RIGHT.mult(windowController.getWindowDimensions().x() / 2);
        GameObject avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(avatar,
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }



    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }
}
