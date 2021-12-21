package Pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

public class Terrain {

    private final int groundHeightAtX0;
    private GameObjectCollection gameObjects;
    private int groundLayer;

    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions){

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = 0; //TODO set groundHeightAtX0
    }

    public float groundHeightAt(float x) {
        return groundHeightAtX0;
    }

}
