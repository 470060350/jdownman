/**
 * 
 */
package net.effigent.jdownman.impl;


import java.io.File;
import java.net.URL;
import java.util.Date;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadListener;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.checksum.ChecksumManager;
import net.effigent.jdownman.checksum.MD5ChecksumManager;
import net.effigent.jdownman.queue.DownloadQueue;
import net.effigent.jdownman.split.DefaultSplitter;
import net.effigent.jdownman.split.Splitter;
import net.effigent.jdownman.util.UIDGenerator;
import net.effigent.jdownman.work.DownloadWorkManager;

import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class DownloadManagerImpl extends AbstractDownloadManager{
	
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(DownloadManagerImpl.class);
	
	
	/**
	 * work directory
	 */
	private String workDirPath = null;
	
	/**
	 * the File object(Directory) representing workDirectory
	 */
	private File workDir = null;
	

	/**
	 * 
	 */
	private String DELIMITER = "/";
	
	/**
	 * Splitter that splits downloads into chunks
	 */
	Splitter splitter = null;
	
	/**
	 * 
	 */
	UIDGenerator uidGenerator = null;
	
	/**
	 * 
	 */
	DownloadFactory downloadFactory = new DownloadFactory();
	
	/**
	 * 
	 */
	DownloadQueue downloadQueue = null;
	
	
	/**
	 * 
	 */
	private boolean initialized = false;
	
	/**
	 * 
	 */
	private Binder binder = null;
	
	/**
	 * 
	 */
	ChecksumManager checksumManager = null;
	
	/**
	 * 
	 */
	DownloadWorkManager workManager = null;
	
	/**
	 * 
	 */
	boolean discardPartialChunks = true;
	

	/**
	 * Initialization method
	 * 
	 * @throws DownloadException
	 */
	public void initialize() throws DownloadException{

		if(workDirPath == null)
			throw new DownloadException("Work Directory Not specified");
		//create the work directory
		workDir = new File(workDirPath);

		//if workDir does not exist .. make it
		if(!workDir.exists())
			workDir.mkdir();
		
		// throw exception if work directory is not a directory
		if(!workDir.isDirectory())
			throw new DownloadException("Path specified for Work Directory is that of a file");
		
		// throw exception if work directory is not writable
		if(!workDir.canWrite())
			throw new DownloadException("Work Directory is not writeable");
		
		
		if(uidGenerator == null) {
			throw new DownloadException("UID generator is not set");
		}
		
		if(checksumManager == null) {
			checksumManager = new MD5ChecksumManager();
		}

		// if splitter is not set .... use the default splitter 
		if(splitter == null) {
			splitter = new DefaultSplitter();
		}
		
		//set the download queue
		workManager.setDownloadQueue(downloadQueue);
		workManager.start(); 
		
		initialized = true;
		
	}

	
	

	
	/**
	 * 
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @param checksum
	 * @param length
	 * @throws DownloadException
	 */

	public Object downloadFile(File destinationFile, URL[] urls, DownloadListener listener, Object checksum, long length,Download.PRIORITY priority) throws DownloadException {

		if(!initialized) {
			throw new DownloadException("DownloadManager not initialized yet");
		}
		
		
		//get the protocol - assumes that all the URLs are of the same protocol
		String protocol = urls[0].getProtocol().toLowerCase();
		
		// get an empty download object corresponding to the protocol
		Download download = downloadFactory.createDownload(protocol);
		if(priority == null)
			priority = PRIORITY.NORMAL;
		
		//populate the download object with the relevant values
		String uid = uidGenerator.generateNewUID();
		download.setID(uid);
		download.setPriority(priority);
		
		String checksumStr = null;
		if(checksum != null) {
			if(checksumManager == null)
				checksumStr = checksum.toString();
			else
				checksumStr = checksumManager.toChecksumString(checksum);
		}
		
		download.setExpectedChecksum(checksumStr);
		download.setFile(destinationFile);
		download.setTotalFileLength(length);
		download.setUrls(urls);
		download.setParentWorkDir(workDir);
		download.setDownloadRequestTime(new Date());
		download.setDELIMITER(DELIMITER);
		//run the download thorugh the splitter 
		download.split(splitter);
		download.setBinder(binder);
		//initialize
		download.initialize();
		//add the listeners now that the download has been initialized
		download.addListener(listener);
		//now that the download information has be collated .. enqueue the download
		downloadQueue.enqueueDownload(download);
		
		return uid;
	}


	/**
	 * @return the splitter
	 */
	public Splitter getSplitter() {
		return splitter;
	}


	/**
	 * @param splitter the splitter to set
	 */
	public void setSplitter(Splitter splitter) {
		this.splitter = splitter;
	}


	/**
	 * @return the uidGenerator
	 */
	public UIDGenerator getUidGenerator() {
		return uidGenerator;
	}


	/**
	 * @param uidGenerator the uidGenerator to set
	 */
	public void setUidGenerator(UIDGenerator uidGenerator) {
		this.uidGenerator = uidGenerator;
	}


	/**
	 * @return the workDir
	 */
	public String getWorkDirPath() {
		return workDirPath;
	}


	/**
	 * @param workDir the workDir to set
	 */
	public void setWorkDirPath(String workDir) {
		this.workDirPath = workDir;
	}





	/**
	 * @return the downloads
	 */
	public DownloadQueue getDownloadQueue() {
		return downloadQueue;
	}





	/**
	 * @param downloads the downloads to set
	 */
	public void setDownloadQueue(DownloadQueue downloads) {
		this.downloadQueue = downloads;
	}


	/**
	 * @return the workManager
	 */
	public DownloadWorkManager getWorkManager() {
		return workManager;
	}

	/**
	 * @param workManager the workManager to set
	 */
	public void setWorkManager(DownloadWorkManager workManager) {
		this.workManager = workManager;
	}





	/**
	 * @return the binder
	 */
	public Binder getBinder() {
		return binder;
	}





	/**
	 * @param binder the binder to set
	 */
	public void setBinder(Binder binder) {
		this.binder = binder;
	}





	/**
	 * @return the dELIMITER
	 */
	public String getDELIMITER() {
		return DELIMITER;
	}





	/**
	 * @param delimiter the dELIMITER to set
	 */
	public void setDELIMITER(String delimiter) {
		DELIMITER = delimiter;
	}





	/**
	 * @return the discardPartialChunks
	 */
	public boolean isDiscardPartialChunks() {
		return discardPartialChunks;
	}





	/**
	 * @param discardPartialChunks the discardPartialChunks to set
	 */
	public void setDiscardPartialChunks(boolean discardPartialChunks) {
		this.discardPartialChunks = discardPartialChunks;
	}



}
