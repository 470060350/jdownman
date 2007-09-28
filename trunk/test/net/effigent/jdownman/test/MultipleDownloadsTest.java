/**
 * 
 */
package net.effigent.jdownman.test;


import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.impl.DownloadManagerImpl;
import net.effigent.jdownman.queue.NonPersistentDownloadQueue;
import net.effigent.jdownman.util.TimestampBasedUIDGenerator;

import org.junit.After;
import org.junit.Before;

/**
 * @author vipul
 *
 */
public class MultipleDownloadsTest {

	DownloadManager downloadManager= null;
	File destinationFile = null;
	

	URL[] urls = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DownloadManagerImpl dmi = new DownloadManagerImpl();
		dmi.setWorkDirPath("../test/BASE_FOLDER");
		dmi.setUidGenerator(new TimestampBasedUIDGenerator());
		dmi.setDownloadQueue(new NonPersistentDownloadQueue());
		dmi.initialize();
		downloadManager = dmi;
		//downloadManager.
		destinationFile = new File("TEST_FILE");
		urls = new URL[1];
		urls[0] = new URL("http://vipuls-macbook.local/repository/planet/9729/75/em.pdf.hexdump"); 
		
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

}
