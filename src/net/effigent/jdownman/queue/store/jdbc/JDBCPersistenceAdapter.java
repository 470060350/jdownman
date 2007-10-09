/**
 * 
 */
package net.effigent.jdownman.queue.store.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.queue.DownloadQueue;
import net.effigent.jdownman.queue.PersistentQueue;
import net.effigent.jdownman.queue.store.PersistenceAdapter;
import net.effigent.jdownman.util.InitializationException;

import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class JDBCPersistenceAdapter implements PersistenceAdapter{
	
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(JDBCPersistenceAdapter.class);
	/**
	 * 
	 */
	private DataSource dataSource = null;
	/**
	 * 
	 */
	private JDBCAdapter adapter = null;
	/**
	 * 
	 */
	private String dataSourceType = null;
	/**
	 * 
	 */
	Map<String,Class> typeAdapterClassMap = new HashMap<String,Class>();
	/**
	 * 
	 */
	private boolean createTablesOnStartup = false;

	/**
	 * 
	 */
	private Binder binder = null;
	
	
	/**
	 * 
	 * @param ds
	 */
	public JDBCPersistenceAdapter(DataSource dataSource) {
		this.dataSource = dataSource;
    }
	/**
	 * 
	 * @param ds
	 */
	public JDBCPersistenceAdapter() {}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
    public JDBCAdapter getAdapter() {
        if (adapter == null) {
            setAdapter(createAdapter());
        }
        return adapter;
    }

    /**
     * 
     * @param object
     */
	private void setAdapter(JDBCAdapter adapter) {
		this.adapter =  adapter;
	}

	/**
	 * 
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private JDBCAdapter createAdapter() {
		Class clazz = typeAdapterClassMap.get(dataSourceType) ;
		JDBCAdapter adapter = null;
		if(dataSourceType == null || clazz == null ) {
			adapter = new DefaultJDBCAdapter();
		}else {
			try {
				adapter = (JDBCAdapter)clazz.newInstance();
			} catch (Exception e) {
				// TODO THIS IS A TEMPORARY BLOCK .. HANDLE THE EXCEPTION GRACEFULLY
				e.printStackTrace();
			} 
		}
		return adapter ;
	}

	/**
	 * 
	 * @param queue
	 * @throws InitializationException
	 */
	public void initialize(DownloadQueue queue) throws InitializationException {
		
        if (isCreateTablesOnStartup()
        		&& false) // IGNORE FOR NOW....  
        {

            try {
                getAdapter().doCreateTables(dataSource.getConnection());
            } catch (SQLException e) {
                logger.warn("Cannot create tables due to: " + e);
                throw new InitializationException("Unable to create the tables "+e.getMessage());
            }
        }
        
        
        List<Download> pendingDownloads = null;
        try {
        	
        	Connection connection = dataSource.getConnection();
        	pendingDownloads = getAdapter().getPendingDownloads(dataSource.getConnection());

        } catch (SQLException e) {
			e.printStackTrace();
            throw new InitializationException("Unable to create the tables "+e.getMessage());
		}
        
        if(pendingDownloads != null ) {
            for(Download download : pendingDownloads) {
            	download.setBinder(binder);
            	try {
            		if(queue instanceof PersistentQueue)
            			((PersistentQueue)queue).enqueuePersistedDownload(download);
            		else 
    				queue.enqueueDownload(download);
    			} catch (DownloadException e) {
    				throw new InitializationException("Unable to enqueue download : "+download+" : "+e.getMessage());
    			}
            }
        }
		
	}

	public void markChunkDownloadComplete(ChunkDownload chunk) throws Exception{
		getAdapter().updateChunkStatus(dataSource.getConnection(),chunk.getParentDownload().getID(),chunk.getId(),Download.STATUS.COMPLETE);		
	}

	public void markChunkDownloadStarted(ChunkDownload chunk)  throws Exception{
		getAdapter().updateChunkStatus(dataSource.getConnection(),chunk.getParentDownload().getID(),chunk.getId(),Download.STATUS.STARTED);		
	}

	public void markDownloadCancelled(Download download)  throws Exception{
		getAdapter().updateDownloadStatus(dataSource.getConnection(),download.getID(),Download.STATUS.CANCELLED);		
		
	}

	public void markDownloadComplete(Download download)  throws Exception{
		getAdapter().cleanupCompletedDownload(dataSource.getConnection(),download.getID());
	}

	public void markDownloadInterrupted(Download download)  throws Exception{
		getAdapter().updateDownloadStatus(dataSource.getConnection(),download.getID(),Download.STATUS.INTERRUPTED);		
		
	}

	public void markDownloadSplit(Download download, int chunkCount, long chunkSize) {
		//This one is ignored for now assuming that a download is already split before persisting .. 
	}

	public void markDownloadStarted(Download download)  throws Exception{
		getAdapter().updateDownloadStatus(dataSource.getConnection(),download.getID(),Download.STATUS.STARTED);		
	}

	public void persistDownload(Download download)  throws Exception{
		getAdapter().persistDownload(dataSource.getConnection(),download);
	}

	public void registerExceptionForDownload(Download download, DownloadException e)  throws Exception{
		getAdapter().registerException(dataSource.getConnection(),download.getID(),e);
	}

	public void saveDownloadSize(Download download, long totalFileLength)  throws Exception{
		getAdapter().setDownloadSize(dataSource.getConnection(),download.getID(),totalFileLength);
	}
	
	/**
	 * 
	 */
	public void setBinder(Binder binder) {
		this.binder= binder;
	}

	
	/**
	 * @return the createTablesOnStartup
	 */
	public boolean isCreateTablesOnStartup() {
		return createTablesOnStartup;
	}

	/**
	 * @param createTablesOnStartup the createTablesOnStartup to set
	 */
	public void setCreateTablesOnStartup(boolean createTablesOnStartup) {
		this.createTablesOnStartup = createTablesOnStartup;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
