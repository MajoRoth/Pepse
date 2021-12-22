package Pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

    private static final Float MIDNIGHT_OPACITY = 0.5f;

    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                    float cycleLength, int layer){

        Renderable renderable = new RectangleRenderable(Color.BLACK);
        GameObject gameObject = new GameObject(Vector2.ZERO, windowDimensions, renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(gameObject, layer);
        gameObject.setTag("Night");

        new Transition<>(gameObject, gameObject.renderer()::setOpaqueness, 0f,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return gameObject;
    }


    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky){

        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}
