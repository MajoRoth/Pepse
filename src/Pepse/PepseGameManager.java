package Pepse;

import Pepse.memento.CareTaker;
import Pepse.memento.Memento;
import Pepse.world.*;
import Pepse.world.animal.Animal;
import Pepse.world.animal.AnimalsGenerator;
import Pepse.world.daynight.Night;
import Pepse.world.daynight.Sun;
import Pepse.world.daynight.SunHalo;
import Pepse.world.trees.Tree;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import java.awt.*;
import java.util.*;

public class PepseGameManager extends GameManager {

    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int HALO_LAYER = Layer.BACKGROUND + 2;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TOPGROUND_LAYER = TERRAIN_LAYER + 1;
    private static final int TRUNK_LAYER = Layer.BACKGROUND + 10;
    private static final int LEAVES_LAYER = Layer.BACKGROUND + 11;
    private static final int FALLING_LEAF_LAYER = LEAVES_LAYER + 1;
    private static final int NIGHT_CYCLE = 100;
    private static final int SEED = 1;
    private static final Map<String, Integer> tagToLayerMap = new HashMap<>();
    private static final String TRUNK_TAG = "trunk";
    private static final String LEAF_TAG = "leaf";
    private static final String BLOCK_TAG = "block";
    private static final String TOPGROUND_TAG = "topGround";
    private static final String FALLING_LEAF_TAG = "fallingLeaf";
    private static final String ANIMAL_TAG = "animal";
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int BLOCK_SIZE = 30;
    private int curCareTakerKey = 0; // Basically the screen index
    private float windowWidth;
    private final CareTaker careTaker = new CareTaker();
    private Terrain terrain;
    private Tree tree;
    private static final String[] tags = new String[]{TRUNK_TAG, LEAF_TAG, BLOCK_TAG, FALLING_LEAF_TAG,
            TOPGROUND_TAG, ANIMAL_TAG};
    private AnimalsGenerator animalsGenerator;

    /**
     * The programs main function
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * The function that initializes the Game
     *
     * @param imageReader      an image reader
     * @param soundReader      a sound reader
     * @param inputListener    an input reader
     * @param windowController the window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        tagToLayerMap.put(TRUNK_TAG, TRUNK_LAYER);
        tagToLayerMap.put(LEAF_TAG, LEAVES_LAYER);
        tagToLayerMap.put(BLOCK_TAG, TERRAIN_LAYER);
        tagToLayerMap.put(TOPGROUND_TAG, TOPGROUND_LAYER);
        tagToLayerMap.put(FALLING_LEAF_TAG, FALLING_LEAF_LAYER);
        tagToLayerMap.put(ANIMAL_TAG, AVATAR_LAYER);
        this.windowWidth = windowController.getWindowDimensions().x();
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), SKY_LAYER);
        Night.create(gameObjects(), windowController.getWindowDimensions(), NIGHT_CYCLE, NIGHT_LAYER);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), NIGHT_CYCLE, gameObjects(),
                SUN_LAYER);
        SunHalo.create(gameObjects(), sun, HALO_COLOR, HALO_LAYER);
        this.terrain = new Terrain(gameObjects(), TERRAIN_LAYER, TOPGROUND_LAYER,
                windowController.getWindowDimensions(), SEED);
        terrain.createInRange(0, (int) (2 * windowWidth));
        this.tree = new Tree(gameObjects(), TRUNK_LAYER, LEAVES_LAYER, FALLING_LEAF_LAYER, SEED, Block.SIZE,
                terrain::groundHeightAt);
        tree.createInRange(0, 2 * (int) windowWidth);

        Vector2 initialAvatarLocation = Vector2.RIGHT.mult(windowController.getWindowDimensions().x() / 2);
        GameObject avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(avatar,
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO, null), FALLING_LEAF_LAYER);
        gameObjects().layers().shouldLayersCollide(FALLING_LEAF_LAYER, TOPGROUND_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TOPGROUND_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TRUNK_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TERRAIN_LAYER, false);
        this.animalsGenerator = new AnimalsGenerator(gameObjects(), AVATAR_LAYER, BLOCK_SIZE, imageReader,
                SEED, terrain::groundHeightAt);
        this.animalsGenerator.createInRange(0, 0);
    }

    /**
     * the update method
     *
     * @param deltaTime the time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int newKey = xToKey(this.getCamera().getTopLeftCorner().x());
        if (newKey > this.curCareTakerKey) { // if moved a screen rightwards
            removeAndStore(newKey - 1);
            getOrGenerate(newKey + 1);
            curCareTakerKey += 1;
        } else if (newKey < this.curCareTakerKey) { // if moved a screen lefwards
            removeAndStore(newKey + 2);
            getOrGenerate(newKey);
            curCareTakerKey -= 1;
        }
    }

    /**
     * gets or generates a chunk of game objects of a certain key
     *
     * @param key the key
     */
    private void getOrGenerate(int key) {
        Memento add_m = careTaker.get(key);
        if (add_m == null) { //Generate
            int minX = (int) keyToX(key);
            int maxX = (int) keyToX(key + 1);
            terrain.createInRange(minX, maxX);
            tree.createInRange(minX, maxX);
            animalsGenerator.createInRange(minX, maxX);
        } else { //Add
            LinkedList<GameObject> add = add_m.getState();
            for (GameObject obj : add) {
                gameObjects().addGameObject(obj, tagToLayerMap.get(obj.getTag()));
            }
        }
    }

    /**
     * removes and stores to the memento a chunk of game objects of a certain key
     *
     * @param key the key
     */
    private void removeAndStore(int key) {
        ArrayList<String> tags_array_list = new ArrayList<>(Arrays.asList(tags));
        LinkedList<GameObject> rem = getObjectsOfTags(tags_array_list, keyToX(key), keyToX(key + 1));
        careTaker.add(key, new Memento(rem));
        for (GameObject obj : rem) {
            String tag = obj.getTag();
            gameObjects().removeGameObject(obj, tagToLayerMap.get(tag));
        }
    }

    /**
     * Translates a certain x to its key (many to one)
     *
     * @param x the x
     * @return the key
     */
    private int xToKey(float x) {
        int negativeOffset = x < 0 ? 1 : 0;
        return (int) x / (int) this.windowWidth - negativeOffset;
    }

    /**
     * Translates a key to the first x that corresponds to it (one to one)
     *
     * @param key the key
     * @return the x
     */
    private float keyToX(int key) {
        return key * this.windowWidth;
    }

    /**
     * A helper method that gets all objects with certain tags
     * @param tags the tag array list
     * @param minX the minimum x
     * @param maxX the maximum x
     * @return a linked list of game objects
     */
    private LinkedList<GameObject> getObjectsOfTags(ArrayList<String> tags, float minX, float maxX) {
        LinkedList<GameObject> ret = new LinkedList<>();
        for (GameObject obj : gameObjects()) {
            boolean inRange = obj.getTopLeftCorner().x() >= minX && obj.getTopLeftCorner().x() < maxX;
            if (tags.contains(obj.getTag()) && inRange) {
                ret.add(obj);
            }
        }
        return ret;
    }
}
