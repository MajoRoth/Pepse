package Pepse.memento;

import danogl.GameObject;

import java.util.LinkedList;

public class Memento {
    private final LinkedList<GameObject> state;
    public Memento(LinkedList<GameObject> gameObjects){
        this.state = gameObjects;
    }

    public LinkedList<GameObject> getState(){
        return this.state;
    }
}
