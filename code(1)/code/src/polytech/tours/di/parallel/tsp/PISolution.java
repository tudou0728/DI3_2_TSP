package polytech.tours.di.parallel.tsp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;

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
public class PISolution extends Solution implements Callable<Solution>
{
	public PISolution(Properties config)
	{
		this.config=config;
	}
	
	public Solution call() throws Exception 
	{
		InstanceReader ir=new InstanceReader();
		ir.buildInstance(this.config.getProperty("instance"));
		Instance instance=ir.getInstance();
		Random rnd=new Random();
		Solution best=new Solution();
		for(int j=0; j<instance.getN(); j++)
		{
			best.add(j);
		}
		Collections.shuffle(best,rnd); 
		//set the objective function of the solution
		best.setOF(TSPCostCalculator.calcOF(instance.getDistanceMatrix(), best));
		long max_cpu=Long.valueOf(config.getProperty("maxcpu"));
		long startTime=System.currentTimeMillis();
		//for(int time=0;time<=max_cpu;time++)
		while((System.currentTimeMillis()-startTime)/1_000<=max_cpu)
		{
			Solution temp=generateRandomSolution();
			Solution local=temp.localSearch(temp,instance);
			if(local.getOF()<temp.getOF())
			{
				best=local.clone();
			}
		}
		return best;
	}
}
