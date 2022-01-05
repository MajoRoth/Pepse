package Pepse.memento;

import java.util.*;

/**
 * CareTaker as learned in TA classes
 */
public class CareTaker {

    /*
    Keys are Integers.
    you spawn in zero and got up or down by 1 when you cover 1 width of screen.
     */
    Map<Integer, Memento> mementoMap = new HashMap<Integer, Memento>();

    /**
     * add a memento to caretaker, given a key
     * @param key
     * @param memento
     */
    public void add(int key, Memento memento){
        this.mementoMap.put(key, memento);
        this.debug();
    }

    /**
     * get a memento by key
     * @param key
     * @return
     */
    public Memento get(int key){
        return this.mementoMap.get(key);
    }

}
