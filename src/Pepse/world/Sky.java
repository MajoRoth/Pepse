package Pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * blue background
 */
public class Sky extends GameObject{

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * constructor
     * @param topLeftCorner
     * @param dimensions
     * @param renderable
     */
    public Sky(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * static method to create the sky
     * @param gameObjects
     * @param windowDimensions
     * @param skyLayer
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {

        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));

        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag("sky");

        return sky;
    }

}
