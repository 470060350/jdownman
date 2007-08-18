/**
 * 
 */
package net.effigent.jdownman;

import java.io.File;
import java.net.URL;



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
	public void downloadFile(File destinationFile,URL[] urls) throws DownloadException;

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
	 * @param CRC
	 * @param length
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile,URL[] urls,Object CRC, long length) throws DownloadException;

	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @param CRC
	 * @param length
	 * @throws DownloadException
	 */
	public void downloadFile(File destinationFile,URL[] urls,DownloadListener listener, Object CRC, long length) throws DownloadException;


}
