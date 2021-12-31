package Pepse.memento;

import java.util.*;

public class CareTaker {

    /*
    Keys are Integers.
    you spawn in zero and got up or down by 1 when you cover 1 width of screen.
     */
    Map<Integer, Memento> mementoMap = new HashMap<Integer, Memento>();


    public void add(int key, Memento memento){
        this.mementoMap.put(key, memento);
        this.debug();
    }

    public Memento get(int key){
        return this.mementoMap.get(key);
    }


    private void debug(){
        System.out.println("------");
        List<Integer> sortedList = new ArrayList<>(mementoMap.keySet());
        Collections.sort(sortedList);
        for (int i: sortedList) {
            System.out.println(i);
        }
    }



}
