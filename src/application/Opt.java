package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Opt {

    private List<Integer> orgList = new ArrayList<>();
    private int c;
    private int lb, bfd;
	private List<Integer> objets ;
    private Node opt;
    private int createdNodes = 1;
    private int exploredNodes = 1;


    public Opt() {
		this.objets =new ArrayList<>();
		this.objets.add(49); this.objets.add(41);
		this.objets.add(34); this.objets.add(33);
		this.objets.add(29); this.objets.add(26);
		this.objets.add(26); this.objets.add(22);
		this.objets.add(20);  this.objets.add(19);
	}

    public int getCreatedNodes() {
        return createdNodes;
    }

    public int getExploredNodes() {
        return exploredNodes;
    }

    public int BFD(List<Integer> weight, int n, int c)
    {
        int res = 0;
        List<Bin> bins = new ArrayList<>();
        this.opt = new Node(weight);
        List<Integer> bin_rem=new ArrayList<>();
        int min,bi=0,rem=0; Bin b=new Bin();
        // Place items one by one
        for (int i=0; i<n; i++)
        {
            min = c+1;

            int w= weight.get(i);
            for (int j=0; j<bin_rem.size(); j++)
            {
                rem =bin_rem.get(j);

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
        this.opt.getBins().addAll(bins);
        return bin_rem.size();
    }

    public int getBfd() {
        return bfd;
    }

    public int getLb() {
        return lb;
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
		Set <Integer> v=new HashSet<Integer>();
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

	/** wasted lower Bound de Korf */
    public int L2(List<Integer> items){

        int wastedSpace = 0;
        int leftSpace, n, sumSmaller = 0, unitsLeft, sumElements = 0, result;
        /**
         Phase d'initialisation
         * */
        int i = 0;

        /**
         * Sum of the Elements
         */
        for (int j = 0; j < items.size() ; j++) {
            sumElements += items.get(j);
        }

        /**
         * Calculating Wasted Space
         */
        while (!items.isEmpty()) {
            sumSmaller = 0;
            Collections.sort(items);
            n = items.size() - 1;
            leftSpace = this.c - items.get(n);
            items.remove(n);

            while ((!items.isEmpty()) && (items.get(0) <= leftSpace)) {
                sumSmaller += items.remove(0);
            }

            if (leftSpace - sumSmaller >= 0)
                wastedSpace += leftSpace - sumSmaller;

            else {
                unitsLeft = (sumSmaller - leftSpace ) % this.c;
                if (unitsLeft > 0)
                    items.add(unitsLeft);
            }
            i++;
        }


        result = ( sumElements + wastedSpace ) / this.c;
        return result;

    }
 
    /**
    *
    * @param spaceLeft : BinCapacity - x
    * @param yIndex : Index of the larger item that can fit inside the spaceLeft
    * @param objets : Sorted original list of items ( sorted in INCREASING order )
    */
   public void undominated_pairs (int spaceLeft,int yIndex, List<Integer> objets)
   {
       Pairs pairs = new Pairs(); /** How represent pairs ??? Will need our own class *_* */

       int i = 0;
       int j = yIndex;
       int y = objets.get(yIndex);
       int m,k;

       /** We can try an example with spaceLeft = 40 , Y = 36 and {36, 28, 28, 22, 18, 18, 5 }*/
       while(i < j)
       {
           if ( objets.get(i) + objets.get(j) <= y ){
               /** We must go to the next element greater than objets.get(i) */
               m = objets.get(i);
               while ( m == objets.get(i) ) i++;
           }
           else
           {
               if( objets.get(i) + objets.get(j) > spaceLeft){
                   /** We must go to the next element smaller than objets.get(j) */
                   k = objets.get(j);
                   while ( k == objets.get(j) ) j--;
               }
               else {
                   while ( (i + 1 != j) && ( objets.get(j) + objets.get(i+1) <= spaceLeft) ) i++;
                   pairs.add(objets.get(i),objets.get(j));
                   m = objets.get(i);
                   while ( m == objets.get(i) ) i++;
                   k = objets.get(j);
                   while ( k == objets.get(j) ) j--;
               }
           }

       }
       pairs.printPairs();
   }


   public Node getNode()
   {
	   return this.opt;
   }


   /**  VALIDATED */
   public boolean solveBP(Node n)
   {
       int i = 0;

	 List<Node> children = n.getChildren();
	 Collections.sort(children, new SortNode());

	 while( !children.isEmpty())
	 {
	     Node c = children.remove(0);
     //   exploredNodes ++;
		if(c.isleaf())
		{   if(c.nbBins() < this.opt.nbBins())
			{
				this.opt = c;
				if( c.nbBins() == lb) return true;
			}
		}
		else {
            if(c.eval() < this.opt.nbBins())
            {
              if (this.solveBP(c)) return true;
            }
		}
	 }
	 return false;
   }

   public List<Integer> getOrgList(){
       List<Integer> copy = new ArrayList<>();
       copy.addAll(this.orgList);
       return copy;
   }



   public void binPacking(List<Integer> items, int c) {
       this.orgList = items;
       this.c = c;
       Bin.setC(c);
       lb = L2(getOrgList(),Bin.c);
       bfd = BFD(getOrgList(),items.size(),c);
     //  System.out.println("BFD = "+bfd+"\nL2 = "+lb);
       if ( bfd == lb ){
           //System.out.println("Optimal found with BFD :\t" + opt.nbBins());
           return;
       }

       Node root = new Node(this.getOrgList());
       solveBP(root);

   }
}
