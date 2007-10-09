/**
 * 
 */
package net.effigent.jdownman.queue;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadListener;
import net.effigent.jdownman.queue.store.PersistenceAdapter;

/**
 * @author vipul
 *
 */
class ListenerForPersistence implements DownloadListener {
	
	/**
	 * 
	 */
	private Download download = null;
	/**
	 * 
	 */
	private PersistenceAdapter persistenceAdapter = null;

	/**
	 * @param download 
	 * @param persister 
	 * 
	 */
	public ListenerForPersistence(Download download, PersistenceAdapter persister) {
		this.download = download;
		this.persistenceAdapter = persister;
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#chunkDownloadComplete(int)
	 */
	public void chunkDownloadComplete(int id) {
		try {
			persistenceAdapter.markChunkDownloadComplete(download.getChunk(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#chunkDownloadStarted(int)
	 */
	public void chunkDownloadStarted(int id) {
		try {
			persistenceAdapter.markChunkDownloadStarted(download.getChunk(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadCancelled()
	 */
	public void downloadCancelled() {
		try {
			persistenceAdapter.markDownloadCancelled(download);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadComplete()
	 */
	public void downloadComplete() {
		try {
			persistenceAdapter.markDownloadComplete(download);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadInterrupted()
	 */
	public void downloadInterrupted() {
		try {
			persistenceAdapter.markDownloadInterrupted(download);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadPaused()
	 */
	public void downloadPaused() {
		try {
			persistenceAdapter.markDownloadInterrupted(download);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadSizesDetermined(long)
	 */
	public void downloadSizeDetermined(long totalFileLength) {
		try {
			persistenceAdapter.saveDownloadSize(download,totalFileLength);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadSplit(int, long)
	 */
	public void downloadSplit(int chunkCount, long chunkSize) {
		try {
			persistenceAdapter.markDownloadSplit(download,chunkCount,chunkSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#downloadStarted()
	 */
	public void downloadStarted() {
		try {
			persistenceAdapter.markDownloadStarted(download);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.DownloadListener#setException(net.effigent.jdownman.DownloadException)
	 */
	public void setException(DownloadException e) {
		try {
			persistenceAdapter.registerExceptionForDownload(download,e);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
