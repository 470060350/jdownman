/**
 * 
 */
package net.effigent.jdownman.queue;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.util.DownloadComparator;

import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class NonPersistentDownloadQueue implements DownloadQueue {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(NonPersistentDownloadQueue.class);
	
	/**
	 * Priority Queue to line up the downloads for processing
	 * in the order of Priority set for each of the - ordered
	 * further by the time of creation (DownloadComparator)
	 */
	BlockingQueue<Download> downloads = new PriorityBlockingQueue<Download>(10,new DownloadComparator());

	

	public void init() {
		// don't have much to initialize in this version
		
	}
	
	/**
	 * 
	 */
	public Download dequeueDownload() throws DownloadException {
		try {
			return downloads.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new DownloadException("Unable to denqueue the download : "+e.getMessage(),e);
		}
	}

	/**
	 * 
	 */
	public void enqueueDownload(Download download) throws DownloadException {
		try {
			downloads.put(download);
		} catch (InterruptedException e) {
			//this won't happen as we are using PriorityBlockingQueue that does not block at all
			e.printStackTrace();
			throw new DownloadException("Unable to enqueue the download : "+e.getMessage(),e);

		}
	}

 


}
