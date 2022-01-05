package Pepse.world.animal;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import java.util.Random;
import java.util.function.Function;

public class AnimalsGenerator {

    private static final float ANIMAL_SPAWN_BUFFER = 5;
    private final Random rnd;
    private GameObjectCollection gameObjects;
    private int layer;
    private int blockSize;
    private ImageReader imageReader;
    private float ANIMAL_PROB = 0.075f;
    private final Function<Float, Float> getTerrainHeight;

    /**
     * animal generator - creates animals depends in range.
     * @param gameObjects - gameobjects() list
     * @param layer - the layer of the animals
     * @param blockSize - size of block constant
     * @param imageReader - image reader from game maanger
     * @param seed - the random seed
     * @param getTerrainHeight - function that returns the height of the terrain as supplied in Terrain
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
     * @param minX - the most left x coor to be rendered
     * @param maxX - the most right x coor to be rendered
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
                        new Vector2(x,root_height - ANIMAL_SPAWN_BUFFER * blockSize), imageReader);
            }
            index += 1;
        }
    }
}
