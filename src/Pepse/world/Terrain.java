package Pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

import java.awt.*;

public class Terrain {

    private final int groundHeightAtX0;
    private GameObjectCollection gameObjects;
    private int groundLayer;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions){

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = 0; //TODO set groundHeightAtX0
    }

    public float groundHeightAt(float x) {
        return groundHeightAtX0;
    }

    public void createInRange(int minX, int maxX){

        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }





}
