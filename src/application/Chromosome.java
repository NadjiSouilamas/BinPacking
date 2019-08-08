package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Chromosome implements Comparable<Chromosome> {
	
	private List<Bin> bins=new ArrayList<Bin>(); 
	private List<Integer> freeItems=new ArrayList<Integer>();
	private double fitness; 
	

	
	public double calculate_fitness()
	{   double f,c,sum=0;
	    int k=2;
		
		for(Bin b: bins)
		{
		  f=Bin.c-b.spaceleft();
		  sum = sum + Math.pow(f/Bin.c, k);
		}
		f=sum/bins.size();
		this.fitness=f;
		return (f);
		
	}
	
	

	public void setFitness(){
		
	}
	
	public Double getFitness(){
		return this.fitness;
		
		
	}
	
	public int nbBins()
	{
		return this.bins.size(); 
		
	}
	
	public static void shuffle(List<Integer> l)
	{
		long seed=System.nanoTime();
		Collections.shuffle(l, new Random(seed));
	}
	
	
	
	
	public void InsertRandom(List<Integer> objets)
	{
		shuffle(objets); 
		ff(objets); 
	}
	
	public void addBin(Bin b)
	{
		this.bins.add(b);
	}

	private void ff(List<Integer> list)
	{   
		int k=0;
		for(int i:list)
		{
			for ( k=0; k < this.bins.size(); k++)
			{
				if(i <= bins.get(k).spaceleft())
				{
					bins.get(k).getList().add(i); 
					break; 
				}
			}
			
			if(k==bins.size())
			{
				Bin b=new Bin(); 
				b.getList().add(i); 
				this.bins.add(b); 
			}
		}
	}


	@Override
	public int compareTo(Chromosome o) {
		
		double f1=this.fitness; 
		double f2=o.fitness;
		/*if(f1>=f2) return -1;
		else*/
		return Double.compare(f1, f2)*(-1);
	}
	
	public void showChromosome()
	{   int j=1; 
		for(Bin b:this.bins)
		{ System.out.println("Bin"+j);
			b.afficher();
			j++;
		}
	}
	
	
	public Pair getCrossoverPoints(){
		Pair pair = new Pair();
		int len = this.bins.size();

		if(len == 0)
			return null;
		
		pair.x = randRange(2, len - 3);
		pair.y = randRange(pair.x+1, len - 1); 
		
		/*if(pair.x == (len - 1 )){
			pair.y = len;
		} else {
			pair.y = randRange(pair.x, pair.x + delta); 
		}*/
		
		return pair;
	}
	
  public static int randRange(int min, int max){
		
		Random rn = new Random();
		int range = max - min + 1;
		int randomNum = rn.nextInt(range) + min;
		
		return randomNum;
	}
  
  public List<Bin> getGenesByDivision(Pair div) {
		List<Bin> genes = new ArrayList<Bin>();
		
		for(int i = div.x; i < div.y; i++){
			Bin b=new Bin();
			b.getList().addAll(this.bins.get(i).getList());
			genes.add(b);
		}
		
		return genes;
	}
  
	public Pair getCrossoverPoints(int delta){
		Pair pair = new Pair();
		int len = this.bins.size();

		if(len == 0)
			return null;
		
		pair.x = randRange(0, len - 1);
		
		if(pair.x == (len - 1 )){
			pair.y = len;
		} else {
			pair.y = randRange(pair.x, pair.x + delta); 
		}
		
		return pair;
	}
	
	public void deleteDuplicatesByGenes(List<Bin> newGenes) {
		
		for(int i = 0; i < this.bins.size(); i++){
			for(int j = 0; j < newGenes.size(); j++){
				for(int n = 0; n < newGenes.get(j).getList().size(); n++){
					
					for(int m = 0; m < this.bins.get(i).getList().size(); m++){
						
						if(this.bins.get(i).getItem(m) == newGenes.get(j).getItem(n)){
							this.bins.get(i).getList().remove(m);
							this.bins.get(i).markForDelete();
							break;
						}
						
					}
					
				}
			}
		}
		
		List<Bin> newBins = new ArrayList<Bin>();
		
		for(int i = 0; i < this.bins.size(); i++){
			if(this.bins.get(i).toDelete()){
				
				List<Integer> items = this.bins.get(i).getList();
				
				for(int k = 0; k < items.size(); k++){
					this.freeItems.add(items.get(k));
				}
				
			} else {
				newBins.add(this.bins.get(i));
			}
		}
		
		this.bins = newBins;
	}
	
	public void insertGenesOnPos(List<Bin> genes, int y) {
		if(y >= this.bins.size()){
			y = this.bins.size();
		}
		
		this.bins.addAll(y, genes);
	}
	
	public void addFreeItems()
	{
		Collections.sort(this.freeItems, Collections.reverseOrder());
		ff(this.freeItems);
		this.freeItems.clear();
		
	}
	
	public void showFreeItems()
 	{
      System.out.print("Free Items : ");
      for(int k:this.freeItems)
      {
          System.out.print(k+" |");   	  
      }
      System.out.println("");
	}
	
	public void mutate(int probabilty, int mutationSize) {
		Random randGen = new Random();
		int test = randGen.nextInt(100);
		this.freeItems.clear();
		if(test > probabilty)
			return;
		
		//mutation
		if(bins.size() < mutationSize){
			
			for(int i = 0; i < bins.size(); i++){
				List<Integer> items = bins.get(i).getList();
				
				for(int k = 0; k < items.size(); k++){
					this.freeItems.add(items.get(k));
				}
				
			}
			this.bins.clear();
			addFreeItems();
			return;
		}
		
		for(int i = 0; i < mutationSize; i++){
			int pos = randGen.nextInt(bins.size());
			List<Integer> items = bins.get(pos).getList();
			
			for(int k = 0; k < items.size(); k++){
				this.freeItems.add(items.get(k));
			}
			this.bins.remove(pos);
		}
		
		addFreeItems();
	
	
}
}
