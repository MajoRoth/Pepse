package Pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class's reason of existance is to enable the overriding of onCollisionEnter, and the maintaining of
 * the leafs transitions.
 */
public class Leaf extends GameObject {
    Transition horizontalTransition;
    Transition angleTransition;
    Transition widthTransition;
    Tree tree;

    /**
     * C'tor
     * @param topLeftCorner position
     * @param dimensions dimensions
     * @param renderable the renderable
     * @param tree which tree the leaf belongs to
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,Tree tree) {
        super(topLeftCorner, dimensions, renderable);
        this.tree = tree;
    }

    /**
     * Handles what happens when a leaf collides with the ground
     * @param other the other object
     * @param collision collision data
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        tree.leafCollision(this);
    }
}
