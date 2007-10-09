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
	 * @param e
	 */
	public void setException(DownloadException e);
	

	/**
	 * notify that the download has been split into 'chunkCount' chunks 
	 * @param chunkCount
	 * @param chunkSize
	 */
	public void downloadSplit(int chunkCount, long chunkSize);
	
	/**
	 * notify if and when the total siz of the download is determined
	 * 
	 * @param totalFileLength
	 */
	public void downloadSizeDetermined(long totalFileLength);
	
	/**
	 * 
	 * @param id
	 */
	public void chunkDownloadStarted(int id);
	/**
	 * 
	 * @param id
	 */
	public void chunkDownloadComplete(int id);
	
}
