/**
 * 
 */
package net.effigent.jdownman;

import java.io.File;
import java.net.URL;

import net.effigent.jdownman.Download.PRIORITY;



/**
 * @author vipul
 *
 */
public interface DownloadManager {
	

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 */
	public void downloadFile(File destinationFile,URL[] urls,PRIORITY priority) throws DownloadException;

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile,URL[] urls,DownloadListener listener) throws DownloadException;

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param md5
	 * @param length
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile,URL[] urls,Object md5, long length) throws DownloadException;

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @param md5
	 * @param length
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile,URL[] urls,DownloadListener listener, Object md5, long length,PRIORITY priority) throws DownloadException;


}
