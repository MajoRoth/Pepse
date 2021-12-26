package Pepse.world.trees;

import Pepse.util.ColorSupplier;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int CANOPY_SIZE = 15;
    private static final int TREE_MAX_HEIGHT = 20;
    private static final int TREE_MIN_HEIGHT = 15;
    private static final float TREE_PROB = 0.1f;


    private final GameObjectCollection gameObjects;
    private int trunkLayer;
    private int leavesLayer;
    private final Random rnd;
    private final float blockSize;
    private final Function<Float, Float> getTerrainHeight;

    public Tree(GameObjectCollection gameObjects,int trunkLayer, int leavesLayer, long seed, float blockSize,
                Function<Float, Float> getTerrainHeight) {
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
        this.leavesLayer = leavesLayer;
        this.rnd = new Random(seed);
        this.blockSize = blockSize;
        this.getTerrainHeight = getTerrainHeight;
    }

    public void createInRange(int minX, int maxX) {
        int min = minX - minX % (int) this.blockSize;
        int max = maxX + ((int) this.blockSize - maxX% (int) this.blockSize);
        boolean[] place_trees = Pepse.util.RandomHelper.weightedCoin(TREE_PROB,
                (int) ((max - min) / this.blockSize), this.rnd);
        int index = 0;
        for (int x = min; x < max; x += (int) this.blockSize){
            if (place_trees[index]) {
                float root_height = this.getTerrainHeight.apply((float)x);

                // TODO Dan - Check this function call below - i think you need to change it to this:
                // TREE_MIN_HEIGHT + this.rnd.nextInt(TREE_MAX_HEIGHT);

                int trunk_height = this.rnd.nextInt(TREE_MIN_HEIGHT, TREE_MAX_HEIGHT);
                make_trunk(x, root_height, trunk_height);
                make_leaves(x, root_height - trunk_height); // it's a minus because y coord is upside-down
            }
            index+=1;
        }
    }

    //TODO: implement make_leaves. probably without the use of Block
    private void make_leaves(float x, float canopyCenter) {
        x = x/this.blockSize;
        int canopyOffset = CANOPY_SIZE / 2;
        for (int i = 0; i < CANOPY_SIZE; i++) {
            for (int j = 0; j < CANOPY_SIZE; j++) {
                Vector2 pos =
                        new Vector2(x + i - canopyOffset, canopyCenter + j - canopyOffset).mult(this.blockSize);
                Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
                gameObjects.addGameObject(new GameObject(pos, Vector2.ONES.mult(this.blockSize),
                        rect),leavesLayer);
            }
        }
    }

    //TODO: implement make_trunk. probably without the use of Block
    private void make_trunk(float root_X, float root_y, int trunkHeight) {
        for (int i = 0; i < trunkHeight; i++) {
            Vector2 pos = new Vector2(root_X,root_y - i*this.blockSize);
            Renderable rect = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
            GameObject trunk_obj = new GameObject(pos, Vector2.ONES.mult(this.blockSize), rect);
            gameObjects.addGameObject(trunk_obj,trunkLayer);
            trunk_obj.setTag("block");
        }
    }
}
