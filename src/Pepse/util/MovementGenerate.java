package Pepse.util;

import danogl.util.Vector2;

import java.util.Random;

/**
 * this class handles the movement of the animals and returns random movemnts
 */
public final class MovementGenerate {

    static Random rd = new Random();

    /**
     * returns the new horizontal velocity
     * @param velocity - current velocity
     * @param movementSpeed - constant
     * @return
     */
    public static float getHorizontalVelocity(float velocity, float movementSpeed){
            float rnd = rd.nextFloat();
            if (velocity == 0){
                return (float)0.8*movementSpeed;
            }
            if (rnd < 0.04) {
                return -velocity;
            }
        return velocity;
    }

    /**
     * returns vertical velocity
     * @param velocity - current vertical velocity
     * @param movementSpeed - constant
     * @return
     */
    public static float getJump(float velocity, float movementSpeed){
        boolean grounded = velocity == 0;
        if (grounded) {
            float rnd = rd.nextFloat();
            if (rnd < 0.001) {
                return movementSpeed;
            } else if (rnd < 0.01){
                return movementSpeed;
            }
            else if (rnd < 0.09){
                return (float)  0.5* movementSpeed;
            }
        }
        return 0;
    }
}
