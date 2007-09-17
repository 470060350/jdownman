/**
 * 
 */
package net.effigent.jdownman.impl;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;

/**
 * @author vipul
 *
 */
public class DownloadFactory {
	
	/**
	 * 
	 */
	private static final String DOWNLOAD_IMPL = "DownloadImpl";
	/**
	 * 
	 */
	private static final String PARENT_PACKAGE = "net.effigent.jdownman.protocol.";
	
	/**
	 * 
	 * @param protocol
	 * @return
	 * @throws DownloadException 
	 */
	Download createDownload(String protocol) throws DownloadException {
		Download  download = null;
		String packageName = PARENT_PACKAGE.concat(protocol.toLowerCase());
		String downloadClassFQN = packageName.concat(".").concat(DOWNLOAD_IMPL);
		
		try {
			Class clazz = Class.forName(downloadClassFQN);
			download = (Download) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new DownloadException(" Invalid or Unsupported protocol : "+protocol);
		}catch (Exception e) {
			throw new DownloadException(" Exception while creating Download Instance for protocol : "+protocol+" : "+e.getMessage(),e);
		}
		
		return download;
	}


}
