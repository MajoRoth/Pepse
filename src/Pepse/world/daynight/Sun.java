package Pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * game object of sun - moves through the sky in an ellipse
 */
public class Sun {

    private static final Vector2 sunDimensions = new Vector2(150, 150);
    private static Vector2 sunCenter;
    private static Vector2 sunVector;

    /**
     * static method to create the sun
     * @param windowDimensions
     * @param cycleLength
     * @param gameObjects
     * @param layer
     * @return
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength,
                                    GameObjectCollection gameObjects, int layer){

        Vector2 sunTopLeft = new Vector2(windowDimensions.x()/2 - sunDimensions.x()/2,
                windowDimensions.y()/2 - sunDimensions.y()/2);
        Renderable renderable = new OvalRenderable(Color.YELLOW);
        GameObject gameObject = new GameObject(sunTopLeft, sunDimensions, renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(gameObject, layer);
        gameObject.setTag("Sun");

        sunCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()/2);
        sunVector = new Vector2(windowDimensions.x()/5, windowDimensions.y()/5);

        new Transition<Float>(gameObject,
                (f) -> {gameObject.setCenter(calcSunPosition(windowDimensions, f));},
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);


        return gameObject;
    }

    /**
     * calculates the position of the sun by the angle
     * @param windowDimensions
     * @param angleInSky
     * @return
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky){
        return sunCenter.add(
                new Vector2((float)Math.sin(Math.toRadians(angleInSky+180)),
                        (float)Math.cos(Math.toRadians(angleInSky+180))).mult(
                        (float) (1+0.3*Math.sin(Math.toRadians(angleInSky-90)))*500
                )
        );
    }
}
