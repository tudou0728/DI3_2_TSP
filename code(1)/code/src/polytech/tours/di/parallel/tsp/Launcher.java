package polytech.tours.di.parallel.tsp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import polytech.tours.di.parallel.tsp.example.ExampleAlgorithm;

/**
 * Launches the optimization algorithm
 * @author Jorge E. Mendoza (dev@jorge-mendoza.com)
 * @version %I%, %G%
 *
 */
public class Launcher {
	
	/**
	 * 
	 * @param args[0] the file (path included) with he configuration settings
	 */
	public static void main(String[] args) throws ExecutionException 
	{
		//read properties
		Properties config=new Properties();
		try 
		{
			config.loadFromXML(new FileInputStream(args[0])); 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		int tasks[]={10,20,50,100,500,1000};
		int threads[]={10,20,50,100,200,500,750,1000};
		
		//dynamically load the algorithm class
		ExampleAlgorithm algorithm=null;;
		try 
		{
			Class<?> c = Class.forName(config.getProperty("algorithm")); 
			algorithm=(ExampleAlgorithm)c.newInstance(); 
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		System.out.println("TASKS"+"\t"+"THREADS"+"\t"+"CPU"+"\t"+"solution");
		  for(int t=0; t<tasks.length; t++)
		  {
			try 
			{
				for(int p=0; p<threads.length; p++)
				{
					long start=System.currentTimeMillis(); 
					Solution s=algorithm.run(threads[p], tasks[t],config);
					long end=System.currentTimeMillis(); 
					System.out.println(tasks[t]+"\t"+threads[p]+"\t"+(end-start)/1000d+"\t"+s);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		  }
	}

}
