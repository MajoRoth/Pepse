package Pepse.world;

import com.sun.jdi.VMOutOfMemoryException;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 500;
    private static final float MAX_FUEL = 100;
    private static final float FUEL_DECREASE_RATE = 0.5f;
    private static final float FUEL_INCREASE_RATE = 0.5f;
    private final UserInputListener inputListener;
    private float fuel;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.fuel = MAX_FUEL;
    }

    //TODO: Add character sprite and animations
    //TODO: Add fuel gauge (not mandatory)
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                    UserInputListener inputListener, ImageReader imageReader) {
        Vector2 avatarDimensions = Vector2.ONES.mult(100);
        Renderable avatarRenderable = new RectangleRenderable(Color.ORANGE); //TODO: Change to avatar sprite
        GameObject avatar = new Avatar(topLeftCorner, avatarDimensions, avatarRenderable, inputListener);
        avatar.transform().setAccelerationY(GRAVITY);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
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
        verticalMovement = handleJumpAndFly(verticalMovement);
        setVelocity(new Vector2(horizontalMovement, verticalMovement));
    }

    float handleJumpAndFly(float verticalMovement) {
        boolean spacePressed = inputListener.isKeyPressed(KeyEvent.VK_SPACE);
        boolean grounded = verticalMovement == 0;
        boolean shiftPressed = inputListener.isKeyPressed(KeyEvent.VK_SHIFT);
        boolean gotFuel = this.fuel > 0;
        boolean canJump = spacePressed && (grounded || (shiftPressed && gotFuel));
        if (canJump) { // jump
            if (!grounded) {
                this.fuel = Math.max(this.fuel - FUEL_DECREASE_RATE, 0);
            }
            verticalMovement = -MOVEMENT_SPEED;
        }
        if (grounded) {
            this.fuel = Math.min(this.fuel + FUEL_INCREASE_RATE, MAX_FUEL);
        }
        return verticalMovement;
    }
}
