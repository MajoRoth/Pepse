package Pepse.util;

import danogl.util.Vector2;

import java.util.Random;

public final class MovementGenerate {

    static Random rd = new Random();

    public static float getHorizontalVelocity(float velocity, float movementSpeed){
            float rnd = rd.nextFloat();
            if (velocity == 0){
                return (float)0.8*movementSpeed;
            }
            if (rnd < 0.001) {
                return -velocity;
            }
        return velocity;
    }

    public static float getJump(float velocity, float movementSpeed){
        boolean grounded = velocity == 0;
        if (grounded) {
            float rnd = rd.nextFloat();
            if (rnd < 0.001) {
                return (float)  movementSpeed;
            } else if (rnd < 0.01){
                return (float)  0.7* movementSpeed;
            }
            else if (rnd < 0.05){
                return (float)  0.5* movementSpeed;
            }
        }
        return 0;
    }
}
