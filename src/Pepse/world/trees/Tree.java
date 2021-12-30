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

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int CANOPY_MAX = 14;
    private static final int CANOPY_MIN = 7;
    private static final int TREE_MAX_HEIGHT = 17;
    private static final int TREE_MIN_HEIGHT = 12;
    private static final float TREE_PROB = 0.1f;


    private final GameObjectCollection gameObjects;
    private final int trunkLayer;
    private final int leavesLayer;
    private final Random rnd;
    private final float blockSize;
    private final Function<Float, Float> getTerrainHeight;

    public Tree(GameObjectCollection gameObjects, int trunkLayer, int leavesLayer, long seed, float blockSize,
                Function<Float, Float> getTerrainHeight) {
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
        this.leavesLayer = leavesLayer;
        this.rnd = new Random(seed);
        this.blockSize = blockSize;
        this.getTerrainHeight = getTerrainHeight;
    }

    /**
     * Generates trees in [minX,maxX] range
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
                make_trunk(x, root_height, trunkHeight);
                // it's a minus because y coord is upside-down
                make_leaves(x, root_height - trunkHeight * blockSize, canopySize);
            }
            index += 1;
        }
    }

    /**
     * A helper method that generates the leaves of a tree
     * @param x canopy center x location
     * @param canopyCenter canopy center y location
     * @param canopySize side length of the canopy size
     */
    private void make_leaves(float x, float canopyCenter, int canopySize) {
        float canopyOffset = (canopySize / 2) * this.blockSize;
        for (int i = 0; i < canopySize; i++) {
            for (int j = 0; j < canopySize; j++) {
                Vector2 pos = new Vector2(x + i * blockSize - canopyOffset,
                        canopyCenter + j * blockSize - canopyOffset);
                Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
                GameObject leafObj = new GameObject(pos, Vector2.ONES.mult(this.blockSize), rect);
                gameObjects.addGameObject(leafObj, leavesLayer);
                leafObj.setTag("leaf");
                float leafAnimationWaitTime = rnd.nextFloat(2f);
                scheduleAnimationTransitions(leafObj, leafAnimationWaitTime);
                //TODO: implement falling and reappearing leaves.
            }
        }
    }

    /**
     * A helper method that schedules transitions to animate the leaves blowing in the wind.
     * @param leafObj the leaf to animate
     * @param leafAnimationWaitTime schedule delay
     */
    private void scheduleAnimationTransitions(GameObject leafObj, float leafAnimationWaitTime) {
        new ScheduledTask(leafObj,
                leafAnimationWaitTime,
                false,
                () ->
                {
                    new Transition<>(leafObj,
                            (angle) -> {
                                leafObj.renderer().setRenderableAngle(angle);
                            },
                            0f,
                            30f,
                            Transition.CUBIC_INTERPOLATOR_FLOAT,
                            1.3f,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                    new Transition<Vector2>(leafObj,
                            leafObj::setDimensions,
                            leafObj.getDimensions(),
                            leafObj.getDimensions().multX(1.2f),
                            Transition.CUBIC_INTERPOLATOR_VECTOR,
                            1.3f,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                });
    }

    /**
     * A helper method that generates a trunk of a tree
     * @param root_x      its x position (actual)
     * @param root_y      its y position (actual)
     * @param trunkHeight the amount of blocks in the trunk
     */
    private void make_trunk(float root_x, float root_y, int trunkHeight) {
        for (int i = 1; i <= trunkHeight; i++) {
            Vector2 pos = new Vector2(root_x, root_y - i * this.blockSize);
            Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            GameObject trunk_obj = new GameObject(pos, Vector2.ONES.mult(this.blockSize), rect);
            gameObjects.addGameObject(trunk_obj, trunkLayer);
            trunk_obj.setTag("trunk");
        }
    }
}
