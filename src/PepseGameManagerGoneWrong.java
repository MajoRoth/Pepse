import Pepse.memento.CareTaker;
import Pepse.memento.Memento;
import Pepse.world.Avatar;
import Pepse.world.Block;
import Pepse.world.Sky;
import Pepse.world.Terrain;
import Pepse.world.daynight.Night;
import Pepse.world.daynight.Sun;
import Pepse.world.daynight.SunHalo;
import Pepse.world.trees.Tree;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


// This file is full of shit.
public class PepseGameManagerGoneWrong extends GameManager {

    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int HALO_LAYER = Layer.BACKGROUND + 2;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TRUNK_LAYER = Layer.BACKGROUND + 10;
    private static final int LEAVES_LAYER = Layer.BACKGROUND + 11;
    private static final Map<String, Integer> tagToMap = new HashMap<String, Integer>();
    private int curCareTakerKey = 0;
    private float windowWidth;


//    public static void main(String[] args) {
//        new PepseGameManager().run();
//    }

    private GameObjectCollection stateLeft;
    private GameObjectCollection stateMid;
    private GameObjectCollection stateRight;
    private CareTaker careTaker;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowWidth = windowController.getWindowDimensions().x();
        careTaker = new CareTaker();
        tagToMap.put("trunk", TRUNK_LAYER);
        tagToMap.put("leaf", LEAVES_LAYER);
        tagToMap.put("block", TERRAIN_LAYER);
        int minRange = -(int) windowWidth;
        int maxRange = 2 * (int) windowWidth;
        Sky.create(this.gameObjects(), windowController.getWindowDimensions(), SKY_LAYER);
        Night.create(gameObjects(), windowController.getWindowDimensions(), 10, NIGHT_LAYER);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 10, gameObjects(),
                SUN_LAYER);
        SunHalo.create(gameObjects(), sun, new Color(255, 255, 0, 20), HALO_LAYER);
        Terrain terrain = new Terrain(gameObjects(), TERRAIN_LAYER,
                windowController.getWindowDimensions(), 1);
        terrain.createInRange(minRange, maxRange);
        new Tree(gameObjects(), TRUNK_LAYER, LEAVES_LAYER, 1, Block.SIZE, terrain::groundHeightAt).createInRange(minRange,
                maxRange); // Dan debugs tree creation
        Vector2 initialAvatarLocation = Vector2.RIGHT.mult(windowController.getWindowDimensions().x() / 2);
        GameObject avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                initialAvatarLocation, inputListener, imageReader);
        setCamera(new Camera(avatar,
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        String[] tags = new String[]{"trunk", "leaf", "block"};
        this.stateLeft = getObjectsOfTags(new ArrayList<String>(Arrays.asList(tags)), -windowWidth, 0);
        this.stateMid = getObjectsOfTags(new ArrayList<String>(Arrays.asList(tags)), 0, windowWidth);
        this.stateRight = getObjectsOfTags(new ArrayList<String>(Arrays.asList(tags)), windowWidth,
                2 * windowWidth);
        this.careTaker.add(-1, new Memento(stateLeft));
        this.careTaker.add(0, new Memento(stateMid));
        this.careTaker.add(1, new Memento(stateRight));
    }

    private GameObjectCollection getObjectsOfTags(ArrayList<String> tags, float minX, float maxX) {
        GameObjectCollection ret = new GameObjectCollection();
        for (GameObject obj : gameObjects()) {
            boolean inRange = obj.getTopLeftCorner().x() >= minX && obj.getTopLeftCorner().x() < maxX;
            if (tags.contains(obj.getTag()) && inRange {
                ret.addGameObject(obj, tagToMap.get(obj.getTag()));
            }
        }
        return ret;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int newKey = xToKey(this.getCamera().getTopLeftCorner().x());
        if (newKey != this.curCareTakerKey) {
            changeRenderedObjects(newKey);
        }
    }

    private void changeRenderedObjects(int newKey) {
        // if newKey in CareTaker,
    }

    public void setState(GameObjectCollection gameObjects, StateKind stateKind) {
        if (stateKind == StateKind.LEFT_STATE) {
            this.stateLeft = gameObjects;
        } else if (stateKind == StateKind.MID_STATE) {
            this.stateMid = gameObjects;
        } else {
            this.stateRight = gameObjects;
        }
    }

    public GameObjectCollection getState(StateKind stateKind) {
        if (stateKind == StateKind.LEFT_STATE) {
            return this.stateLeft;
        } else if (stateKind == StateKind.MID_STATE) {
            return this.stateMid;
        } else {
            return this.stateRight;
        }
    }

    public Memento saveStateToMemento(int key, StateKind stateKind) {
        Memento memento;
        if (stateKind == StateKind.LEFT_STATE) {
            memento = new Memento(this.stateLeft);
        } else if (stateKind == StateKind.MID_STATE) {
            memento = new Memento(this.stateMid);
        } else {
            memento = new Memento(this.stateRight);
        }
    }

    public void getStateFromMemento(int key, StateKind stateKind) {
        Memento memento = this.careTaker.get(key);
        GameObjectCollection state;
        if (stateKind == StateKind.LEFT_STATE) {
            state = this.stateLeft;
        } else if (stateKind == StateKind.MID_STATE) {
            state = this.stateMid;
        } else {
            state = this.stateRight;
        }
        for (GameObject obj : state) {
            gameObjects().removeGameObject(obj, tagToMap.get(obj.getTag()));
        }
        this.state = memento.getState();
        for (GameObject obj : state) {
            gameObjects().addGameObject(obj, tagToMap.get(obj.getTag()));
        }
        this.careTaker.add(key, memento);
    }

    private int xToKey(float x) {
        int negativeOffset = x < 0 ? 1 : 0;
        return (int) x / (int) this.windowWidth - negativeOffset;
    }



    @FunctionalInterface
    public interface Component {
        void update(float deltaTime);
    }
}
