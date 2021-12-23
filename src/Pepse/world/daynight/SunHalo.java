package Pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    public static GameObject create(GameObjectCollection gameObjects, GameObject sun,
            Color color, int layer){

        Renderable renderable = new OvalRenderable(color);
        GameObject gameObject = new GameObject(Vector2.ZERO, new Vector2(250, 250), renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(gameObject, layer);
        gameObject.setTag("Sun Halo");
        gameObject.addComponent((f) -> {
            gameObject.setCenter(sun.getCenter());
        });




        return gameObject;

    }
}
