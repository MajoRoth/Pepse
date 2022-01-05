package pepse.util;

import java.util.Random;

/**
 * this class handles the movement of the animals and returns random movemnts
 */
public final class MovementGenerate {

    private static final float BIG_JUMP_PROB = 0.01f;
    private static final float MEDIUM_JUMP_PROB = 0.015f;
    private static final float MEDIUM_JUMP_HEIGHT = 0.8f;
    private static final float SMALL_JUMP_PROB = 0.09f;
    private static final float SMALL_JUMP_HEIGHT = 0.5f;
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
            if (rnd < BIG_JUMP_PROB) {
                return movementSpeed;
            } else if (rnd < MEDIUM_JUMP_PROB){
                return MEDIUM_JUMP_HEIGHT * movementSpeed;
            }
            else if (rnd < SMALL_JUMP_PROB){
                return (float)  SMALL_JUMP_HEIGHT* movementSpeed;
            }
        }
        return 0;
    }
}
