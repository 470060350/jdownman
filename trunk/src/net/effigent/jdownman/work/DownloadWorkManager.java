/**
 * 
 */
package net.effigent.jdownman.work;

import java.util.List;
import java.util.concurrent.ExecutorService;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.config.ThreadingProfile;
import net.effigent.jdownman.queue.DownloadQueue;

import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class DownloadWorkManager implements WorkManager {

	
	private static final Logger logger = Logger.getLogger(DownloadWorkManager.class);
	
	/**
	 * threading profile for the download manager
	 */
	private ThreadingProfile threadingProfile = null;
	
	/**
	 * 
	 */
	ExecutorService executorService = null;
	
	/**
	 * 
	 */
	DownloadQueue downloadQueue = null;
	
	
	/**
	 * 
	 */
	public DownloadWorkManager() {	}

	/**
	 * 
	 */
	public DownloadWorkManager(ThreadingProfile threadingProfile) {
		this.threadingProfile = threadingProfile;
	}

	/**
	 * 
	 * @throws UMOException
	 */
    public synchronized void start() throws DownloadException
    {
        if (executorService == null)
        {
            executorService = threadingProfile.createPool();
        }
		System.out.println(Thread.currentThread().getName()+" - Starting Download WOrk Manager");

        executorService.execute(this);
    }

    /**
     * 
     */
	public void run() {
		//run ... and keep running ..
		System.out.println(Thread.currentThread().getName()+" - Running Download WOrk Manager");
		while(true) {
			
			try {
				Download download = downloadQueue.dequeueDownload();
				System.out.println(Thread.currentThread().getName()+" - Got Download "+download);
				
				List<ChunkDownload> chunks = download.getChunks();
				for(ChunkDownload chunk :  chunks ) {
					download(chunk);
				}
			} catch (DownloadException e) {
				logger.error("UNable to dequeue from the queue "+e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
    /**
     * @see WorkManager#download(ChunkDownload)
     */
    public void download(ChunkDownload download) throws DownloadException
    {
        if (executorService == null || executorService.isShutdown())
        {
            throw new IllegalStateException("This MuleWorkManager is stopped");
        }

        executorService.execute(download);
    }

	/**
	 * @return the downloadQueue
	 */
	public DownloadQueue getDownloadQueue() {
		return downloadQueue;
	}

	/**
	 * @param downloadQueue the downloadQueue to set
	 */
	public void setDownloadQueue(DownloadQueue downloadQueue) {
		this.downloadQueue = downloadQueue;
	}

	/**
	 * @return the threadingProfile
	 */
	public ThreadingProfile getThreadingProfile() {
		return threadingProfile;
	}

	/**
	 * @param threadingProfile the threadingProfile to set
	 */
	public void setThreadingProfile(ThreadingProfile threadingProfile) {
		this.threadingProfile = threadingProfile;
	}

	/**
	 * @return the executorService
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * @param executorService the executorService to set
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
