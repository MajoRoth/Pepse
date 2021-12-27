package Pepse.memento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CareTaker {

    /*
    Keys are Integers.
    you spawn in zero and got up or down by 1 when you cover 1 width of screen.
     */
    Map<Integer, Memento> mementoMap = new HashMap<Integer, Memento>();


    public void add(int key, Memento memento){
        this.mementoMap.put(key, memento);
    }

    public Memento get(int key){
        return this.mementoMap.get(key);
    }



}
