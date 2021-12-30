package Pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Leaf extends GameObject {
    Transition horizontalTransition;
    Tree tree;

    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,Tree tree) {
        super(topLeftCorner, dimensions, renderable);
        this.tree = tree;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        tree.leafCollision(this);
    }
}
