package Pepse.world;

import Pepse.display.EnergyDisplayer;
import Pepse.util.FloatCounter;
import com.sun.jdi.VMOutOfMemoryException;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
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
    private final Renderable idleRenderable;
    private final Renderable jumpRenderable;
    private final Renderable walkRenderable;
    private final UserInputListener inputListener;
    private float fuel;
    private static FloatCounter fuelCounter;
    private static Vector2 dimensions;
    private static Vector2 avatarDimensions = new Vector2(70,160);
    private boolean shouldFlip = false;

    public Avatar(Vector2 topLeftCorner, Vector2 dim, Renderable idleRenderable, Renderable jumpRenderable,
                  Renderable walkRenderable, UserInputListener inputListener) {
        super(topLeftCorner, dim, idleRenderable);
        this.idleRenderable = idleRenderable;
        this.jumpRenderable = jumpRenderable;
        this.walkRenderable = walkRenderable;
        this.inputListener = inputListener;
        this.fuel = MAX_FUEL;
        fuelCounter = new FloatCounter(fuel);
        dimensions = dim;
    }

    //TODO: Add character sprite and animations
    //TODO: Add fuel gauge (not mandatory)
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                    UserInputListener inputListener, ImageReader imageReader) {
        Renderable idleRenderable = imageReader.readImage("Pepse/assets/avatarWalk1.png", true);
        Renderable jumpRenderable = imageReader.readImage("Pepse/assets/avatarJump.png", true);
        Renderable walkRenderable = new AnimationRenderable(new String[]{"Pepse/assets/avatarWalk1.png",
                "Pepse/assets/avatarWalk2.png"}, imageReader, true, 0.1);
        GameObject avatar = new Avatar(topLeftCorner, avatarDimensions, idleRenderable, jumpRenderable,
                walkRenderable,inputListener);
        avatar.transform().setAccelerationY(GRAVITY);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);

        GameObject displayer = new EnergyDisplayer(
                new Vector2((float) (dimensions.x() - 50), dimensions.y() - 50),
                new Vector2(30, 30),
                fuelCounter
        );
        displayer.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(displayer, Layer.UI);

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
        if (verticalMovement != 0f) {
            renderer().setRenderable(jumpRenderable);
        } else if (horizontalMovement != 0f) {
            renderer().setRenderable(walkRenderable);
        }
        else{
            renderer().setRenderable(idleRenderable);
        }
        if (horizontalMovement != 0f)
            shouldFlip = horizontalMovement < 0;
        renderer().setIsFlippedHorizontally(shouldFlip);
        setVelocity(new Vector2(horizontalMovement, verticalMovement));
        fuelCounter.setValue(fuel);
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
