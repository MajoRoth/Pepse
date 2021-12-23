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

public class Terrain {

    private final int groundHeightAtX0;
    private final PerlinNoise perlinNoise;
    private GameObjectCollection gameObjects;
    private int groundLayer;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    public Terrain(GameObjectCollection gameObjects, int groundLayer,Vector2 windowDimensions, int seed){

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = (int) (windowDimensions.y()*2/3); //TODO set groundHeightAtX0
        this.perlinNoise = new PerlinNoise(seed);
    }

    public float groundHeightAt(float x) {
        return this.groundHeightAtX0 + 2*Block.SIZE*(float) this.perlinNoise.noise(x);
    }

    public void createInRange(int minX, int maxX){

        Renderable rect;

        int min = minX - minX % (int) Block.SIZE;
        int max = maxX + ((int) Block.SIZE - maxX% (int) Block.SIZE);

        for (int x = min; x < max; x += (int) Block.SIZE){
            for (int i=0; i < this.TERRAIN_DEPTH; i++){
                rect = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(x, this.groundHeightAt(x) + i*Block.SIZE), rect);
                this.gameObjects.addGameObject(block, groundLayer);
                block.setTag("block");
            }

        }
    }





}
