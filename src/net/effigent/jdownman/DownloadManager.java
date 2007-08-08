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
	 * @param urls
	 * @param listener
	 */
	public void downloadFile(File destinationFile,URL[] urls,DownloadListener listener, Object CRC);


}
