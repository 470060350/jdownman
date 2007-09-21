/**
 * 
 */
package net.effigent.jdownman;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.effigent.jdownman.split.Splitter;

import org.apache.log4j.Logger;

/**
 * 
 * @author vipul
 *
 */
public abstract class Download {
	
	
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Download.class);
	
	/**
	 * 
	 */
	private static final String DELIMITER = "/";
	
	/**
	 * 
	 */
	private List<DownloadListener> listeners = new ArrayList<DownloadListener>();

	/**
	 * All the pending downloads
	 */
	private Queue<ChunkDownload> chunks = new ConcurrentLinkedQueue<ChunkDownload>();

	
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
		FAILED
	}
	
	
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
	private URL[] urls = null;
	
	/**
	 * 
	 */
	private Object expectedMD5 = null;
	
	
	/**
	 * 
	 */
	private Date downloadRequestTime = null;
	
	/**
	 * 
	 */
	public Download() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	/**
	 * This method is declared here to be implemented by protocol
	 * specific implementations of Download.
	 * 
	 * @param url
	 * @param destination
	 */
	protected abstract void download(URL url, File destination, int beginRange, int endRange, DownloadMonitor monitor);
	
	/**
	 * 
	 * @param splitter
	 */
	public void split(Splitter splitter) {
		List<ChunkDownload> chunks = splitter.split(this);
		this.chunks.addAll(chunks);
	}
	
	/**
	 * 
	 * @param splitter
	 */
//	public void bind(Binder binder) {
//		pendingChuncks.addAll();
//	}
	
	/**
	 * 
	 *
	 */
	public void initialize() throws DownloadException{
		
		if(parentWorkDir == null)
			throw new DownloadException("parentWorkDirectory not set");
		if(!parentWorkDir.isDirectory())
			throw new DownloadException("parentWorkDirectory "+parentWorkDir.getAbsolutePath()+" is not a directory");
		if(!parentWorkDir.canWrite())
			throw new DownloadException("parentWorkDirectory is not writable");

		workDir = new File(parentWorkDir,"wd"+ID);
		if(!workDir.mkdir()) {
			throw new DownloadException("unable to create Directory "+workDir.getAbsolutePath());
		}
		
		for(ChunkDownload chunk : chunks) {
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
	 * @return the expectedMD5Value
	 */
	public Object getExpectedMD5() {
		return expectedMD5;
	}

	/**
	 * @param expectedMD5Value the expectedMD5Value to set
	 */
	public void setExpectedMD5(Object expectedMD5) {
		this.expectedMD5 = expectedMD5;
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
	public void setParentWorkDir(File workDir) {
		workDir = workDir;
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
	 * 
	 * @param downloadListener
	 */
	public void addListener(DownloadListener downloadListener) {
		listeners.add(downloadListener);
	}
	
	
	/**
	 * Returns a list of ChunkDownloads for this download
	 * @return the chunks
	 */
	public List<ChunkDownload> getChunks() {
		
		List<ChunkDownload> chunkList = new ArrayList<ChunkDownload>();
		chunkList.addAll(chunks);
		return chunkList;
		
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
	public class ChunkDownload implements Runnable{

		/**
		 * 
		 */
		private int id = -1;

		/**
		 * 
		 */
		private URL url = null;

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
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("\tDownloaded "+this);
				
			}finally {
				Thread.currentThread().setName(threadName);
			}
		}
		
		// -------------------  GETTERS and SETTERS -------------------   
	}
	//#################################	 InnerClass - ChunckDownload #################################	 


}
