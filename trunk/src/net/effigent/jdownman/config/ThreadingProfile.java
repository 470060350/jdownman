/**
 * 
 */
package net.effigent.jdownman.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.effigent.jdownman.util.ChunkDownloadComparator;
import net.effigent.jdownman.util.DownloadNamedThreadFactory;

/**
 * @author vipul
 *
 */
public class ThreadingProfile {
	
	/**
	 * 
	 */
    private ThreadFactory threadFactory;
    /**
     * 
     */
    private int maxThreadsActive = 10;
    /**
     * 
     */
    private int maxThreadsIdle = 5;
    
    /**
     * 
     */
    private int threadTTL = 30000;

    

	/**
	 * 
	 */
	public ThreadingProfile() {}

	
	/**
	 * This method creates a thread pool
	 * 
	 * @param name
	 * @return
	 */
	public ThreadPoolExecutor createPool() {
	
		/* the line below doesn't work .. why? because of the ThreadPoolExecutor design that accepts a BlockingQueue<Runnable>
		 * and not BlockingQueue<? extends Runnable>
		 * This is forcing me to change the ChunkDownloadComparator to be NOT specific to ChunkDownaload but to ANY Runnable
		*/
//		BlockingQueue<ChunkDownload> chunks = new PriorityBlockingQueue<ChunkDownload>(10,new ChunkDownloadComparator());

											//an unbounded queue..
		BlockingQueue<Runnable> chunkQueue = new PriorityBlockingQueue<Runnable>(10,new ChunkDownloadComparator());
		
		// create a new ThreadPool
        ThreadPoolExecutor pool = new ThreadPoolExecutor(maxThreadsIdle, 
        												maxThreadsActive, 
        												threadTTL,
        												TimeUnit.MILLISECONDS, 
        												chunkQueue);

        ThreadFactory tf = threadFactory;
        //if thread factory has not been set ... set the default one (DownloadNamed)
        if(tf == null) {
        	tf = new DownloadNamedThreadFactory();
        }
        pool.setThreadFactory(tf);
        
        
		return pool;
	
	}
	


	/**
	 * @return the maxThreadsActive
	 */
	public int getMaxThreadsActive() {
		return maxThreadsActive;
	}



	/**
	 * @param maxThreadsActive the maxThreadsActive to set
	 */
	public void setMaxThreadsActive(int maxThreadsActive) {
		this.maxThreadsActive = maxThreadsActive;
	}



	/**
	 * @return the maxThreadsIdle
	 */
	public int getMaxThreadsIdle() {
		return maxThreadsIdle;
	}



	/**
	 * @param maxThreadsIdle the maxThreadsIdle to set
	 */
	public void setMaxThreadsIdle(int maxThreadsIdle) {
		this.maxThreadsIdle = maxThreadsIdle;
	}



	/**
	 * @return the threadFactory
	 */
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}



	/**
	 * @param threadFactory the threadFactory to set
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}


	/**
	 * @return the threadTTL
	 */
	public int getThreadTTL() {
		return threadTTL;
	}


	/**
	 * @param threadTTL the threadTTL to set
	 */
	public void setThreadTTL(int threadTTL) {
		this.threadTTL = threadTTL;
	}

	
	
	
}
