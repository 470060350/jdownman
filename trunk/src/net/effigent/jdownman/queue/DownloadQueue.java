/**
 * 
 */
package net.effigent.jdownman.queue;


import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.util.InitializationException;

/**
 * This interface declares the operations 
 * of the Queue that would hold the downloads
 * in certain order.
 * 
 * Implementations of this interface may need to
 * be backed up by a persistance layer that keeps  
 * 
 * 
 * @author vipul
 *
 */
public interface DownloadQueue {
	
	/**
	 * @throws InitializationException 
	 * 
	 *
	 */
	public void initialize() throws InitializationException;
	
	/**
	 * 
	 * @param download
	 * @throws DownloadException
	 */
	public void enqueueDownload(Download download) throws DownloadException;
	
	/**
	 * 
	 * @return
	 * @throws DownloadException
	 */
	public Download dequeueDownload() throws DownloadException;

}
