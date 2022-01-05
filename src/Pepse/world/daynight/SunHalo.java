package Pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * sun halo - follows the sun and surronds it
 */
public class SunHalo {

    private static final String SUN_HALO_TAG = "Sun Halo";
    private static final float HALLO_SIZE = 250;

    /**
     * static method to create the halo
     * @param gameObjects - gameObjects() list
     * @param sun - the sun gameobject
     * @param color - the color of the halo
     * @param layer - the layer that the halo will be at
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects, GameObject sun,
            Color color, int layer){

        Renderable renderable = new OvalRenderable(color);
        GameObject gameObject = new GameObject(Vector2.ZERO, new Vector2(HALLO_SIZE, HALLO_SIZE), renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(gameObject, layer);
        gameObject.setTag(SUN_HALO_TAG);
        gameObject.addComponent((f) -> {
            gameObject.setCenter(sun.getCenter());
        });

        return gameObject;

    }
}
