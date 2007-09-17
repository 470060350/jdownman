/**
 * 
 */
package net.effigent.jdownman.impl;


import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadListener;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.util.DefaultSplitter;
import net.effigent.jdownman.util.Splitter;
import net.effigent.jdownman.util.UIDGenerator;

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
	 * Priority Queue to line up the downloads for processing
	 * in the order of Priority set for each of the - ordered
	 * further by the time of creation (DownloadComparator)
	 */
	BlockingQueue<Download> downloads = new PriorityBlockingQueue<Download>(1,new DownloadComparator());

	
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
	private boolean initialized = false;
	

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

		// if splitter is not set .... use the default splitter 
		if(splitter == null) {
			splitter = new DefaultSplitter();
		}

		
		initialized = true;
		
	}

	
	

	
	/**
	 * 
	 * @param destinationFile
	 * @param urls
	 * @param listener
	 * @param md5
	 * @param length
	 * @throws DownloadException
	 */

	public void downloadFile(File destinationFile, URL[] urls, DownloadListener listener, Object md5, long length,Download.PRIORITY priority) throws DownloadException {

		if(!initialized) {
			throw new DownloadException("DownloadManager not initialized yet");
		}
		
		
		//get the protocol - assumes that all the URLs are of the same protocol
		String protocol = urls[0].getProtocol().toLowerCase();
		
		// get the download object corresponding to the protocol
		Download download = downloadFactory.createDownload(protocol);
		if(priority == null)
			priority = PRIORITY.NORMAL;
		
		String uid = uidGenerator.generateNewUID();
		download.setID(uid);
		download.setPriority(priority);
		download.setExpectedMD5(md5);
		download.setFile(destinationFile);
		download.setTotalFileLength(length);
		download.setUrls(urls);
		download.setParentWorkDir(workDir);
		download.split(splitter);
		download.setDownloadRequestTime(new Date());
		
		try {
			downloads.put(download);
		} catch (InterruptedException e) {
			//never really happens as we are using PriorityBlockingQueue 
			e.printStackTrace();
			logger.error("Unable to enqueue download : "+e.getMessage(),e);
		}
		
		
		// TODO Auto-generated method stub
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
	 * 
	 * @author vipul
	 *
	 */
	private static class DownloadComparator implements Comparator<Download>{

		/**
		 * 
		 */
		public int compare(Download o1, Download o2) {
			if(o1.getPriority().getValue() > o2.getPriority().getValue())
				return 1;
			if(o1.getPriority().getValue() < o2.getPriority().getValue())
				return -1;

			if(o1.getDownloadRequestTime().after(o1.getDownloadRequestTime()))
				return -1;
			else
				return 1;
			
		}
		
	}
	
	
}
