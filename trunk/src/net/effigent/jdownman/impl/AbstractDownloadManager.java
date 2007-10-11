/**
 * 
 */
package net.effigent.jdownman.impl;

import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadListener;
import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;

/**
 * @author vipul
 *
 */
public abstract class AbstractDownloadManager implements DownloadManager {



	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 */
	public void downloadFile(File destinationFile, URL[] urls,PRIORITY priority)
			throws DownloadException {
		downloadFile(destinationFile,urls,null,null,-1,priority);
	}

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile, URL[] urls,
			DownloadListener listener) throws DownloadException {
		downloadFile(destinationFile,urls,listener,null,-1,PRIORITY.NORMAL);
	}

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param checksum
	 * @param length
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile, URL[] urls, Object checksum,
			long length) throws DownloadException {
		downloadFile(destinationFile,urls,null,checksum,length,PRIORITY.NORMAL);
	}


}
