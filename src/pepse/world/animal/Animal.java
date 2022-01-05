package pepse.world.animal;

import pepse.util.MovementGenerate;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Animal GameObject
 */
public class Animal extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 500;
    private static final String ANIMAL_TAG = "animal";
    private static final String FROG_ASSET = "pepse/assets/Frog.png";
    private static Vector2 animalDimensions = Vector2.ONES.mult(40);
    private boolean shouldFlip = false;

    /**
     * constructor for animal
     * @param topLeftCorner
     * @param dim
     * @param renderable
     */
    public Animal(Vector2 topLeftCorner, Vector2 dim, Renderable renderable) {
        super(topLeftCorner, dim, renderable);
    }

    /**
     * static method to create animals
     * call Animal.create({params}) to create an animal and add ot game
     * @param gameObjects - gameobjects() list
     * @param layer - the layer of the animals
     * @param topLeftCorner - the spawn place
     * @param imageReader - image reader supplied by Game Manager
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                    ImageReader imageReader) {

//        Renderable animalRenderable = new RectangleRenderable(Color.RED); //TODO: Change to animal sprite
        Renderable animalRenderable = imageReader.readImage(FROG_ASSET, true);
        GameObject animal = new Animal(topLeftCorner, animalDimensions, animalRenderable);
        animal.transform().setAccelerationY(GRAVITY);
        animal.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(animal, layer);
        animal.setTag(ANIMAL_TAG);

        return animal;
    }


    /**
     * controls animal's movement
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float horizontalMovement = MovementGenerate.getHorizontalVelocity(this.getVelocity().x(), MOVEMENT_SPEED);
        float verticalMovement = this.getVelocity().y() - MovementGenerate.getJump(this.getVelocity().y(), MOVEMENT_SPEED);
        if (horizontalMovement != 0f)
            shouldFlip = horizontalMovement < 0;
        this.renderer().setIsFlippedHorizontally(shouldFlip);
        setVelocity(new Vector2(horizontalMovement, verticalMovement));
    }
}
