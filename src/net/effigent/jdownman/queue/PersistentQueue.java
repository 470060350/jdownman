package net.effigent.jdownman.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.queue.store.PersistenceAdapter;
import net.effigent.jdownman.util.DownloadComparator;
import net.effigent.jdownman.util.InitializationException;

public  class PersistentQueue implements DownloadQueue{

	/**
	 * Priority Queue to line up the downloads for processing
	 * in the order of Priority set for each of the download - ordered
	 * further by the time of creation (DownloadComparator)
	 */
	BlockingQueue<Download> downloads = new PriorityBlockingQueue<Download>(10,new DownloadComparator());

	/**
	 * 
	 */
	private PersistenceAdapter persistenceAdapter = null;
	
	/**
	 * A latch for initialization
	 */
	private CountDownLatch initLatch = new CountDownLatch(1);
	
	/**
	 * 
	 */
	Binder binder = null;
	

	/**
	 * 
	 * 
	 * @throws InitializationException 
	 */
	public void initialize() throws InitializationException {
		if(persistenceAdapter == null)
			throw new InitializationException("Persister not set "); 
		
		persistenceAdapter.setBinder(binder);
		persistenceAdapter.initialize(this);
		initLatch.countDown();
	}
	
	/**
	 * 
	 */
	public Download dequeueDownload() throws DownloadException {

		try {//wait if the the queue has not been initialized yet!
			initLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new DownloadException(" Interrupted Exception thrown while waiting on the initLatch : "+e.getMessage());
		}
		try {
			return downloads.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new DownloadException("Unable to denqueue the download : "+e.getMessage(),e);
		}
	}

	/**
	 * 
	 */
	public void enqueueDownload(Download download) throws DownloadException {
		
		try {//wait if the the queue has not been initialized yet!
			initLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new DownloadException(" Interrupted Exception thrown while waiting on the initLatch : "+e.getMessage());
		}
		
		try {
			persistenceAdapter.persistDownload(download);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DownloadException("Unable to eprsist the exception");
		}
		enqueuePersistedDownload(download);
	}

	/**
	 * 
	 */
	public void enqueuePersistedDownload(Download download) throws DownloadException {
		
		
		ListenerForPersistence listener = new ListenerForPersistence(download,persistenceAdapter);
		download.addListener(listener);

		try {
			downloads.put(download);
		} catch (InterruptedException e) {
			//this won't happen as we are using PriorityBlockingQueue that does not block at all
			e.printStackTrace();
			throw new DownloadException("Unable to enqueue the download : "+e.getMessage(),e);
		}
	}

	/**
	 * @return the persister
	 */
	public PersistenceAdapter getPersistenceAdapter() {
		return persistenceAdapter;
	}

	/**
	 * @param persistenceAdapter the persister to set
	 */
	public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
		this.persistenceAdapter = persistenceAdapter;
	}

	/**
	 * 
	 * @param binder
	 */
	public void setBinder(Binder binder) {
		this.binder = binder;
	}

	/**
	 * @return the binder
	 */
	public Binder getBinder() {
		return binder;
	}

}
