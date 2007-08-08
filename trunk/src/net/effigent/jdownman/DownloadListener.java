/**
 * 
 */
package net.effigent.jdownman;


/**
 * @author vipul
 *
 */
public interface DownloadListener {
	/**
	 * 
	 *
	 */
	public void downloadStarted();
	/**
	 * 
	 *
	 */
	public void downloadPaused();
	/**
	 * 
	 *
	 */
	public void downloadCancelled();
	/**
	 * 
	 *
	 */
	public void downloadInterrupted();
	/**
	 * 
	 *
	 */
	public void downloadComplete();

	/**
	 * 
	 * @param size
	 */
	public void setTotalExpectedSize(long size);
	/**
	 * 
	 * @param monitors
	 */
	public void setDownloadMonitors(DownloadMonitor[] monitors);
	
	/**
	 * 
	 * @param e
	 */
	public void setException(DownloadException e);
	
}
