package Pepse.world.trees;
import danogl.collisions.GameObjectCollection;

import java.awt.*;
import java.util.Random;
import java.util.function.IntUnaryOperator;

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100,50,20);
    private static final Color LEAF_COLOR = new Color(50,200,30);
    private static final int LEAVES_SIZE = 10;
    private static final int TREE_MAX_HEIGHT = 20;
    private static final int TREE_MIN_HEIGHT = 15;
    private static final float TREE_PROB = 0.1f;


    private final GameObjectCollection gameObjects;
    private final Random rnd;
    private final int blockSize;
    private final IntUnaryOperator getTerrainHeight;

    public Tree(GameObjectCollection gameObjects, long seed, int blockSize,
                IntUnaryOperator getTerrainHeight){
        this.gameObjects = gameObjects;
        this.rnd = new Random(seed);
        this.blockSize = blockSize;
        this.getTerrainHeight = getTerrainHeight;
    }

    public void createInRange(int minX, int maxX){
        boolean[] place_trees = Pepse.util.RandomHelper.weightedCoin(TREE_PROB,
                (maxX-minX)/this.blockSize,this.rnd);
        for (int i = minX; i < maxX; i++) {
            if (place_trees[i]){
                int x = i*this.blockSize;
                int root_height = this.getTerrainHeight.applyAsInt(x);
                int trunk_height = this.rnd.nextInt(TREE_MIN_HEIGHT,TREE_MAX_HEIGHT);
                make_trunk(x,root_height,trunk_height);
                make_leaves(x,root_height+trunk_height);
            }
        }
    }
    //TODO: implement make_leaves. probably without the use of Block
    private void make_leaves(int x, int i) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    //TODO: implement make_trunk. probably without the use of Block
    private void make_trunk(int root_X, int root_y, int trunk_height) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}
