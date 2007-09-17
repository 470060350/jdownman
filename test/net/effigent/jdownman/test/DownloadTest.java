package net.effigent.jdownman.test;


import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.impl.DownloadManagerImpl;
import net.effigent.jdownman.util.TimestampBasedUIDGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DownloadTest {

	DownloadManager downloadManager= null;
	File destinationFile = null;
	URL[] urls = null;

	@Before
	public void setUp() throws Exception {
		DownloadManagerImpl dmi = new DownloadManagerImpl();
		dmi.setWorkDirPath("../test/BASE_FOLDER");
		dmi.setUidGenerator(new TimestampBasedUIDGenerator());

		dmi.initialize();
		downloadManager = dmi;
		//downloadManager.
		destinationFile = new File("TEST_FILE");
		urls = new URL[1];
		urls[0] = new URL("http://localhost/repository/planet/9729/75/ar.mpg"); 
	}
	
	
	@Test
	public void download() throws Exception{
		downloadManager.downloadFile(destinationFile, urls,PRIORITY.NORMAL);
		downloadManager.downloadFile(new File("TEST_FILE1"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("TEST_FILE2"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE3"), urls,PRIORITY.LOW_PRIORITY);
		
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
