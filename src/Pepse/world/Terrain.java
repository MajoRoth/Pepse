package Pepse.world;

import Pepse.util.ColorSupplier;
import Pepse.util.PerlinNoise;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Vector;

/**
 * Terrain in changing height with perlin noise
 */
public class Terrain {

    private static final int TOPGROUND_DEPTH = 5;
    private int topGroundLayer;
    private final int groundHeightAtX0;
    private final PerlinNoise perlinNoise;
    private GameObjectCollection gameObjects;
    private int groundLayer;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    /**
     * constructor
     * @param gameObjects
     * @param groundLayer
     * @param topGroundLayer
     * @param windowDimensions
     * @param seed
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer,int topGroundLayer,
                   Vector2 windowDimensions, int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.topGroundLayer = topGroundLayer;
        this.groundHeightAtX0 = (int) (windowDimensions.y() * 2 / 3); //TODO set groundHeightAtX0
        this.perlinNoise = new PerlinNoise(seed);
    }

    /**
     * calculates the height of the ground at each x
     * @param x
     * @return
     */
    public float groundHeightAt(float x) {
        return this.groundHeightAtX0 + 3 * Block.SIZE * (float) this.perlinNoise.noise(x);
    }

    /**
     * creates all the block in a given range and adds to gameObject()
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX) {

        Renderable rect;

        int min = minX - minX % (int) Block.SIZE;
        int max = maxX + ((int) Block.SIZE - maxX % (int) Block.SIZE);

        for (int x = min; x < max; x += (int) Block.SIZE) {
            for (int i = 0; i < TOPGROUND_DEPTH; i++) {
                rect = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x, this.groundHeightAt(x) + i * Block.SIZE), rect);
                this.gameObjects.addGameObject(block, topGroundLayer);
                block.setTag("topGround");
            }
            for (int i = TOPGROUND_DEPTH; i < TERRAIN_DEPTH; i++) {
                rect = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x, this.groundHeightAt(x) + i * Block.SIZE), rect);
                this.gameObjects.addGameObject(block, groundLayer);
                block.setTag("block");
            }
        }
    }


}
