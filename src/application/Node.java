package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Node {

 // public static List<Integer> R=new ArrayList<>();
  private List<Bin> bins=new ArrayList<>();	
  private List<Integer> leftItems=new ArrayList<>();
  private List<Completion> comp =new ArrayList<Completion>() ;
  private List<Node> children =new ArrayList<Node>() ;
  private List<List<Integer>> Is = new ArrayList<>();
    /**
     *
     */
  private int r = 0;

  
  public Node(List<Integer> leftItems) {
	this.leftItems = leftItems;
  }

  public Node() {
	
  }

  public void setLeftItems(List<Integer> list)
  {
	 this.leftItems=list;  
  }

  public  List<Integer> getLeftItems()
  {
	return this.leftItems;  
  }

  public void addBin(Bin b)
  {
	  this.bins.add(b); 
  }

  public boolean isleaf()
  {
	  if(leftItems.isEmpty()) return true; 
	  else return false;
  }
  
  /*
   * this function generates all undominated completions 
   */
  public List<Node> getChildren()
  {   
	  List<Integer> I = new ArrayList<>();
	  List<Integer> E = new ArrayList<>();
      List<Integer> R = new ArrayList<>();

      R.addAll(leftItems);
      int y = R.remove(0); /** ADDED */
//	  int y = this.leftItems.get(0);
	  this.r = Bin.c - y;
//	  this.leftItems.remove(0);


      this.feasible(I, E, R, this.r,0);

	  /*int i=1;
	  System.out.println("-------------------AFFICHAGE DES CONTENU DES FILS ------------------");
	  for (Node n :this.children)
	  {   
		  System.out.println("FILS "+i+":"); i++; 
		  System.out.println("LIST OF BINS");
		  for (Bin bin:n.getBins())
		  {
			  bin.afficher();
		  }
		  System.out.println("LEFT ITEMS");
		  for (int j : (n.getLeftItems()))
		  {
			System.out.print(j+" |");  
		  }
		  System.out.println("");
	  }
	  System.out.println("--------------------END OF THE LEVEL--------------------------------");
	  */
	  return this.children;
  }
  
  
  public List<Bin> getBins() {
	return bins;
}

public void setBins(List<Bin> bins) {
	this.bins = bins;
}

public int nbBins()
  {
	  return this.bins.size(); 
  }
  /*
   * the eval function is the lower bound defined as the sum of 
   * remaining elements plus the leftspace in the bins completed 
   * so far, devided by the bin capacity and rounded up 
   */
  public int eval() {  int somme=0;
	 for(int i:this.leftItems)  
	 {
		 somme+=i;
	 }
	 for(Bin b: this.bins)
	 {
		 somme+=b.spaceleft(); 
	 }
	 
	  return (int)Math.ceil(somme/(float)Bin.c);  
  }



 /* public void setR()
  {
      R.add(99);
      R.add(2);

  }*/

  public void afficherComp(){
      int i = 1;
      for (Completion c: comp ) {
          System.out.println("Completion "+i);
          c.afficher();
          i++;
      }
  }

  private boolean test(List<Integer> i,List<Integer> e, int r){

      ArrayDeque queue = new ArrayDeque<Subset>();
      ArrayDeque children;
      Subset subsetI = new Subset(0,i);
      Subset s;
      int t = subsetI.sum();

      queue.add(subsetI);
      for (Integer x : e) {
          while( !queue.isEmpty()){
              s = (Subset) ((ArrayDeque) queue).remove();
              if (x - s.sum() <= r - t){
                  if( x > s.sum() )
                      return false;
                  if( (x == s.sum()) && (! s.isSingleton() ) )
                      return false;

              }
              children = s.getChildren();
              queue.addAll(children);
          }
      }
      return true;
  }

  private boolean isIn(List<List<Integer>> Is,List<Integer> I){


      for (List<Integer> a: Is ) {
          if(a.equals(I)) return true;
      }
      return false;
  }
  private void feasible(List<Integer> I,List<Integer>E,List<Integer>R,int u, int l) {
      int x;
      if(R.isEmpty()| u==0 ) {
          if (l == 0)
              if (!Is.contains(I) && test(I, E, this.r)){

                      Is.add(I);
                      //create a new node
                      Node n = new Node();
                      Bin b = new Bin();
                      //assign the list of bins of the parent node to the child
                      n.getBins().addAll(this.bins);
                      //add the Largest element first : y
                      b.getList().add(Bin.c - this.r);
                      //add the completion to the bin
                      b.getList().addAll(I);
                      n.getBins().add(b);
                      // something is not working here !
                      n.getLeftItems().addAll(leftItems);

                      //          System.out.println("Before Remove All :");
                      //          for (int j = 0; j < n.getLeftItems().size() ; j++) {
                      //              System.out.println("\t"+n.getLeftItems().get(j));
                      //         }
                      for (Integer a : b.getList()) {
                          n.getLeftItems().remove((Object) a);
                      }
                      //          n.getLeftItems().removeAll(b.getList());

                      //          System.out.println("After Remove All :");
                      //          for (int j = 0; j < n.getLeftItems().size() ; j++) {
                      //              System.out.println("\t"+n.getLeftItems().get(j));
                      //          }

                      /*for (Bin bin : n.getBins())
                      {
                          n.getLeftItems().removeAll(bin.getList());
                      }*/
                      // add the child node to the list of children
                      this.children.add(n);
              }
      }
      else {
          x=R.get(0);
          if(x>u)
          {
              R.remove(0);
              feasible(I,E,R,u,l);
          }
          else
          {
              if(x==u)
              {
                  I.add(x);
                  R.remove(0);
                  feasible(I,E,R,u-x,0);
              }
              else
              {
                  R.remove(0);
                  List <Integer> R1=new ArrayList<>();
                  List <Integer> I1=new ArrayList<>();
                  List <Integer> E2=new ArrayList<>();
                  R1.addAll(R); E2.addAll(E); E2.add(x);
                  I1.addAll(I); I1.add(x);
                  feasible(I1,E,R1,u-x,Math.max(0,l-x));
                  feasible(I,E2,R,u,Math.max(l,x+1));
              }

          }
      }

  }


    /***
     * From here is all I added
     */

    public void show(){
        int i = 0, n = 1;

        System.out.println("LEFT ITEMS");
        if(leftItems.isEmpty()) System.out.println("\tnothing");
        else
            for (Integer a: leftItems) {
                System.out.print("\t"+a);
                i++;
                if (i == 5){
                    System.out.println();
                    i = 0;
                }
            }
        System.out.println();
        System.out.println("NUMBER OF BINS\t"+bins.size());
        System.out.println();
        System.out.println("BINS CONTENT");
        for (Bin b: bins) {
            System.out.println("Bin "+n);
            b.afficher();
            n++;
        }
    }
}
  

