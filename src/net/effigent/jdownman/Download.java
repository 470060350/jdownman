/**
 * 
 */
package net.effigent.jdownman;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.split.Splitter;

import org.apache.log4j.Logger;

/**
 * 
 * @author vipul
 *
 */
public abstract class Download  implements Serializable{ 
//implements Externalizable {

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Download.class);
	
	/**
	 * 
	 */
	private String DELIMITER = "/";
	
	/**
	 * 
	 */
	private transient List<DownloadListener> listeners = new ArrayList<DownloadListener>();

	/**
	 * All the pending downloads
	 */
	private Map<Integer,ChunkDownload> chunks = new TreeMap<Integer,ChunkDownload>();
	
	/**
	 * chunks that are still not complete ... this includes the chunks that are BEING downloaded ... 
	 * and the ones that are still in the queue
	 */
	private AtomicInteger pendingChunkCount = new AtomicInteger(0);

	/**
	 * chunks that are in progress right now.
	 */
	private transient AtomicInteger ongoingChunkCount = new AtomicInteger(0);

	/**
	 * default priority is NORMAL
	 */
	private PRIORITY priority = PRIORITY.NORMAL;
	
	/**
	 * 
	 */
	private STATUS status = STATUS.PENDING;
	
	/**
	 * 
	 */
	private String ID = null;
	
	/**
	 * 
	 */
	private File file= null;

	/**
	 * 
	 */
	long totalFileLength = -1;
	
	/**
	 * 
	 */
	private File parentWorkDir = null;

	/**
	 * 
	 */
	private File workDir = null;

	/**
	 * 
	 */
	protected URL[] urls = null;
	
	/**
	 * 
	 */
	private String expectedChecksum = null;
	
	
	/**
	 * 
	 */
	private Date downloadRequestTime = null;
	
	/**
	 * 
	 */
	private Date commencementTime = null;
	/**
	 * 
	 */
	private Date completionTime = null;
	
	/**
	 * 
	 */
	private transient Binder binder = null;

	/**
	 * 
	 */
	boolean discardPartialChunks = false;


	/**
	 * 
	 */
	public void Download() {
		listeners = new ArrayList<DownloadListener>();
		ongoingChunkCount = new AtomicInteger(0);
	}
	
	/**
	 * This method is declared here to be implemented by protocol
	 * specific implementations of Download.
	 * 
	 * @param url
	 * @param destination
	 * @param beginRange
	 * @param endRange
	 * @param totalFileSize
	 * @param monitor
	 * @throws DownloadException
	 */
	protected abstract void download(URL url, File destination, long beginRange, long endRange, long totalFileSize,boolean appendToFile)  throws DownloadException;
	
	/**
	 * 
	 * get the size of the file 
	 * @return
	 */
	protected abstract long size() throws DownloadException;
	
	/**
	 * 
	 * @return
	 */
	public abstract String getProtocol();

	
	/**
	 * The visitor -Splitter in this case - works upon the current download and splits it
	 * generating multiple chunks.
	 * 
	 * @param splitter
	 * @throws DownloadException 
	 */
	public void split(Splitter splitter) throws DownloadException {
		//get the sizse of the file
		totalFileLength = size();
		//let the splitter split this download
		List<ChunkDownload> chunks = splitter.split(this);
		
		for(ChunkDownload chunk : chunks) {
			//save all the chunks .. map them against their ids
			this.chunks.put(chunk.getId(), chunk);
		}
		//let all the listeners know that the download has been split
		for(DownloadListener listener : listeners) 
		{
			if(totalFileLength>0) {
				listener.downloadSizeDetermined(totalFileLength);
			}
			listener.downloadSplit(chunks.size(),splitter.getMaxChunkSize());
		}
		//set the pending chunk count .. since all of them are pending 
		pendingChunkCount.compareAndSet(0, chunks.size());
	}
	
	/**
	 * Initialize the download .... 
	 *
	 */
	public void initialize() throws DownloadException{
		//create a new array for listeners
		listeners = new ArrayList<DownloadListener>();
		// set the ongoing chunk count to 0
		ongoingChunkCount = new AtomicInteger(0);
		//check for the parentWorkDir's integrity 
		if(parentWorkDir == null)
			throw new DownloadException("parentWorkDirectory not set");
		if(!parentWorkDir.isDirectory())
			throw new DownloadException("parentWorkDirectory "+parentWorkDir.getAbsolutePath()+" is not a directory");
		if(!parentWorkDir.canWrite())
			throw new DownloadException("parentWorkDirectory is not writable");
		//create (or verify) the work directory for this downlpad
		workDir = new File(parentWorkDir,"wd"+ID);
		if(!workDir.exists() && !workDir.mkdir()) {
			throw new DownloadException("unable to create Directory "+workDir.getAbsolutePath());
		}
		//set the file path for each chunk ... 
		for(ChunkDownload chunk : chunks.values()) {
			chunk.setChunkFilePath(workDir.getAbsolutePath()+DELIMITER+chunk.getId());
		}

	}

	
	// -------------------  GETTERS and SETTERS -------------------   
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param id the iD to set
	 */
	public void setID(String id) {
		ID = id;
	}

	/**
	 * @return the priority
	 */
	public PRIORITY getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(PRIORITY priority) {
		this.priority = priority;
	}

	/**
	 * @return the expectedChecksum
	 */
	public Object getExpectedChecksum() {
		return expectedChecksum;
	}

	/**
	 * @param expectedChecksum 
	 */
	public void setExpectedChecksum(String expectedChecksum) {
		this.expectedChecksum = expectedChecksum;
	}

	/**
	 * @return the filePath
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFile(File file) {
		this.file= file;
	}

	/**
	 * @return the urls
	 */
	public URL[] getUrls() {
		return urls;
	}

	/**
	 * @param urls the urls to set
	 */
	public void setUrls(URL[] urls) {
		this.urls = urls;
	}
	
	/**
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(STATUS status) {
		this.status = status;
	}

	/**
	 * @return the wORK_DIR
	 */
	public File getParentWorkDir() {
		return parentWorkDir;
	}

	/**
	 * @param work_dir the wORK_DIR to set
	 */
	public void setParentWorkDir(File parentWorkDir) {
		this.parentWorkDir = parentWorkDir;
	}

	/**
	 * @return the totalFileLength
	 */
	public long getTotalFileLength() {
		return totalFileLength;
	}

	/**
	 * @param totalFileLength the totalFileLength to set
	 */
	public void setTotalFileLength(long totalFileLength) {
		this.totalFileLength = totalFileLength;
	}
	/**
	 * @return the workDir
	 */
	public File getWorkDir() {
		return workDir;
	}

	/**
	 * @param workDir the workDir to set
	 */
	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	/**
	 * @return the downloadRequestTime
	 */
	public Date getDownloadRequestTime() {
		return downloadRequestTime;
	}

	/**
	 * @param downloadRequestTime the downloadRequestTime to set
	 */
	public void setDownloadRequestTime(Date downloadRequestTime) {
		this.downloadRequestTime = downloadRequestTime;
	}
	
	/**
	 * @return the commencementTime
	 */
	public Date getCommencementTime() {
		return commencementTime;
	}

	/**
	 * @return the completionTime
	 */
	public Date getCompletionTime() {
		return completionTime;
	}
	/**
	 * 
	 * @param downloadListener
	 */
	public void addListener(DownloadListener downloadListener) {
		if(downloadListener != null)
			listeners.add(downloadListener);
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
	 * @param commencementTime the commencementTime to set
	 */
	public void setCommencementTime(Date commencementTime) {
		this.commencementTime = commencementTime;
	}

	/**
	 * @param completionTime the completionTime to set
	 */
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
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
	/**
	 * @return the pendingChunkCount
	 */
	public AtomicInteger getPendingChunkCount() {
		return pendingChunkCount;
	}

	/**
	 * @param pendingChunkCount the pendingChunkCount to set
	 */
	public void resetPendingChunkCount() {
		pendingChunkCount.set(chunks.size());
	}

	/**
	 * 
	 * @param download
	 */
	public void notifyDownloadCommencement(ChunkDownload download) {
		int ongoing = ongoingChunkCount.incrementAndGet();
		if(ongoing == 1) {
			commencementTime = new Date();
			
			if (logger.isDebugEnabled())
				logger.debug("Download "+ID+" started at : "+commencementTime);
			System.out.println("%%% Download started at : "+commencementTime);
			status = STATUS.STARTED;
		}
		
		System.out.println(" ********************************  invoking Listeners for donwloadStart ");
		for(DownloadListener listener : listeners) {
			System.out.println(" ********************************  Listener invoked : "+listener);
			if(ongoing == 1)
				listener.downloadStarted();
			listener.chunkDownloadStarted(download.getId());
		}

		
	}

	/**
	 * 
	 * @param completedChunk
	 */
	private void notifyDownloadCompletion(ChunkDownload chunk) throws DownloadException{
		//decrement the pending count and get it  
		int pending = pendingChunkCount.decrementAndGet();

		if(pending == 0) {
			//no more pending now .... 			
			binder.bindDownload(this);
			status = STATUS.COMPLETE;
		}

		for(DownloadListener listener : listeners) {
			listener.chunkDownloadComplete(chunk.getId());
			if(pending == 0) {
				listener.downloadComplete();
			}
		}
		
	}
	
	/**
	 * 
	 *
	 */
	public void complete() {

		completionTime = new Date();
		if (logger.isDebugEnabled())
			logger.debug("Download "+ID+" Completed at : "+completionTime);
		for(DownloadListener listener : listeners) {
			listener.downloadComplete();
		}
		
		System.out.println("%%% Download Completed at : "+completionTime);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Total Time : "+(completionTime.getTime() - commencementTime.getTime()));
		
	}



	
	/**
	 * Returns a list of ChunkDownloads for this download
	 * @return the chunks
	 */
	public List<ChunkDownload> getChunks() {
		
		List<ChunkDownload> chunkList = new ArrayList<ChunkDownload>();
		chunkList.addAll(chunks.values());
		return chunkList;
		
	}

	/**
	 * Returns a list of ChunkDownloads for this download
	 * @return the chunks
	 */
	public void setChunks(List<ChunkDownload> chunkList) {
		
		//iterate through all the chunks and add them to the map
		for(ChunkDownload chunk : chunkList) {
			chunks.put(chunk.getId(),chunk);
		}
		
	}

	
	/**
	 * 
	 * @return
	 */
	public ChunkDownload getChunk(Integer chunkId) {
		return chunks.get(chunkId);
	}

	/**
	 * 
	 */
	public String toString() {
		return ID+" priority -> "+priority+"  requestedTime : "+downloadRequestTime.toGMTString();
	}
	
	

	// -------------------  GETTERS and SETTERS -------------------   

	//#################################	 InnerClass - ChunckDownload #################################	 
	
	/**
	 * 
	 */
	public class ChunkDownload implements Runnable,Externalizable{

		/**
		 * 
		 */
		private int id = -1;

		/**
		 * 
		 */
		private STATUS status = STATUS.PENDING;
		
		/**
		 * 
		 */
		private String chunkFilePath = null;
		
		/**
		 * 
		 */
		private long beginRange = -1;

		/**
		 * 
		 */
		private long endRange = -1;
		/**
		 * 
		 */
		private long size = -1;
		
		// -------------------  GETTERS and SETTERS -------------------   
		
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * @return the status
		 */
		public STATUS getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(STATUS status) {
			this.status = status;
		}

		/**
		 * @return the chunkFilePath
		 */
		public String getChunkFilePath() {
			return chunkFilePath;
		}
		/**
		 * @param chunkFilePath the chunkFilePath to set
		 */
		public void setChunkFilePath(String chunkFilePath) {
			this.chunkFilePath = chunkFilePath;
		}

		/**
		 * 
		 * @return
		 */
		public Download getParentDownload() {
			return Download.this;
		}
		/**
		 * @return the beginRange
		 */
		public long getBeginRange() {
			return beginRange;
		}
		/**
		 * @param beginRange the beginRange to set
		 */
		public void setBeginRange(long beginRange) {
			this.beginRange = beginRange;
		}
		/**
		 * @return the endRange
		 */
		public long getEndRange() {
			return endRange;
		}
		/**
		 * @param endRange the endRange to set
		 */
		public void setEndRange(long endRange) {
			this.endRange = endRange;
		}
		
		/**
		 * @param endRange the endRange to set
		 */

		
		/**
		 * 
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(" Chunk : ");
			sb.append(Download.this.getID());
			sb.append("-"+id);
			sb.append(" Range : "+beginRange+"-"+endRange);
			sb.append(" Status : "+status.name());
			sb.append(" Priority : "+Download.this.getPriority().name());
			
			return sb.toString();
			
		}

		/**
		 * 
		 */
		public void run() {
			String threadName = Thread.currentThread().getName();
			try {
				Thread.currentThread().setName(threadName+"- Downloading:"+Download.this.ID+"-"+this.id+" - ");
				System.out.println("\n**** Downloading "+this);
				try {
					//TODO figure out how to select a URL
					
					notifyDownloadCommencement(this);
					
					
					File chunkFile = new File(chunkFilePath);
					boolean downloadFile = true;
					boolean appendToFile = false;
					
					if(chunkFile.exists()) {
						//if the file already exists ... see if it has the same size as was expected.
						
						long chunkFileSize = chunkFile.length();
						if(chunkFileSize == (endRange - beginRange +1)) {
							//if the file exists already .. don't download it again.(by setting the downloadFile flag as false)
							logger.warn(" Chunk "+id+" of download "+ID+ " already exists. Not downloading again");
							downloadFile = false;
							
						}else if(!discardPartialChunks){
							beginRange= beginRange+chunkFileSize;
							appendToFile = true;
						}else{
							chunkFile.delete();
						}
						
					}

					/**
					 * if download is needed .... 
					 */
					if(downloadFile) {
						download(urls[0],chunkFile, beginRange, endRange, totalFileLength,appendToFile);
					}

					notifyDownloadCompletion(this);

				} catch (Exception e) {
					e.printStackTrace();
					setStatus(STATUS.FAILED);
					logger.error(" Unable to download the chunk : "+this,e);
				}
				System.out.println("\tDownloaded "+this);
				
				
			}finally {
				Thread.currentThread().setName(threadName);
			}
		}

		/**
		 * 
		 */
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			id = in.readInt();
			status = (STATUS)in.readObject();
			chunkFilePath = (String)in.readObject();
			beginRange = in.readLong();
			endRange = in.readLong();
			
		}

		/**
		 * 
		 */
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeInt(id);
			out.writeObject(status);
			out.writeObject(chunkFilePath);
			out.writeLong(beginRange);
			out.writeLong(endRange);
			
		}

		/**
		 * @return the size
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @param size the size to set
		 */
		public void setSize(long size) {
			this.size = size;
		}

		// -------------------  GETTERS and SETTERS -------------------   
	}
	//#################################	 InnerClass - ChunckDownload #################################	 



	/**
	 * 
	 * @author vipul
	 *
	 */
	public static enum PRIORITY{
		LOW_PRIORITY(1),
		NORMAL(2),
		HIGH_PRIORITY(3),
		URGENT(4);
		
		/**
		 * 
		 */
		int value = -1;
		/**
		 * 
		 * @param priority
		 */
		PRIORITY(int value){
			this.value = value;
		}
		/**
		 * @return the priority
		 */
		public int getValue() {
			return value;
		}
		/**
		 * @param priority the priority to set
		 */
		public void setValue(int priority) {
			this.value = priority;
		}
		
	}
	
	/**
	 * 
	 * @author vipul
	 *
	 */
	public static enum STATUS{
		PENDING,
		STARTED,
		COMPLETE,
		FAILED, 
		CANCELLED, 
		INTERRUPTED
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
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

}
