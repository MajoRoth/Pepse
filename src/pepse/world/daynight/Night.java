package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Night - black background with changes occupancy
 */
public class Night {

    private static final Float MIDNIGHT_OPACITY = 0.7f;

    /**
     * static method to create the black background
     *
     * @param gameObjects      - gameObjects() list
     * @param windowDimensions - yhe dimensions of the window
     * @param cycleLength      - the length of a day
     * @param layer            - the layer that the sky will be at
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {

        Renderable renderable = new RectangleRenderable(Color.BLACK);
        GameObject gameObject = new GameObject(Vector2.ZERO, windowDimensions, renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(gameObject, layer);
        gameObject.setTag("Night");

        new Transition<>(gameObject, gameObject.renderer()::setOpaqueness, 0f,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return gameObject;
    }
}
