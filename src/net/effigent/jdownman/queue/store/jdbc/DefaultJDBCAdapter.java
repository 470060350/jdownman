/**
 * 
 */
package net.effigent.jdownman.queue.store.jdbc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.Download.STATUS;
import net.effigent.jdownman.impl.DownloadFactory;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * @author vipul
 *
 */
public class DefaultJDBCAdapter implements JDBCAdapter {



	/**
	 * 
	 */
	private Statements statements = new Statements();
	
	/**
	 * 
	 */
	private DownloadFactory downloadFactory = new DownloadFactory(); 
	

	/**
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public void doCreateTables(Connection connection) throws SQLException{
		throw new SQLException("NOT IMPLEMENTED");
	}
	/**
	 * 
	 * @param connection 
	 * @param download
	 * @throws SQLException
	 */
	public void persistDownload(Connection connection, Download download) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[11];
		params[0] = download.getID(); //ID
		params[1] = download.getPriority().name(); //PRIORITY
		params[2] = download.getStatus().name(); //STATUS
		params[3] = download.getExpectedChecksum(); //CHECKSUM
		params[4] = download.getWorkDir().getAbsolutePath(); //WORK_DIR
		params[5] = download.getFile().getAbsolutePath(); //TARGET_FILE
		params[6] = download.getTotalFileLength(); //SIZE
		params[7] = download.getDownloadRequestTime(); //REQUEST_TIME
		params[8] = download.getCommencementTime(); //COMMENCEMEN_TIME
		params[9] = download.getCompletionTime() ; //COMPLETION_TIME
		params[10] = download.getProtocol() ; //protocol

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getInsertDownloadStatement(), params);

			for(ChunkDownload chunk : download.getChunks()) {
				persistChunk(connection, chunk);
			}
			
			URL[] urls =  download.getUrls();
			for(URL url: urls) {
				persistDownloadUrls(connection,download.getID(), url);
			}
			
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
		
		
		
		
	}

	/**
	 * 
	 * @param connection
	 * @param id
	 * @param url
	 * @throws SQLException 
	 */
	private void persistDownloadUrls(Connection connection, String id, URL url) throws SQLException {
		if(url == null)
			return;
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[2];
		params[0] = id;
		params[1] = url.toExternalForm();
		
		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getInsertUrlsStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
//			connection.close();
		}

	}
	/**
	 * 
	 * @param chunk
	 * @throws SQLException
	 */
	public void persistChunk(Connection connection,ChunkDownload chunk) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[7];
		params [0] = chunk.getId();//ID
		params [1] = chunk.getParentDownload().getID();//DODWNLOAD_ID
		params [2] = chunk.getStatus().name();//STATUS
		params [3] = chunk.getChunkFilePath();//FILE_PATH
		params [4] = chunk.getSize();//SIZE
		params [5] = chunk.getBeginRange();//BEGIN_RANGE
		params [6] = chunk.getEndRange();//END_RANGE

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getInsertChunkStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
//			connection.close();
		}
	}
	/**
	 * 
	 * @param connection 
	 * @param id
	 * @param id2
	 * @param complete
	 * @throws SQLException
	 */
	public void updateChunkStatus(Connection connection, String downloadId, int chunkId, STATUS status) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[3];
		params [0] = status.name();//status
		params [1] = chunkId;//chunk ID
		params [2] = downloadId;//DODWNLOAD_ID

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getUpdateChunkStatusStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
	}
	/**
	 * 
	 * @param connection 
	 * @param id
	 * @param cancelled
	 * @throws SQLException
	 */
	public void updateDownloadStatus(Connection connection, String id, STATUS status) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[2];
		params [0] = status.name();//status
		params [1] = id;//chunk ID

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getUpdateDownloadStatusStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
	}

	/**
	 * 
	 * @param connection 
	 * @param id
	 * @param e
	 * @throws SQLException
	 */
	public void registerException(Connection connection, String id, DownloadException e) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[2];
		params [0] = e.getClass().getName()+":"+e.getMessage();//length
		params [1] = id;//chunk ID

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getSaveExceptionMessageStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
	}
	/**
	 * 
	 * @param connection 
	 * @param id
	 * @param totalFileLength
	 * @throws SQLException
	 */
	public void setDownloadSize(Connection connection, String id, long totalFileLength) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[2];
		params [0] = totalFileLength;//length
		params [1] = id;//chunk ID

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getSetDownloadSizeStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
	}

	
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
	public List<Download> getPendingDownloads(Connection connection) throws SQLException{
		List<Download> downloads = null;
		
		QueryRunner qr = new QueryRunner();
		
		downloads = (List<Download>)qr.query(connection, statements.getGetPendingDownloadsStatement(),new GetPendingDownloads_RSH());
		
		for(Download download : downloads) {
			
			qr.query(connection, statements.getGetChunksForDownloadStatement(), download.getID(), new PopulateDownloadWithChunks_RSH(download));
			qr.query(connection, statements.getGetUrlsForDownloadStatement(), download.getID(), new PopulateDownloadWithUrls_RSH(download));
		}
		
		
		return downloads;
	}

	/**
	 * 
	 * @param connection
	 * @param id
	 */
	public void cleanupCompletedDownload(Connection connection, String id)  throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] params  = new Object[1];
		params [0] = id;//download ID

		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			qr.update(connection, statements.getDeleteDownloadStatement(), params);
			qr.update(connection, statements.getDeleteChunksStatement(), params);
			qr.update(connection, statements.getDeleteUrlsStatement(), params);
		}finally {
			connection.setAutoCommit(autoCommit);
			connection.close();
		}
	}

	
	/**
	 * 
	 * @param statementProvider
	 */
    public void setStatements(Statements statements) {
    	this.statements = statements;
    }

	/**
	 * @author vipul
	 *
	 */
	public class GetPendingDownloads_RSH implements ResultSetHandler {

		/* (non-Javadoc)
		 * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
		 */
		public List<Download> handle(ResultSet resultSet) throws SQLException {
			List<Download> downloads = new ArrayList<Download>();
			while(resultSet.next()) {
				Download download = null;
				String protocol = resultSet.getString("PROTOCOL");
				try {
					download =downloadFactory.createDownload(protocol.toLowerCase());
				} catch (DownloadException e) {
					throw new SQLException("Unable to create new Download Object for protocol : "+protocol);
				}
				
				String id = resultSet.getString("ID");
				String priority= resultSet.getString("PRIORITY");
				String status = resultSet.getString("STATUS");
				String checksum = resultSet.getString("CHECKSUM");
				String workDir = resultSet.getString("WORK_DIR");
				String targetFile = resultSet.getString("TARGET_FILE");
				long size = resultSet.getLong("SIZE");
				Date requestTime = resultSet.getTimestamp("REQUEST_TIME");
				Date commencementTime = resultSet.getTimestamp("COMMENCEMENT_TIME");
				Date completionTime = resultSet.getTimestamp("COMPLETION_TIME");
				
				
				download.setID(id);
				download.setPriority(Download.PRIORITY.valueOf(priority));
				download.setStatus(Download.STATUS.valueOf(status));
				download.setExpectedChecksum(checksum);
				download.setWorkDir(new File(workDir));
				download.setFile(new File(targetFile));
				download.setTotalFileLength(size);
				download.setDownloadRequestTime(requestTime);
				download.setCommencementTime(commencementTime);
				download.setCompletionTime(completionTime);
				
				
				
				
				
				downloads.add(download);
			}

			return downloads ;
		}

	}

	/**
	 * @author vipul
	 *
	 */
	public class PopulateDownloadWithChunks_RSH implements ResultSetHandler {
		
		private Download download = null;

		public PopulateDownloadWithChunks_RSH(Download download) {
			this.download = download;
		}

		/**
		 * 
		 */
		public Object handle(ResultSet resultSet) throws SQLException {
			List<ChunkDownload> chunks = new ArrayList<ChunkDownload>();

			while(resultSet.next()) {
				
				int id = resultSet.getInt("ID");
				String downloadID = resultSet.getString("DOWNLOAD_ID");
				String status = resultSet.getString("STATUS");
				String filePath = resultSet.getString("FILE_PATH");
				long size = resultSet.getLong("SIZE");
				long beginRange = resultSet.getLong("BEGIN_RANGE");
				long endRange = resultSet.getLong("END_RANGE");

				ChunkDownload chunk = download.new ChunkDownload();
				chunk.setBeginRange(beginRange);
				chunk.setEndRange(endRange);
				chunk.setChunkFilePath(filePath);
				chunk.setId(id);
				chunk.setSize(size);
				chunk.setStatus(Download.STATUS.valueOf(status));
				
				chunks.add(chunk);
				
			}
			
			download.setChunks(chunks);
			download.resetPendingChunkCount();
			return null;
		}

	}


	/**
	 * @author vipul
	 *
	 */
	public class PopulateDownloadWithUrls_RSH implements ResultSetHandler {

		Download download = null;
		
		/**
		 * 
		 * @param download
		 */
		public PopulateDownloadWithUrls_RSH(Download download) {
			this.download = download;
		}


		/**
		 * 
		 */
		public Object handle(ResultSet resultSet) throws SQLException {
			List<URL> urlList = new ArrayList<URL>();

			while(resultSet.next()) {
				
				String urlStr= resultSet.getString("URL");
				try {
					URL url = new URL(urlStr);
					urlList.add(url);
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new SQLException("Invalid URL : "+urlStr);
				}
				
			}
			URL[] urls = new URL[urlList.size()];
			urls = urlList.toArray(urls);
			download.setUrls(urls);
			
			return null;
		}

	}
}
