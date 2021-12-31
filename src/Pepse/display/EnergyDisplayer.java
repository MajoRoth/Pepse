package Pepse.display;

import Pepse.util.FloatCounter;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

public class EnergyDisplayer extends GameObject {
    private FloatCounter counter;
    private float currentCounter;

    public EnergyDisplayer(Vector2 topLeftCorner, Vector2 dimensions, FloatCounter counter) {
        super(topLeftCorner, dimensions, new TextRenderable("Energy: " + Float.toString(counter.getValue())));
        this.counter = counter;
        this.currentCounter = counter.getValue();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.currentCounter != this.counter.getValue()){
            this.currentCounter = this.counter.getValue();
            this.renderer().setRenderable(new TextRenderable("Energy: " + Float.toString(this.counter.getValue())));
        }
    }
}
