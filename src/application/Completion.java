package application;

import java.util.ArrayList;
import java.util.List;

public class Completion {

    private List<Integer> c=new ArrayList<>();



    public Completion() {

    }

    public List<Integer> getC() {
        return c;
    }

    public void setC(List<Integer> c) {
        this.c = c;
    }

    public void afficher()
    {
        System.out.print("Feasible set : ");
        for( int k :this.c)
        {
            System.out.print(k+"| ");
        }
        System.out.println("");
    }




}
