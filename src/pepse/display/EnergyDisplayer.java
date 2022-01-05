package pepse.display;

import pepse.util.FloatCounter;
import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * handles the energy displayer at the top left of the screen
 */
public class EnergyDisplayer extends GameObject {
    private static final String ENERGY_DISPLAY_STRING = "Energy: ";
    private FloatCounter counter;
    private float currentCounter;

    /**
     * constructor
     * @param topLeftCorner - top left corner of the energy bar
     * @param dimensions - the dimensions of the energy bar
     * @param counter - a reference for for the float
     */
    public EnergyDisplayer(Vector2 topLeftCorner, Vector2 dimensions, FloatCounter counter) {
        super(topLeftCorner, dimensions, new TextRenderable(ENERGY_DISPLAY_STRING + Float.toString(counter.getValue())));
        this.counter = counter;
        this.currentCounter = counter.getValue();
    }

    /**
     * update the energy bar
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.currentCounter != this.counter.getValue()){
            this.currentCounter = this.counter.getValue();
            this.renderer().setRenderable(new TextRenderable(ENERGY_DISPLAY_STRING + Float.toString(this.counter.getValue())));
        }
    }
}
