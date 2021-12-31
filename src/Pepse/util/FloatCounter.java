package Pepse.util;

public class FloatCounter {

    private float value;

    public FloatCounter(){
        this.value = 0;
    }

    public FloatCounter(float value){
        this.value = value;
    }


    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
