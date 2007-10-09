package net.effigent.jdownman.test;


import java.io.File;
import java.net.URL;

import javax.sql.DataSource;

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


public class DownloadTest {

	DownloadManager downloadManager= null;
	URL[] urls = null;

	@Before
	public void setUp() throws Exception {

		System.setOut(new ThreadNamePrepender(System.out));

		DownloadManagerImpl dmi = new DownloadManagerImpl();
		dmi.setWorkDirPath("../test/BASE_FOLDER");
		dmi.setUidGenerator(new TimestampBasedUIDGenerator());
//		dmi.setDownloadQueue(new NonPersistentDownloadQueue());
		
		
		Binder binder = new SimpleBinder();
		
		PersistentQueue queue = new PersistentQueue();
		queue.setBinder(binder);

//		queue.setPersistenceAdapter(new FileBasedPersister(new File("../test/jdm_persistence")));
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost/jdm");
		dataSource.setUsername("jdm");
		dataSource.setPassword("jdm");
		
		
		JDBCPersistenceAdapter persistenceAdapter = new JDBCPersistenceAdapter(dataSource);
		persistenceAdapter.setBinder(binder);
	
		queue.setPersistenceAdapter(persistenceAdapter);
		
		dmi.setDownloadQueue(queue);
		queue.initialize();

		dmi.setBinder(binder);
		
		
		ThreadingProfile threadingProfile = new ThreadingProfile();
		threadingProfile.setMaxThreadsActive(100);
		threadingProfile.setMaxThreadsIdle(26);
		
		DownloadWorkManager workManager = new DownloadWorkManager(threadingProfile);
		dmi.setWorkManager(workManager);
		
		DefaultSplitter splitter = new DefaultSplitter();
		splitter.setMaxChunkSize(1000000l);
		dmi.setSplitter(splitter);
		
		dmi.initialize();
		downloadManager = dmi;
		//downloadManager.
		urls = new URL[1];
//		urls[0] = new URL("http://vipuls-macbook.local/repository/planet/9729/75/em.pdf.hexdump"); 
//		urls[0] = new URL("http://download.gigahost123.com/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
//		urls[0] = new URL("http://localhost:8888/songs/Audio/indian/movies/bombay/Theme%20Music.mp3"); 
//		urls[0] = new URL("http://localhost/repository/planet/9729/75/vipul.jpg"); 
		urls[0] = new URL("http://localhost/repository/planet/9729/75/Federer.mpg"); 
			
		
	}
	
	
	@Test
	public void download() throws Exception{
		downloadManager.downloadFile(new File("TEST_FILE0_N.mpg"), urls,PRIORITY.NORMAL);
		downloadManager.downloadFile(new File("TEST_FILE1_U.mpg"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("TEST_FILE2_H.mpg"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE3_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE4_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE5_H.mpg"), urls,PRIORITY.HIGH_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE6_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
		downloadManager.downloadFile(new File("TEST_FILE7_U.mpg"), urls,PRIORITY.URGENT);
		downloadManager.downloadFile(new File("TEST_FILE8_L.mpg"), urls,PRIORITY.LOW_PRIORITY);
		
		System.in.read();
		while(true)
			Thread.sleep(10000);
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
