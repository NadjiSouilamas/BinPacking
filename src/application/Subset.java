package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Subset {
    private List<Integer> set;
    private int index;

    public Subset(int index, List set){
        this.index = index;
        this.set = set;
    }

    public int sum(){
        int s = 0;
        for (Integer e: set) {
            s += e;
        }
        return s;
    }

    public boolean isSingleton(){
        return (set.size() == 1);
    }


    public ArrayDeque getChildren(){
        int i = index;
        int size = set.size();
        ArrayDeque<Subset> childrenSet = new ArrayDeque<>();
        ArrayList child;

        while( size - i > 0 ){
            child = (ArrayList)((ArrayList<Integer>) set).clone();
            child.remove(i);
            childrenSet.add(new Subset(i,child));
            i++;
        }
        return childrenSet;
    }
}

