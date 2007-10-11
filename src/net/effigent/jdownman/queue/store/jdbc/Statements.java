/**
 * 
 */
package net.effigent.jdownman.queue.store.jdbc;

import net.effigent.jdownman.Download;

/**
 * @author vipul
 *
 */
public class Statements {

	protected String downloadTableName = "JDM_DOWNLOAD";
	protected String downloadUrlsTableName = "JDM_DOWNLOAD_URLS";
    protected String chunkTableName = "JDM_CHUNK";
    
    protected String insertDownloadStatememnt = null;
    protected String insertChunkStatememnt = null;
    protected String updateDownloadStatusStatememnt = null;
    protected String updateChunkStatusStatememnt = null;
    protected String setDownloadSizeStatememnt = null;
    protected String saveExceptionMessageStatement = null;
    protected String getPendingDownloadsStatement = null;
    protected String getChunksForDownloadStatement = null;
    protected String insertUrlsStatement = null;
    protected String getUrlsForDownloadStatement = null;
    protected String deleteDownloadStatement = null;
    protected String deleteChunksStatement = null;
    protected String deleteUrlsStatement = null;

    /**
     * 
     * @return
     */
    public String getInsertDownloadStatement() {
    	if(insertDownloadStatememnt == null) {
    		insertDownloadStatememnt = "INSERT INTO "+downloadTableName+" (" +
    				"ID," +
    				"PRIORITY," +
    				"STATUS," +
    				"CHECKSUM," +
    				"WORK_DIR," +
    				"TARGET_FILE," +
    				"SIZE," +
    				"REQUEST_TIME," +
    				"COMMENCEMENT_TIME," +
    				"COMPLETION_TIME, " +
    				"PROTOCOL " +
    				") values(?,?,?,?,?,?,?,?,?,?,?)";
    	}
    	return insertDownloadStatememnt;
    }
    
    /**
     * 
     * @return
     */
    public String getInsertChunkStatement() {
    	if(insertChunkStatememnt == null) {
    		insertChunkStatememnt = "INSERT INTO "+chunkTableName+" (" +
    				"ID," +
    				"DOWNLOAD_ID," +
    				"STATUS," +
    				"FILE_PATH," +
    				"SIZE," +
    				"BEGIN_RANGE," +
    				"END_RANGE" +
    				") values(?,?,?,?,?,?,?)";
    	}
    	return insertChunkStatememnt;
    }
    
    
    /**
     * 
     * @return
     */
    public String getUpdateDownloadStatusStatement() {
    	if(updateDownloadStatusStatememnt == null) {
    		updateDownloadStatusStatememnt = " UPDATE "+downloadTableName+" " +
    				"set STATUS = ?"+
    				" WHERE ID = ? ";
    	}
    	return updateDownloadStatusStatememnt;
    }
    
    /**
     * 
     * @return
     */
    public String getUpdateChunkStatusStatement() {
    	if(updateChunkStatusStatememnt == null) {
    		updateChunkStatusStatememnt = " UPDATE "+chunkTableName+" " +
    				" SET STATUS = ?" +
    				" WHERE ID = ? " +
    				" AND DOWNLOAD_ID = ?";
    	}
    	return updateChunkStatusStatememnt;
    }
    
    /**
     * 
     * @return
     */
    public String getSetDownloadSizeStatement() {
    	if(setDownloadSizeStatememnt == null) {
    		setDownloadSizeStatememnt = " UPDATE "+downloadTableName+ 
    									" SET SIZE = ? " +
    									" WHERE ID = ?";
    			 
    	}
    	return setDownloadSizeStatememnt;
    }

    /**
     * 
     * @return
     */
    public String getSaveExceptionMessageStatement() {
		
    	if(saveExceptionMessageStatement == null) {
    		saveExceptionMessageStatement = " UPDATE "+downloadTableName+ 
    									" SET EXCEPTION = ? " +
    									" WHERE ID = ?";
    			 
    	}
    	return saveExceptionMessageStatement;
		
	}

    /**
     * 
     * @return
     */
	public String getGetPendingDownloadsStatement() {
		
    	if(getPendingDownloadsStatement== null) {
    		getPendingDownloadsStatement = " SELECT "+
				"PROTOCOL," +
				"ID," +
				"PRIORITY," +
				"STATUS," +
				"CHECKSUM," +
				"WORK_DIR," +
				"TARGET_FILE," +
				"SIZE," +
				"REQUEST_TIME," +
				"COMMENCEMENT_TIME," +
				"COMPLETION_TIME " +
				" FROM " + downloadTableName+ 
				" WHERE STATUS <> '"+Download.STATUS.COMPLETE.name()+"' " +
				" ORDER BY ID ";
    	}
    	return getPendingDownloadsStatement;
	}
	/**
	 * 
	 * @return
	 */
	public String getGetChunksForDownloadStatement() {
		
    	if(getChunksForDownloadStatement == null) {
    		getChunksForDownloadStatement = " SELECT "+
				"ID," +
				"DOWNLOAD_ID," +
				"STATUS," +
				"FILE_PATH," +
				"SIZE," +
				"BEGIN_RANGE," +
				"END_RANGE " +
				" FROM " + chunkTableName + 
				" WHERE DOWNLOAD_ID = ? " +
				" ORDER BY ID ";
    	}
    	return getChunksForDownloadStatement;
		
	}

	/**
	 * 
	 * @return
	 */
	public String getInsertUrlsStatement() {
		
    	if(insertUrlsStatement == null) {
    		insertUrlsStatement = " INSERT INTO " +
    				downloadUrlsTableName+
    				" VALUES(?,?)";
    	}
    	return insertUrlsStatement;
		
	}
    
	/**
	 * 
	 * @return
	 */
	public String getGetUrlsForDownloadStatement() {
		
    	if(getUrlsForDownloadStatement == null) {
    		getUrlsForDownloadStatement = " SELECT "+
    			" URL from "+downloadUrlsTableName+
				" WHERE DOWNLOAD_ID = ? ";
    	}
    	return getUrlsForDownloadStatement;
	}

	/**
	 * 
	 * @return
	 */
	public String getDeleteDownloadStatement() {
		
    	if(deleteDownloadStatement == null) {
    		deleteDownloadStatement = " DELETE "+
    			" FROM "+downloadTableName+
				" WHERE ID = ? ";
    	}
    	return deleteDownloadStatement;
	}

	/**
	 * 
	 * @return
	 */
	public String getDeleteChunksStatement() {

		if(deleteChunksStatement == null) {
    		deleteChunksStatement = " DELETE "+
    			" FROM "+chunkTableName+
				" WHERE DOWNLOAD_ID = ? ";
    	}
    	return deleteChunksStatement;
	}

	/**
	 * 
	 * @return
	 */
	public String getDeleteUrlsStatement() {

		if(deleteUrlsStatement == null) {
			deleteUrlsStatement = " DELETE "+
    			" FROM "+downloadUrlsTableName+
				" WHERE DOWNLOAD_ID = ? ";
    	}
    	return deleteUrlsStatement;
	}
}
