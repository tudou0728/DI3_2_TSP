package polytech.tours.di.parallel.tsp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;


/**
 * Concrete implementation of a solution represented by a single permutation of integers. This permutation becomes handy to represent
 * solutions to problems such as the traveling salesman problem, the knapsack problem, the flow shop scheduling problem, etc.
 * 
 * This concrete implementation is supported on an {@link ArrayList}.
 * 
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 * @since Jan 7, 2015
 *
 */
public class Solution extends ArrayList<Integer> implements Cloneable
{
	
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The objective function
	 */
	//protected BigDecimal of;
	protected double of=Double.NaN;
	
	//protected long runs;
	protected Properties config;
	public Solution(Properties config)
	{
		//this.runs=runs;
		this.config=config;
	}
	
	public Solution()
	{
		this.config=null;
	}
	@Override
	public Solution clone() {
		Solution clone=new Solution();
		clone.of=this.of;
		for(Integer i:this){
			clone.add(i);
		}
		return clone;
	}

	/**
	 * 
	 * @return the objective function of the solution
	 */
	public double getOF() {
		return this.of;
	}

	/**
	 * Sets the objective function of the solution
	 * @param of the objective to set
	 */
	public void setOF(double of) {
		this.of=of;		
	}
	
	@Override
	public String toString(){
		String str=super.toString();
		str=str.concat("\t OF="+this.of);
		return str;
	}
	
	@Override
	public void add(int index, Integer element){
		if(index==this.size())
			super.add(element);
		else
			super.add(index,element);
	}
	/**
	 * Relocates the element in position <code>i</code> at position <code>j</code>. The method only manipulates the
	 * permutation it does not updates the objective function or any other attribute of the encoded solution.
	 * @param i the extracting position
	 * @param j the inserting position
	 */
	public void relocate(int i, int j){
		if(i<j){
			this.add(j,this.get(i));
			this.remove(i);
		}else{
			this.add(j,this.remove(i));
		}
	}
	
	/**
	 * Swaps the element in position <code>i</code> and the element in position <code>j</code>. The method
	 * only manipulates the permutation; it does not up update the objective function or any other attribute of the encode solution.
	 * Client classes are responsible to update the attributes of the solution.
	 * @param i the first swapping position. 0 <= i< {@link #size()}
	 * @param j the second swapping position. 0 <= j< {@link #size()}
	 */
	public void swap(int i, int j){
		int temp=this.get(i);
		this.set(i,this.get(j));
		this.set(j, temp);
	}
	
	public Solution exploreNeighborhood(Solution s,Instance instance)
	{
		Solution neighborhood=s.clone();
		for(int i=0;i<s.size();i++)
		{
			for(int j=0;j<s.size();j++)
			{
				if(i != j)
				{
				   Solution temp=s.clone(); 
				   temp.swap(i, j);
				   temp.setOF(TSPCostCalculator.calcOF(instance.getDistanceMatrix(), temp));
				   if(temp.getOF()<s.getOF())
				   {
					   neighborhood=temp.clone();
				   }
				}
			}
		}
		return neighborhood;
	}
	
	public Solution localSearch(Solution s,Instance instance)
	{
		boolean boucle = true;
	    while(boucle)
		{
			Solution local=s.exploreNeighborhood(s,instance);
			if(local.getOF()<s.getOF())
			{
				s=local.clone();
			}
			else
			{
				boucle=false;
			}
		}
		return s;
	}
	
	public Solution generateRandomSolution(Properties config)
	{
		InstanceReader ir=new InstanceReader();
		ir.buildInstance(config.getProperty("instance"));
		Instance instance=ir.getInstance();
		Random rnd=new Random();
		Solution s=new Solution();
		for(int j=0; j<instance.getN(); j++)
		{
			s.add(j);
		}
		Collections.shuffle(s,rnd);
		s.setOF(TSPCostCalculator.calcOF(instance.getDistanceMatrix(), s));
		return s;
	}
   
	public Solution generateRandomSolution()
	{
		InstanceReader ir=new InstanceReader();
		ir.buildInstance(this.config.getProperty("instance"));
		Instance instance=ir.getInstance();
		Random rnd=new Random();
		Solution s=new Solution();
		for(int j=0; j<instance.getN(); j++)
		{
			s.add(j);
		}
		Collections.shuffle(s,rnd);
		s.setOF(TSPCostCalculator.calcOF(instance.getDistanceMatrix(), s));
		return s;
	}
}
