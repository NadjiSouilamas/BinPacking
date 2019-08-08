package application;

import java.util.*;
import java.lang.Math;

public class SA {

    private List<Integer> items = new ArrayList<>();
    private int n;
    private int c;

    public SA(List<Integer> items, int n, int c){

        this.items.addAll(items);
        this.n = n;
        this.c = c;
    }

    public SA(Instance instance){
        this.items.addAll(instance.getItemsList());
        this.n = instance.getN();
        this.c = instance.getC();
        Bin.c = this.c;
    }

    public List<Bin> simulatedAnnealing(int kmax, double t, int initSol, double alpha){
        List<Bin> s = new ArrayList<>();
        List<Bin> sn;
        List<Bin> sBest;

        //System.out.println("Simulated annealing with\nkmax\t"+kmax+"\tt\t"+t);
        //System.out.println("Initsol\t"+initSol+"\talpha\t"+alpha);

        long e, emax = 0;
        long best;
        long en;
        int next_taken = 0, best_updated = 0;

        int k = 0;
        int i = 1;

        int lowerBound = L2(items, c);

        switch (initSol) {
            case 1: {
                s = Heuristics.FFD(this.items, n, c);
         //       System.out.println("initialized with FFD");
                break;
            }
            case 2:{
                s = Heuristics.BFD(this.items, n, c);
          //      System.out.println("initialized with BFD");
                break;
            }
            default:{
                s = Heuristics.NFD(this.items, n, c);
           //     System.out.println("initialized with NFD");
                break;
            }
        }

        if(s.size() == lowerBound)
            return s;
        e = energy(s);
        best = e;
        sBest = s;

        while(k < kmax){
            sn = voisin(s, alpha);
            en = energy(sn);

            if((en > e) || (Math.random() < Math.exp((en - e)/t))){
                s = sn;
                e = en;
                if( e > best){
                    best = e;
                    sBest = s;
                    if(sBest.size() == lowerBound)
                        return sBest;
                }
            }
            t = cool(t);
            k++;
        }
        return sBest;
    }

    /* NFD */
    public List<Bin> NFD(List<Integer> weight, int n, int c)
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
        return s;
    }

    /* BFD */
    public List<Bin> BFD(List<Integer> weight, int n, int c)
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
            if (min==c+1)
            {
                b=new Bin();
                b.getList().add(w);
                bins.add(b);
                bin_rem.add(c - w);
            }
            else
            {
                bins.get(bi).getList().add(w);
                bin_rem.set(bi, min);

            }
        }
        return bins;
       // this.opt.getBins().addAll(bins);
      //  return bin_rem.size();
    }

    /* FFD */
    public List<Bin> FFD(List<Integer> weight, int n, int c) {
        System.out.println("printing parameters :\nN = " + n + "\tC = " + c);
        int res = 0;
        List<Bin> bins = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int item = weight.get(i);
            int j;

            for (j = 0; j < res; j++) {
                int left = bins.get(j).getSpaceLeft();
                if (item <= left) {
                    bins.get(j).addItem(item);
                    break;
                }
            }

            if (j == res) {
                Bin bin = new Bin();
                bin.addItem(item);
                bins.add(bin);
                res++;
            }
        }

        return bins;
    }

    public List<Bin> voisin(List<Bin> s, double intensificationFactor){

        if (Math.random() > intensificationFactor)
            return voisin_1(s);

        List<Bin> v = new ArrayList<>();
        int current_index = 0;
        int size = s.size();
        for (Bin b : s){
            v.add(Bin.copy(b));
        }
        Collections.sort(v, new SortBin());

        while(current_index < size - 1){
            Bin bin = v.get(current_index);
            int index_element = bin.size() - 1;
            int element = bin.getItem(index_element);
            if( element <= v.get(current_index + 1).getSpaceLeft()){
                /** Search for Best Fit Bin **/
                int i = current_index + 1;
                while( (i + 1 < size ) && (element <= v.get(i + 1).getSpaceLeft())){
                    i++;
                }
                swap0(bin, v.get(i), index_element);
                if (bin.isEmpty())
                    v.remove(bin);
                return v;
            }
            else{
                for(int i = size - 1; i > current_index; i--){

                    Bin binb = v.get(i);
                    for( int j = 1; j < binb.size(); j++){
                        if( element > binb.getItem(j)
                                && ((binb.getSpaceLeft() + binb.getItem(j) - element) >= 0)
                                && (bin.getSpaceLeft() + element - binb.getItem(j) >= 0)){
                            swap1(bin, binb, index_element, j);
                            return v;
                    }
                }
            }
        }
        current_index++;
    }
        return voisin_1(s);
    }

    /**
     * Méthode de génération du voisin aléatoire
     * @param s : élément dont on cherche le voisin
     * @return
     */

    public List<Bin> voisin_1(List<Bin> s) {
        List<Bin> v = new ArrayList<>();
        int binListSize = s.size();
        boolean found = false;

        for (Bin b : s){
            v.add(Bin.copy(b));
        }
        /**
         * generate a neighbor of s
         */

        while( ! found) {
            int n = random(0,binListSize);
            int m = random(0,binListSize);
            Bin binA = v.get(n);
            Bin binB;

            if(n == m)
                binB = v.get((n+1) % binListSize);
            else
                binB = v.get(m);

            int iA = random(0, binA.size());
            int objectA = binA.getList().get(iA);

            if( objectA <= binB.spaceleft()) {
                swap0(binA, binB, iA);
                if( binA.isEmpty())
                    v.remove(binA);
                return v;
            }
            else {
                int iB = 0;
                while ( iB < binB.getList().size()){
                    if( binB.getSpaceLeft() + binB.getList().get(iB) - binA.getList().get(iA) >= 0
                            && (binA.getSpaceLeft() + binA.getList().get(iA) - binB.getList().get(iB) >= 0)){
                        swap1(binA, binB, iA, iB);
                        return v;
                    }
                iB++;
                }
            }

        }

        return v;
    }

    private void swap1(Bin binA, Bin binB, int iA, int iB) {

        int a = binA.removeItem(iA);
        int b = binB.removeItem(iB);

        binA.addItem(b);
        binB.addItem(a);
    }


    private void swap0(Bin binA, Bin binB, int iA) {
        binB.addItem(binA.removeItem(iA));
    }

    private int random(int min, int max){

        return ( (int) Math.floor(Math.random() * (max - min) + min));
    }

    public double cool(double t){
        return (0.99 * t);
    }

    public long energy(List<Bin> s){
        return ( - s.size());
    }

    /// ldalpha utilisés par L2 de MTP   //
    public  int ldalpha(int a,int c,List <Integer> list)
    {
        int  j1=0; int sommej2=0;
        int  j2=0;
        int  sommej3=0;
        for(int w : list)
        {
            if(w>(c-a))	j1++;
            if(w<=(c-a) && w > c/2)
            { j2++; sommej2=sommej2+w;}
            if(w<=c/2 && w>= a) sommej3=sommej3+w;
        }
        int s=(sommej3 -((j2*c)-sommej2));
        return j1+j2+Math.max(0, (int)Math.ceil(s/(float)c));

    }

    /***** L2 de MPT ***/

    public int L2(List<Integer> objets , int c) {
        int max=-1; int l;
        Set<Integer> v=new HashSet<Integer>();
        for(int w:objets)
        {
            if(w<=c/2) v.add(w);
        }
        if(v.isEmpty()) return objets.size();
        else {
            for (int w :v)
            {
                l=ldalpha(w,c,objets);
                if(l>max) max=l;
            }
            return max ;
        }

    }

}
