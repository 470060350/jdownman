package net.effigent.jdownman.test;


import java.io.File;
import java.net.URL;

import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;
import net.effigent.jdownman.bind.Binder;
import net.effigent.jdownman.bind.SimpleBinder;
import net.effigent.jdownman.config.ThreadingProfile;
import net.effigent.jdownman.impl.DownloadManagerImpl;
import net.effigent.jdownman.queue.PersistentQueue;
import net.effigent.jdownman.queue.store.jdbc.JDBCPersistenceAdapter;
import net.effigent.jdownman.split.DefaultSplitter;
import net.effigent.jdownman.util.ThreadNamePrepender;
import net.effigent.jdownman.util.TimestampBasedUIDGenerator;
import net.effigent.jdownman.work.DownloadWorkManager;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DownloadTest {
	
	public static void main(String[] args) throws Exception {
		DownloadTest test = new DownloadTest();
		test.setUp();
		test.download();
	}

	DownloadManager downloadManager= null;
	URL[] urls = null;

	@Before
	public void setUp() throws Exception {

		ClassPathXmlApplicationContext appContext= new ClassPathXmlApplicationContext("downloadTest-appContext.xml"); 
		
		
		System.setOut(new ThreadNamePrepender(System.out));

		downloadManager = (DownloadManager)appContext.getBean("downloadManager");
		
		urls = new URL[1];
//		urls[0] = new URL("http://vipuls-macbook.local/repository/planet/9729/75/em.pdf.hexdump"); 
//		urls[0] = new URL("http://download.gigahost123.com/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
//		urls[0] = new URL("http://localhost:8888/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
		urls[0] = new URL("http://download.gigahost123.com/songs/Audio/indian/movies/Om%20Shanti%20Om%20(2007)/02%20-%20Dard-E-Disco%20-%20Sukhwinder%20Singh,%20Marianne,%20Nisha%20&%20Caralisa%20@%20Fmw11.com.mp3"); 
//		urls[0] = new URL("http://localhost/repository/planet/9729/75/Federer.mpg"); 
//		urls[1] = new URL("http://localhost/repository/satellite/9729/75/Federer111.mpg"); 
			
		
	}
	
	
	@Test
	public void download() throws Exception{
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE0_N.mpg"), urls,PRIORITY.NORMAL);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE1_U.mpg"), urls,PRIORITY.URGENT);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE2_H.mpg"), urls,PRIORITY.HIGH_PRIORITY);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE3_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE4_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE5_H.mpg"), urls,PRIORITY.HIGH_PRIORITY);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE6_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE7_U.mpg"), urls,PRIORITY.URGENT);
//		downloadManager.downloadFile(new File("OUTPUT/TEST_FILE8_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
		
		downloadManager.downloadFile(new File("OUTPUT/Song0_N.mp3"), urls,PRIORITY.NORMAL);
		downloadManager.downloadFile(new File("OUTPUT/Song1_U.mp3"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("OUTPUT/Song2_H.mp3"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("OUTPUT/Song3_L.mp3"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("OUTPUT/Song4_L.mp3"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("OUTPUT/Song5_H.mp3"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("OUTPUT/Song6_L.mp3"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("OUTPUT/Song7_U.mp3"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("OUTPUT/Song8_L.mp3"), urls,PRIORITY.LOW_PRIORITY);
		System.in.read();
		while(true)
			Thread.sleep(10000);
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
