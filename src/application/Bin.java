package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bin {
	
	private List<Integer> list;
    public static int c;
	private int filled = 0;
	private boolean markedForDelete = false;

	public void markForDelete()
{
	this.markedForDelete=true;
}

	public boolean toDelete()
	{
		return this.markedForDelete;
	}

	public boolean addItem(int item){

		if(item <= c - filled){
			list.add(item);
			filled += item;
			return true;
		}
		else
			return false;
	}

	public int size(){
		return this.list.size();
	}

	public int getSpaceLeft(){
		return c - filled;
	}

	public static void setC(int c) {
		Bin.c = c;
	}

	public Bin() {
		this.list = new ArrayList<>();
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	} 
	
	public int spaceleft()
	{   int somme=0;
		for (int i :this.list)
		{
		  somme=somme+i;
		}
		return c-somme; 
	}
	
    public void afficher()
    {
    	for(int i :this.list)
    	{
    		System.out.print(Integer.toString(i) + " |");
    	}
    	System.out.println("");
    }

	public int getFilled() {
		return filled;
	}

	public void setFilled(int filled) {
		this.filled = filled;
	}

	public static Bin copy(Bin a){

		Bin b = new Bin();

		for (int item: a.getList()) {
			b.addItem(item);
		}
		return b;
	}

	public int getItem(int index){
		return list.get(index);
	}

	public boolean isEmpty(){
		return list.isEmpty();
	}

	public int removeItem(int index){
		int element = list.remove(index);
		filled -= element;
		return element;
	}


	public void print(){
		System.out.print("[ ");
		for ( int i: list
			 ) {
			System.out.print(i+", ");
		}
		System.out.print("Free = "+this.getSpaceLeft());
		System.out.println("]");
	}

	public void sortItems(){
		Collections.sort(this.list);
	}

	public int removeLast(){
		return this.list.remove(size() - 1);
	}

}
