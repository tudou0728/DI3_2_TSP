package polytech.tours.di.parallel.tsp.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import polytech.tours.di.parallel.tsp.Algorithm;
import polytech.tours.di.parallel.tsp.PISolution;
//import polytech.tours.di.parallel.tsp.Instance;
//import polytech.tours.di.parallel.tsp.InstanceReader;
import polytech.tours.di.parallel.tsp.Solution;
//import polytech.tours.di.parallel.tsp.TSPCostCalculator;

/**
 * Implements an example in which we read an instance from a file and print out some of the distances in the distance matrix.
 * Then we generate a random solution and computer its objective function. Finally, we print the solution to the output console.
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class ExampleAlgorithm implements Algorithm 
{

	@Override
	public Solution run(int nbThreads, int nbTasks,Properties config) throws ExecutionException 
	{
		ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
		List<Future<Solution>> results = null;
		List<Callable<Solution>> tasks=new ArrayList<Callable<Solution>>();	
		for(int t=1; t<=nbTasks; t++)
		{
			tasks.add(new PISolution(config)); 
		}
		try 
		{
			results=executor.invokeAll(tasks); 
			executor.shutdown();
		} 
		catch (InterruptedException e) 
		{
			System.out.println("error1");
		}
		try 
		{
			Solution best=new Solution();		
			try {
				best=best.generateRandomSolution(config);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			try {
				for(Future<Solution> t:results)
				{
					if(t.get().getOF()< best.getOF())
					{
						best=t.get().clone();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return best;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
