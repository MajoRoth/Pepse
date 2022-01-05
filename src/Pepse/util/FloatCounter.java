package Pepse.util;

/**
 * sets up a reference for float
 */
public class FloatCounter {

    private float value;

    /**
     * empty constructor
     */
    public FloatCounter(){
        this.value = 0;
    }

    /**
     * constructor that takes initial value
     * @param value
     */
    public FloatCounter(float value){
        this.value = value;
    }

    /**
     * getter
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     * setter
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
    }
}
