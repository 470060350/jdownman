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
	private URL url = null;
	private AtomicLong startingByte = null;
	private AtomicLong endingByte = null;
	private AtomicLong bytesDownloaded = null;
	private AtomicBoolean complete = null;
	/**
	 * @return the bytesDownloaded
	 */
	public AtomicLong getBytesDownloaded() {
		return bytesDownloaded;
	}
	/**
	 * @param bytesDownloaded the bytesDownloaded to set
	 */
	public void setBytesDownloaded(AtomicLong bytesDownloaded) {
		this.bytesDownloaded = bytesDownloaded;
	}
	/**
	 * @return the complete
	 */
	public AtomicBoolean getComplete() {
		return complete;
	}
	/**
	 * @param complete the complete to set
	 */
	public void setComplete(AtomicBoolean complete) {
		this.complete = complete;
	}
	/**
	 * @return the endingByte
	 */
	public AtomicLong getEndingByte() {
		return endingByte;
	}
	/**
	 * @param endingByte the endingByte to set
	 */
	public void setEndingByte(AtomicLong endingByte) {
		this.endingByte = endingByte;
	}
	/**
	 * @return the startingByte
	 */
	public AtomicLong getStartingByte() {
		return startingByte;
	}
	/**
	 * @param startingByte the startingByte to set
	 */
	public void setStartingByte(AtomicLong startingByte) {
		this.startingByte = startingByte;
	}
	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
