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
    // For memento
    private static final Map<String, Integer> tagToLayerMap = new HashMap<String, Integer>();
    private int curCareTakerKey = 0; // Basically the screen index
    private float windowWidth;
    private final CareTaker careTaker = new CareTaker();
    private Terrain terrain;
    private Tree tree;
    private static final String[] tags = new String[]{"trunk", "leaf", "block", "fallingLeaf", "topGround", "animal"};
    private AnimalsGenerator animalsGenerator;
    //~~~until here


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // For memento
        tagToLayerMap.put("trunk", TRUNK_LAYER);
        tagToLayerMap.put("leaf", LEAVES_LAYER);
        tagToLayerMap.put("block", TERRAIN_LAYER);
        tagToLayerMap.put("topGround", TOPGROUND_LAYER);
        tagToLayerMap.put("fallingLeaf", FALLING_LEAF_LAYER);
        tagToLayerMap.put("animal", AVATAR_LAYER);
        this.windowWidth = windowController.getWindowDimensions().x();
        //~~~until here
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), SKY_LAYER);
        Night.create(gameObjects(), windowController.getWindowDimensions(), NIGHT_CYCLE, NIGHT_LAYER);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), NIGHT_CYCLE, gameObjects(),
                SUN_LAYER);
        SunHalo.create(gameObjects(), sun, new Color(255, 255, 0, 20), HALO_LAYER);
        //changed from local variable for memento
        this.terrain = new Terrain(gameObjects(), TERRAIN_LAYER, TOPGROUND_LAYER,
                windowController.getWindowDimensions(), SEED);
        terrain.createInRange(0, (int) (2 * windowWidth));
        this.tree = new Tree(gameObjects(), TRUNK_LAYER, LEAVES_LAYER, FALLING_LEAF_LAYER, SEED, Block.SIZE,
                terrain::groundHeightAt);
        tree.createInRange(0, 2 * (int) windowWidth); // Dan debugs tree creation
        //~~~until here

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


        this.animalsGenerator = new AnimalsGenerator(gameObjects(), AVATAR_LAYER, 30, imageReader,
                SEED, terrain::groundHeightAt);
        this.animalsGenerator.createInRange(0 ,0);

         /*
         GameObject animal = Animal.create(gameObjects(), AVATAR_LAYER,
                initialAnimalLocation, imageReader);

          */
    }


    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }

    // Memento Behaviour

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

    private void removeAndStore(int key) {
        ArrayList<String> tags_array_list = new ArrayList<String>(Arrays.asList(tags));
        LinkedList<GameObject> rem = getObjectsOfTags(tags_array_list, keyToX(key), keyToX(key + 1));
        careTaker.add(key, new Memento(rem));
        for (GameObject obj : rem) {
            String tag = obj.getTag();
            gameObjects().removeGameObject(obj, tagToLayerMap.get(tag));
        }
    }


    private int xToKey(float x) {
        int negativeOffset = x < 0 ? 1 : 0;
        return (int) x / (int) this.windowWidth - negativeOffset;
    }

    private float keyToX(int key) {
        return key * this.windowWidth;
    }

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
