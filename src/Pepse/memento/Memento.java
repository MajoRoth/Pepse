package Pepse.memento;

import danogl.GameObject;

import java.util.LinkedList;

/**
 * Memento as learned in TA classes
 */
public class Memento {
    private final LinkedList<GameObject> state;

    /**
     * creates a memento and sets is value
     * @param gameObjects
     */
    public Memento(LinkedList<GameObject> gameObjects){
        this.state = gameObjects;
    }

    /**
     * returns a memento's content
     * @return
     */
    public LinkedList<GameObject> getState(){
        return this.state;
    }
}
