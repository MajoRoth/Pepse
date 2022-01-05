package Pepse.world.animal;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import java.util.Random;
import java.util.function.Function;

public class AnimalsGenerator {

    private final Random rnd;
    private GameObjectCollection gameObjects;
    private int layer;
    private int blockSize;
    private ImageReader imageReader;
    private float ANIMAL_PROB = 0.075f;
    private final Function<Float, Float> getTerrainHeight;

    /**
     * animal generator - creates animals depends in range.
     * @param gameObjects
     * @param layer
     * @param blockSize
     * @param imageReader
     * @param seed
     * @param getTerrainHeight
     */
    public AnimalsGenerator(GameObjectCollection gameObjects, int layer, int blockSize, ImageReader imageReader,
                            int seed, Function<Float, Float> getTerrainHeight){

        this.gameObjects = gameObjects;
        this.layer = layer;
        this.blockSize = blockSize;

        this.imageReader = imageReader;
        this.rnd = new Random(seed);
        this.getTerrainHeight = getTerrainHeight;
    }

    /**
     * create animals in range of movement.
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX){

        int min = minX - minX % (int) this.blockSize;
        int max = maxX + ((int) this.blockSize - maxX % (int) this.blockSize);

        boolean[] placeAnimals = Pepse.util.RandomHelper.weightedCoin(ANIMAL_PROB,
                (int) ((max - min) / this.blockSize), this.rnd);

        int index = 0;
        for (int x = min; x < max; x += (int) this.blockSize) {
            if (placeAnimals[index]) {
                float root_height = this.getTerrainHeight.apply((float) x);

                GameObject animal1 = Animal.create(gameObjects, this.layer,
                        new Vector2(x,root_height - 5* blockSize), imageReader);
            }
            index += 1;
        }
    }
}
