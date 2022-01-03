package Pepse.world.animal;

import Pepse.util.MovementGenerate;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Animal extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 500;
    private static Vector2 animalDimensions = Vector2.ONES.mult(40);

    public Animal(Vector2 topLeftCorner, Vector2 dim, Renderable renderable) {
        super(topLeftCorner, dim, renderable);
    }

    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                    ImageReader imageReader) {

        Renderable animalRenderable = new RectangleRenderable(Color.RED); //TODO: Change to animal sprite
        GameObject animal = new Animal(topLeftCorner, animalDimensions, animalRenderable);
        animal.transform().setAccelerationY(GRAVITY);
        animal.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(animal, layer);
        animal.setTag("animal");

        return animal;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float horizontalMovement = MovementGenerate.getHorizontalVelocity(this.getVelocity().x(), MOVEMENT_SPEED);
        float verticalMovement = this.getVelocity().y() - MovementGenerate.getJump(this.getVelocity().y(), MOVEMENT_SPEED);

        setVelocity(new Vector2(horizontalMovement, verticalMovement));
    }
}
