/**
 * 
 */
package net.effigent.jdownman;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author vipul
 *
 */
public class DownloadMonitor {
	
	/**
	 * 
	 */
	private URL url = null;
	/**
	 * 
	 */
	private AtomicLong beginIndex = null;
	/**
	 * 
	 */
	private AtomicLong endIndex = null;
	/**
	 * 
	 */
	private AtomicLong bytesDownloaded = null;
	/**
	 * 
	 */
	private AtomicBoolean complete = null;
	/**
	 * 
	 * @return the bytesDownloaded
	 */
	public AtomicLong getBytesDownloaded() {
		return bytesDownloaded;
	}
	/**
	 * 
	 * @param bytesDownloaded the bytesDownloaded to set
	 */
	public void setBytesDownloaded(AtomicLong bytesDownloaded) {
		this.bytesDownloaded = bytesDownloaded;
	}
	/**
	 * 
	 * @return the complete
	 */
	public AtomicBoolean getComplete() {
		return complete;
	}
	/**
	 * 
	 * @param complete the complete to set
	 */
	public void setComplete(AtomicBoolean complete) {
		this.complete = complete;
	}
	/**
	 * 
	 * @return the endingByte
	 */
	public AtomicLong getEndIndex() {
		return endIndex;
	}
	/**
	 * 
	 * @param endingByte the endingByte to set
	 */
	public void setEndIndex(AtomicLong endingByte) {
		this.endIndex = endingByte;
	}
	/**
	 * 
	 * @return the startingByte
	 */
	public AtomicLong getBeginIndex() {
		return beginIndex;
	}
	/**
	 * 
	 * @param startingByte the startingByte to set
	 */
	public void setBeginIndex(AtomicLong startingByte) {
		this.beginIndex = startingByte;
	}
	/**
	 * 
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	/**
	 * 
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
