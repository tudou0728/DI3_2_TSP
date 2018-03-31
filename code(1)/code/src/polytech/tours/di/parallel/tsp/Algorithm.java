package polytech.tours.di.parallel.tsp;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Defines the interface to optimization algorithms
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public interface Algorithm {
	
	/**
	 * Runs the algorithm and returns the solution
	 * @param config the algorithm's configuration
	 * @return the solution to the problem
	 */
	public Solution run(int nbThreads, int nbTasks,Properties config) throws ExecutionException;
	
}
