package application;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Generation {
	
	private List<Chromosome> population=new ArrayList<Chromosome>(); 
	private List<Double> fitnessArr=new ArrayList<Double>();
	private double bestFitOnGeneration=0;
	private int bestBinsCount=1000000; 
    static int selection_size; 
    static int delta=5;
    static int mutation_size; 
    static int mutation_proba; 
    static int population_size; 
	
    /***** initiate the first population ******/	
	public static Generation initFirstGeneration(List<Integer> objets)
	{
		Generation gen=new Generation();
		for (int i=0;i < population_size;i++)
		{
			Chromosome c=new Chromosome(); 
			c.InsertRandom(objets);
			gen.addChromosome(c);
			
		}
		return gen; 
	}
	
	public static void setSelectionSize(int size)
	{
		selection_size=size; 
	}
	
	public static int getSelectionSize()
	{
		return selection_size; 
	}
	
	
     	
     /******* count the fitness of each chromosome ******/	
	public  void countFistness()
	{   
		double f=0;
		for(Chromosome c:this.population)
		{
			f=c.calculate_fitness();
			fitnessArr.add(f);
			if(c.nbBins()<this.bestBinsCount)
			{
				this.bestFitOnGeneration=f;
				bestBinsCount=c.nbBins();
			}
		}
	}
	
	public int getBestBinsCount()
	{
		return this.bestBinsCount; 
	}
	
	
	public void setBestBinsCount(int b)
	{
	 this.bestBinsCount=b; 
	}
	
     /***** add a chromosome to the list ******/ 	
	public void addChromosome(Chromosome c)
	{
		this.population.add(c); 
		
	}
	
    /******* select the parents  ****/ 
	
	/*public void selection()
	{
	    int limit =50; 
	    Collections.sort(this.population);
	    if(this.population.size()>50)
	    {
	    	this.population.subList(0, limit); 
	    }
	}*/
   
	private  List<Chromosome> selection()
	{
		Collections.sort(this.population);
	    List<Chromosome> parents=new ArrayList<Chromosome>();
	    double sum=0; 
    	for(Chromosome c:this.population)
    	{
    		sum = sum + c.getFitness();
    	}
    	
	    for (int i=0;i<selection_size;i++)
	    {
	    	double rand = Math.random();
	    	rand *= sum;
	    	double psum = 0; 
	    	for(Chromosome c:this.population)
	    	{
	    		psum +=c.getFitness(); 
	    		if(psum > rand)
	    		{
	    			parents.add(c); 
	    			break; 
	    		}
	    	}
	    }
	    return parents;
	}
	
	
	public void generateNextGeneration()
	{
	List<Chromosome> newGeneration=new ArrayList<Chromosome>();
	List<Chromosome> parents=new ArrayList<Chromosome>();
	parents =this.selection();
	
  
    
	newGeneration.addAll(parents); 
	long seed=System.nanoTime();
	Collections.shuffle(parents, new Random(seed));
	/*for(Chromosome c : parents)
	{
		c.showChromosome();
	}*/
	int i=1;
    while(parents.isEmpty())
	{
	    Chromosome c1=parents.remove(0); 
	    Chromosome c2=parents.remove(0);
	    System.out.println("Parents size == "+parents.size());
	    this.crossOver(c1,c2);
	    newGeneration.add(c1);
	    newGeneration.add(c2);
	  
	    System.out.println("New generation size == "+newGeneration.size());
	    
	}
    this.population=newGeneration;
	}
	
	public void crossOver(Chromosome chr1,Chromosome chr2)
	{
		Pair crPointsChr1 = chr1.getCrossoverPoints(delta);
		Pair crPointsChr2 = chr2.getCrossoverPoints(delta);
		
	
		System.out.println("crossover points chr1 x= "+crPointsChr1.x +" y= "+crPointsChr1.y); 
		System.out.println("crossover points chr2 x= "+crPointsChr2.x +" y= "+crPointsChr2.y); 
		if(crPointsChr1 != null && crPointsChr2 != null){
			List<Bin> genesFromChr1 = chr1.getGenesByDivision(crPointsChr1);
			List<Bin> genesFromChr2 = chr2.getGenesByDivision(crPointsChr2);
			
			System.out.println("GENES FROM CHR1");
			showBins(genesFromChr1); 
			
			System.out.println("GENES FROM CHR2");
			showBins(genesFromChr2);
			
			chr1.deleteDuplicatesByGenes(genesFromChr2);
			chr2.deleteDuplicatesByGenes(genesFromChr1);
			
			System.out.println("CHR1 AFTER DELETING DUBLICATES");
			chr1.showChromosome();
			System.out.println("CHR2 AFTER DELETING DUBLICATES");
			chr2.showChromosome();
			
			chr1.insertGenesOnPos(genesFromChr2,crPointsChr1.y);
			chr2.insertGenesOnPos(genesFromChr1,crPointsChr2.x);
			
			chr1.addFreeItems();
			chr2.addFreeItems();
		
		}
		
	
	}
	
	public void addMutations() {
		for (int i = 0; i < this.population.size(); i++) {
			this.population.get(i).mutate(mutation_proba, mutation_size);
		}
	}
	
	public void showPopulation()
	{   int i=1;
		for (Chromosome c:this.population)
		{
			System.out.println("Chromosome "+i);
			c.showChromosome();
			i++;
		}
	}
	
    public void showChrNbBins()
    {
    	int i=1;
 		for (Chromosome c:this.population)
 		{
 			System.out.println("Chromosome \t"+i +": \t"+"Nb Bins :\t"+c.nbBins()+" fitness \t"+new DecimalFormat("##.##").format(c.getFitness()));
 			
 			i++;
 		}
    }
    

    public Chromosome getChromosome (int i)
    {
    	return this.population.get(i);
    }
    
    public void showBins(List<Bin> l)
    {
    	for(Bin b:l)
    	{
    		b.afficher();
    	}
    }

	public static int getSelection_size() {
		return selection_size;
	}

	public static void setSelection_size(int selection_size) {
		Generation.selection_size = selection_size;
	}

	public static int getDelta() {
		return delta;
	}

	public static void setDelta(int delta) {
		Generation.delta = delta;
	}

	public static int getMutation_size() {
		return mutation_size;
	}

	public static void setMutation_size(int mutation_size) {
		Generation.mutation_size = mutation_size;
	}

	public static int getMutation_proba() {
		return mutation_proba;
	}

	public static void setMutation_proba(int mutation_proba) {
		Generation.mutation_proba = mutation_proba;
	}

	public static int getPopulation_size() {
		return population_size;
	}

	public static void setPopulation_size(int population_size) {
		Generation.population_size = population_size;
	}
	

   



}
