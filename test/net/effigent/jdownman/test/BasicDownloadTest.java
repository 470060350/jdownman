package net.effigent.jdownman.test;

/**
 * 
 */
import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.impl.SimpleDownloadManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author vipul
 *
 */
public class BasicDownloadTest {

	
	DownloadManager downloadManager = null;
	File destinationFile = null;
	URL[] urls = null;
	
	@Before
	public void setUp() throws Exception {
		downloadManager = new SimpleDownloadManager();
		destinationFile = new File("TEST_FILE");
		urls = new URL[1];
		urls[0] = new URL("http://localhost/repository/planet/9729/75/ar.mpg"); 
	}
	
	/**
	 * @throws DownloadException 
	 * 
	 *
	 */
	@Test
	public void downloadFile() throws DownloadException {
		downloadManager.downloadFile(destinationFile, urls,PRIORITY.NORMAL);
		
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
