package application;

import java.util.ArrayList;
import java.util.List;

public class Heuristics {
    static int nextFit(List<Integer> weight, int n, int c)
    {

// Initialize result (Count of bins) and remaining
// capacity in current bin.
        int res = 0, bin_rem = c;

// Place items one by one
        for (int i = 0; i < n; i++)
        {
            // If this item can't fit in current bin
            if (weight.get(i) > bin_rem)
            {
                res++; // Use a new bin
                bin_rem = c - weight.get(i);
            }
            else
                bin_rem -= weight.get(i);
        }
        return res;
    }
    /* NFD */
    public static List<Bin> NFD(List<Integer> weight, int n, int c)
    {
        Bin bin = new Bin();
        List<Bin> s = new ArrayList<>();
        int res = 0, bin_rem = c;

        // Place items one by one
        for (int i = 0; i < n; i++)
        {
            int element = weight.get(i);
            if (!bin.addItem(element))
            {
                s.add(bin); // Use a new bin
                bin = new Bin();
                bin.addItem(element);
            }
        }
        if( ! s.contains(bin))
            s.add(bin);
        return s;
    }

    /* BFD */
    public static List<Bin> BFD(List<Integer> weight, int n, int c)
    {
        int res = 0;
        List<Bin> bins = new ArrayList<>();
        // this.opt = new Node(weight);
        List<Integer> bin_rem = new ArrayList<>();
        int min, bi=0, rem=0;
        Bin b = new Bin();

        // Place items one by one
        for (int i = 0; i < n; i++)
        {
            min = c + 1;
            int w = weight.get(i);
            for (int j = 0; j < bin_rem.size(); j++)
            {
                rem = bin_rem.get(j);
                if (rem >= w &&
                        rem - w < min)
                {
                    bi = j;
                    min = rem - w;
                }
            }

            // If no bin could accommodate weight[i],
            // create a new bin
            if (min == c+1)
            {
                b = new Bin();
                b.addItem(w);
                bins.add(b);
                bin_rem.add(c - w);
            }
            else
            {
                bins.get(bi).addItem(w);
                bin_rem.set(bi, min);
            }
        }
        return bins;
    }

    /* FFD */
    public static List<Bin> FFD(List<Integer> weight, int n, int c)
    {
        int res = 0;
        List<Bin> bins = new ArrayList<>();

        for (int i = 0; i < n; i++){
            int item = weight.get(i);
            int j;

            for(j = 0; j < res; j++){
                int left = bins.get(j).getSpaceLeft();
                if ( item <= left ){
                    bins.get(j).addItem(item);
                    break;
                }
            }

            if (j == res){
                Bin bin = new Bin();
                bin.addItem(item);
                bins.add(bin);
                res++;
            }
        }
        return bins;
    }
}
