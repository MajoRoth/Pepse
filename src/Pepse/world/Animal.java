package Pepse.world;

import Pepse.display.EnergyDisplayer;
import Pepse.util.FloatCounter;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Animal extends GameObject {

    private final UserInputListener inputListener;
    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 500;
    private static Vector2 animalDimensions = Vector2.ONES.mult(30);

    public Animal(Vector2 topLeftCorner, Vector2 dim, Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, dim, renderable);
        this.inputListener = inputListener;
    }

    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                    UserInputListener inputListener, ImageReader imageReader) {

        Renderable animalRenderable = new RectangleRenderable(Color.RED); //TODO: Change to animal sprite
        GameObject animal = new Animal(topLeftCorner, animalDimensions, animalRenderable, inputListener);
        animal.transform().setAccelerationY(GRAVITY);
        animal.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(animal, layer);

        return animal;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float horizontalMovement = 0;
        float verticalMovement = this.getVelocity().y();
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            horizontalMovement -= MOVEMENT_SPEED;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            horizontalMovement += MOVEMENT_SPEED;
        }
    }
}
