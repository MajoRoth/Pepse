package Pepse.memento;

import danogl.collisions.GameObjectCollection;

public class Memento {
    private final GameObjectCollection state;
    public Memento(GameObjectCollection gameObjects){
        this.state = gameObjects;
    }

    public GameObjectCollection getState(){
        return this.state;
    }
}
