/**
 * 
 */
package net.effigent.jdownman.queue.store;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.queue.DownloadQueue;
import net.effigent.jdownman.util.InitializationException;

/**
 * @author vipul
 *
 */
public interface PersistenceAdapter {

	/**
	 * @throws InitializationException 
	 * 
	 *
	 */
	void initialize(DownloadQueue queue) throws InitializationException;
	
	
	/**
	 * 
	 */
	void setBinder(Binder binder);
	
	/**
	 * 
	 * @param download
	 */
	void persistDownload(Download download)  throws Exception;
	/**
	 * 
	 * @param chunk
	 */
	void markChunkDownloadComplete(ChunkDownload chunk)  throws Exception;
	/**
	 * 
	 * @param chunk
	 */
	void markChunkDownloadStarted(ChunkDownload chunk)  throws Exception;
	/**
	 * 
	 * @param download
	 */
	void markDownloadCancelled(Download download)  throws Exception;
	/**
	 * 
	 * @param download
	 */
	void markDownloadComplete(Download download)  throws Exception;
	/**
	 * 
	 * @param download
	 */
	void markDownloadInterrupted(Download download)  throws Exception;
	/**
	 * 
	 * @param download
	 * @param totalFileLength
	 */
	void saveDownloadSize(Download download, long totalFileLength)  throws Exception;
	/**
	 * 
	 * @param download
	 * @param chunkCount
	 * @param chunkSize
	 */
	void markDownloadSplit(Download download, int chunkCount, long chunkSize)  throws Exception;
	/**
	 * 
	 * @param download
	 */
	void markDownloadStarted(Download download)  throws Exception;
	/**
	 * 
	 * @param download
	 * @param e
	 */
	void registerExceptionForDownload(Download download, DownloadException e)  throws Exception;
	
	

}
