/**
 * 
 */
package net.effigent.jdownman.work;

import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;


/**
 * @author vipul
 *
 */
public interface WorkManager extends Runnable{

	/**
	 * 
	 * @param download
	 * @throws DownloadException
	 */
	public void download(ChunkDownload download) throws DownloadException;
	
}
