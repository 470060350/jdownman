/**
 * 
 */
package net.effigent.jdownman.queue.store.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.queue.DownloadQueue;
import net.effigent.jdownman.queue.PersistentQueue;
import net.effigent.jdownman.queue.store.PersistenceAdapter;
import net.effigent.jdownman.util.InitializationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * @author vipul
 *
 */
public class FileBasedPersister implements PersistenceAdapter {
	
	/**
	 * 
	 */
	private static final String FILE_SUFFIX = ".jdm";

	/**
	 * 
	 */
	AtomicBoolean archive = new AtomicBoolean(false);
	
	/**
	 * 
	 */
	private File baseDirectory = null;
	
	/**
	 * 
	 */
	private Map<String,File> downloadFileMap = new HashMap<String,File>();
	
	/**
	 * 
	 */
	private Binder binder = null;

	/**
	 * 
	 */
	public FileBasedPersister(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		
	}
	

	/**
	 * @throws InitializationException 
	 * 
	 */
	public void initialize(DownloadQueue queue) throws InitializationException {
		
		
		if(!baseDirectory.exists()) {
			baseDirectory.mkdir();
		}
		File archiveDir = new File(baseDirectory,"archive");
		if(archive.get() && !archiveDir.exists()) {
			archiveDir.mkdir();
		}
		
		Iterator<File> downloadFiles = FileUtils.iterateFiles(baseDirectory, new SuffixFileFilter(FILE_SUFFIX), null);
		
		//iterate over the file list .. and load/create download object for each
		while(downloadFiles.hasNext()) {
			File downloadFile = downloadFiles.next();
			Download download = null;// read each pending donwload ... and enqueue it.
			try{
				
				download = loadDownload(downloadFile);
				downloadFileMap.put(download.getID(), downloadFile);
				download.setBinder(binder);
				download.initialize();
				if(queue instanceof PersistentQueue)
					((PersistentQueue)queue).enqueuePersistedDownload(download);
				else
					queue.enqueueDownload(download);
			} catch (Exception e) {
				e.printStackTrace();
				throw new InitializationException("Unable to enqueue the download "+download+" : "+e.getMessage(),e);
			}
		}

	}

	/**
	 * This method loads the download details from the file
	 * 
	 * @param downloadFile
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	private Download loadDownload(File downloadFile) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(downloadFile));
		Download download = (Download) ois.readObject();
		download.setBinder(binder);
		return download;
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markChunkDownloadComplete(net.effigent.jdownman.Download.ChunkDownload)
	 */
	public void markChunkDownloadComplete(ChunkDownload chunk) {
		try {
			updateDownloadObject(chunk.getParentDownload());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markChunkDownloadStarted(net.effigent.jdownman.Download.ChunkDownload)
	 */
	public void markChunkDownloadStarted(ChunkDownload chunk) {
		try {
			updateDownloadObject(chunk.getParentDownload());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markDownloadCancelled(net.effigent.jdownman.Download)
	 */
	public void markDownloadCancelled(Download download) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markDownloadComplete(net.effigent.jdownman.Download)
	 */
	public void markDownloadComplete(Download download) {
		File downloadFile = downloadFileMap.remove(download.getID());
		if(downloadFile != null)
			downloadFile.delete();
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markDownloadInterrupted(net.effigent.jdownman.Download)
	 */
	public void markDownloadInterrupted(Download download) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markDownloadSplit(net.effigent.jdownman.Download, int, long)
	 */
	public void markDownloadSplit(Download download, int chunkCount,
			long chunkSize) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#markDownloadStarted(net.effigent.jdownman.Download)
	 */
	public void markDownloadStarted(Download download) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#persistDownload(net.effigent.jdownman.Download)
	 */
	public void persistDownload(Download download) {

		File downloadFile = new File(baseDirectory,download.getID().concat(FILE_SUFFIX));
		downloadFileMap.put(download.getID(),downloadFile);
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#registerExceptionForDownload(net.effigent.jdownman.Download, net.effigent.jdownman.DownloadException)
	 */
	public void registerExceptionForDownload(Download download,
			DownloadException exception) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.queue.Persister#saveDownloadSizes(net.effigent.jdownman.Download, long)
	 */
	public void saveDownloadSize(Download download, long totalFileLength) {
		try {
			updateDownloadObject(download);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param download
	 */
	public synchronized void  updateDownloadObject(Download download) throws IOException{
		File downloadFile = downloadFileMap.get(download.getID());
		
		if(downloadFile.exists() && !downloadFile.delete()) {
			throw new IOException("Unable to delete the old file for download : "+download.getID());
		}
		if(!downloadFile.createNewFile()) {
			throw new IOException("Unable to create new file for download : "+download.getID());
		}
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(downloadFile));
		oos.writeObject(download);
	}

	/**
	 * 
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
