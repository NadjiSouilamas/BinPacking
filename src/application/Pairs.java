package application;


import java.util.ArrayList;
import java.util.List;

public class Pairs {

    private List<Integer> objects = new ArrayList<>();
    private int numberPairs = 0;

    public void add(int a, int b){
        objects.add(a);
        objects.add(b);
        numberPairs++ ;
    }

    public int getNumberPairs() {
        return numberPairs;
    }

    public void printPairs(){
        if (numberPairs == 0){
            System.out.println("No pair to display !");
        }
        else
        {
            for (int i = 0; i < numberPairs; i++ ){
                System.out.println("Pair "+i+":\t"+objects.get(2*i)+"\t"+objects.get(2*i + 1));
            }
        }
    }
}
