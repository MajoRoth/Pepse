package Pepse.world.trees;

import Pepse.util.ColorSupplier;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

/**
 * This class handles the creation of trunks and leaves.
 */
public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int CANOPY_MAX = 12;
    private static final int CANOPY_MIN = 7;
    private static final int TREE_MAX_HEIGHT = 17;
    private static final int TREE_MIN_HEIGHT = 12;
    private static final float TREE_PROB = 0.075f;
    private static final float FADE_OUT_TIME = 4f;
    private static final float LEAF_FALLING_VELOCITY = 150;
    private static final float ANIMATION_WAIT_TIME_MAX = 2f;
    private static final float LEAF_LIFE_TIME_MAX = 30f;
    private static final float LEAF_DEATH_TIME_MAX = 25f;
    private static final Float LEAF_ANGLE_MIN = -5f;
    private static final Float LEAF_ANGLE_MAX = 10f;
    private static final float LEAF_ANIMATION_CYCLE_DURATION = 1f;
    private static final float LEAF_MIN_WIDTH_FACTOR = 0.9f;
    private static final float LEAF_MAX_WIDTH_FACTOR = 1.1f;
    private static final Float LEAF_X_MAX_VEL = 30f;
    private static final float LEAF_VEL_TRANSITION_TIME = 1.5f;
    private static final String FALLING_LEAF_TAG = "fallingLeaf";


    private final GameObjectCollection gameObjects;
    private final int trunkLayer;
    private final int leavesLayer;
    private final int fallingLeavesLayer;
    private final Random rnd;
    private final float blockSize;
    private final Function<Float, Float> getTerrainHeight;

    /**
     * C'tor
     * @param gameObjects this game's game object collection (needed for addition of leaves and trunks)
     * @param trunkLayer the layer in which the tree trunks are
     * @param leavesLayer the layer in which the leaves are
     * @param fallingLeavesLayer the layer in which the falling leaves are
     * @param seed the seed for the random functionalities
     * @param blockSize the block size for leaf and trunk blocks
     * @param getTerrainHeight a function that gets the height of the terrain at a given point
     */
    public Tree(GameObjectCollection gameObjects, int trunkLayer, int leavesLayer, int fallingLeavesLayer,
                long seed,
                float blockSize,
                Function<Float, Float> getTerrainHeight) {
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
        this.leavesLayer = leavesLayer;
        this.fallingLeavesLayer = fallingLeavesLayer;
        this.rnd = new Random(seed);
        this.blockSize = blockSize;
        this.getTerrainHeight = getTerrainHeight;
    }

    /**
     * Generates trees in [minX,maxX] range
     *
     * @param minX lower bound of tree creation
     * @param maxX upper bound of tree creation
     */
    public void createInRange(int minX, int maxX) {
        int min = minX - minX % (int) this.blockSize;
        int max = maxX + ((int) this.blockSize - maxX % (int) this.blockSize);
        boolean[] placeTrees = Pepse.util.RandomHelper.weightedCoin(TREE_PROB,
                (int) ((max - min) / this.blockSize), this.rnd);
        int index = 0;
        for (int x = min; x < max; x += (int) this.blockSize) {
            if (placeTrees[index]) {
                float root_height = this.getTerrainHeight.apply((float) x);
                int trunkHeight = this.rnd.nextInt(TREE_MIN_HEIGHT, TREE_MAX_HEIGHT);
                int canopySize = this.rnd.nextInt(CANOPY_MIN, CANOPY_MAX);
                canopySize -= canopySize % 2 == 0 ? 1 : 0; // makes the number odd
                makeTrunk(x, root_height, trunkHeight);
                // it's a minus because y coord is upside-down
                makeLeaves(x, root_height - trunkHeight * blockSize, canopySize);
            }
            index += 1;
        }
    }

    /**
     * A helper method that generates the leaves of a tree
     * @param x            canopy center x location
     * @param canopyCenter canopy center y location
     * @param canopySize   side length of the canopy size
     */
    private void makeLeaves(float x, float canopyCenter, int canopySize) {
        float canopyOffset = (canopySize / 2) * this.blockSize;
        for (int i = 0; i < canopySize; i++) {
            for (int j = 0; j < canopySize; j++) {
                Vector2 pos = new Vector2(x + i * blockSize - canopyOffset,
                        canopyCenter + j * blockSize - canopyOffset);
                makeLeaf(pos);
            }
        }
    }

    /**
     * A helper method that generates each leaf
     * @param pos the leaf's position
     */
    private void makeLeaf(Vector2 pos) {
        Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
        Leaf leafObj = new Leaf(pos, Vector2.ONES.mult(this.blockSize), rect, this);
        gameObjects.addGameObject(leafObj, leavesLayer);
        leafObj.setTag("leaf");
        float leafAnimationWaitTime = rnd.nextFloat(ANIMATION_WAIT_TIME_MAX);
        scheduleAnimationTransitions(leafObj, leafAnimationWaitTime);
        float leafLifeTime = rnd.nextFloat(LEAF_LIFE_TIME_MAX);
        new ScheduledTask(leafObj, leafLifeTime, false,
                () ->
                {
                    gameObjects.removeGameObject(leafObj, leavesLayer);
                    gameObjects.addGameObject(leafObj, fallingLeavesLayer);
                    leafObj.setTag(FALLING_LEAF_TAG);
                    leafObj.transform().setVelocityY(LEAF_FALLING_VELOCITY);
                    Transition transition = new Transition<>(leafObj,
                            leafObj.transform()::setVelocityX,
                            LEAF_X_MAX_VEL, -LEAF_X_MAX_VEL, Transition.CUBIC_INTERPOLATOR_FLOAT,
                            LEAF_VEL_TRANSITION_TIME,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
                    leafObj.horizontalTransition = transition;
                    leafObj.renderer().fadeOut(FADE_OUT_TIME, () -> leafRebirth(leafObj, pos));
                });
    }

    /**
     * A method that handles the rebirth of a leaf (after it has passed away)
     * @param leafObj
     * @param pos
     */
    private void leafRebirth(GameObject leafObj, Vector2 pos) {
        float leafDeathTime = rnd.nextFloat(LEAF_DEATH_TIME_MAX);
        new ScheduledTask(leafObj, leafDeathTime, false, () -> {
            makeLeaf(pos);
            gameObjects.removeGameObject(leafObj, fallingLeavesLayer);
        });
    }

    /**
     * A helper method that schedules transitions to animate the leaves blowing in the wind.
     *
     * @param leafObj               the leaf to animate
     * @param leafAnimationWaitTime schedule delay
     */
    private void scheduleAnimationTransitions(GameObject leafObj, float leafAnimationWaitTime) {
        new ScheduledTask(leafObj,
                leafAnimationWaitTime,
                false,
                () ->
                {
                    Transition angleTrans = new Transition<>(leafObj,
                            (angle) -> {
                                leafObj.renderer().setRenderableAngle(angle);
                            },
                            LEAF_ANGLE_MIN,
                            LEAF_ANGLE_MAX,
                            Transition.CUBIC_INTERPOLATOR_FLOAT,
                            LEAF_ANIMATION_CYCLE_DURATION,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                    Transition widthTrans = new Transition<Vector2>(leafObj,
                            leafObj::setDimensions,
                            leafObj.getDimensions().multX(LEAF_MIN_WIDTH_FACTOR),
                            leafObj.getDimensions().multX(LEAF_MAX_WIDTH_FACTOR),
                            Transition.CUBIC_INTERPOLATOR_VECTOR,
                            LEAF_ANIMATION_CYCLE_DURATION,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                    ((Leaf) leafObj).widthTransition = widthTrans;
                    ((Leaf) leafObj).angleTransition = angleTrans;
                });
    }

    /**
     * A helper method that generates a trunk of a tree
     *
     * @param root_x      its x position (actual)
     * @param root_y      its y position (actual)
     * @param trunkHeight the amount of blocks in the trunk
     */
    private void makeTrunk(float root_x, float root_y, int trunkHeight) {
        for (int i = 1; i <= trunkHeight; i++) {
            Vector2 pos = new Vector2(root_x, root_y - i * this.blockSize);
            Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            GameObject trunk_obj = new GameObject(pos, Vector2.ONES.mult(this.blockSize), rect);
            gameObjects.addGameObject(trunk_obj, trunkLayer);
            trunk_obj.setTag("trunk");
        }
    }

    /**
     * A method that defines what happens to a leaf when it hits the ground.
     * Its in this class because i didn't want to send the gameObjectCollection gameObjects to Leaf on
     * construction.
     * @param leaf the leaf that fell
     */
    public void leafCollision(Leaf leaf) {
        leaf.setVelocity(Vector2.ZERO);
        removeLeafTransitions(leaf);
        gameObjects.removeGameObject(leaf, fallingLeavesLayer);
        gameObjects.addGameObject(leaf, leavesLayer);
        leaf.setTag("leaf");
    }

    /**
     * A helper method that removes the leaf's transitions.
     * @param leaf
     */
    private void removeLeafTransitions(Leaf leaf) {
        leaf.removeComponent(leaf.horizontalTransition);
        leaf.removeComponent(leaf.angleTransition);
        leaf.removeComponent(leaf.widthTransition);
        new Transition<Float>(leaf,
                leaf.transform()::setVelocityX,
                leaf.getVelocity().x(), 0f, Transition.CUBIC_INTERPOLATOR_FLOAT,
                LEAF_VEL_TRANSITION_TIME / 10,
                Transition.TransitionType.TRANSITION_ONCE, null);
        new Transition<>(leaf,
                (angle) -> {
                    leaf.renderer().setRenderableAngle(angle);
                },
                leaf.renderer().getRenderableAngle(), 0f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                LEAF_ANIMATION_CYCLE_DURATION / 10,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
        new Transition<Vector2>(leaf,
                leaf::setDimensions,
                leaf.getDimensions(),
                Vector2.ONES.mult(blockSize),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                LEAF_ANIMATION_CYCLE_DURATION / 10,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }
}
