package net.effigent.jdownman.test;


import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.bind.SimpleBinder;
import net.effigent.jdownman.config.ThreadingProfile;
import net.effigent.jdownman.impl.DownloadManagerImpl;
import net.effigent.jdownman.queue.NonPersistentDownloadQueue;
import net.effigent.jdownman.util.TimestampBasedUIDGenerator;
import net.effigent.jdownman.work.DownloadWorkManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DownloadTest {

	DownloadManager downloadManager= null;
	URL[] urls = null;

	@Before
	public void setUp() throws Exception {
		DownloadManagerImpl dmi = new DownloadManagerImpl();
		dmi.setWorkDirPath("../test/BASE_FOLDER");
		dmi.setUidGenerator(new TimestampBasedUIDGenerator());
		dmi.setDownloadQueue(new NonPersistentDownloadQueue());
		dmi.setBinder(new SimpleBinder());
		
		ThreadingProfile threadingProfile = new ThreadingProfile();
		threadingProfile.setMaxThreadsActive(6);
		threadingProfile.setMaxThreadsIdle(6);
		
		DownloadWorkManager workManager = new DownloadWorkManager(threadingProfile);
		dmi.setWorkManager(workManager);
		dmi.initialize();
		downloadManager = dmi;
		//downloadManager.
		urls = new URL[1];
//		urls[0] = new URL("http://vipuls-macbook.local/repository/planet/9729/75/em.pdf.hexdump"); 
//		urls[0] = new URL("http://download.gigahost123.com/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
//		urls[0] = new URL("http://localhost:8888/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
		urls[0] = new URL("http://localhost/repository/planet/9729/75/vipul.jpg"); 
		
		
	}
	
	
	@Test
	public void download() throws Exception{
		downloadManager.downloadFile(new File("TEST_FILE0.jpg"), urls,PRIORITY.NORMAL);
		downloadManager.downloadFile(new File("TEST_FILE1.jpg"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("TEST_FILE2.jpg"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE3.jpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE4.jpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE5.jpg"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE6.jpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE7.jpg"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("TEST_FILE8.jpg"), urls,PRIORITY.LOW_PRIORITY);
		
		System.in.read();
		while(true)
			Thread.sleep(10000);
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
