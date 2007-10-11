/**
 * 
 */
package net.effigent.jdownman.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadListener;
import net.effigent.jdownman.DownloadManager;
import net.effigent.jdownman.Download.PRIORITY;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.io.IOUtils;

/**
 * @author vipul
 *
 */
public class SimpleDownloadManager extends AbstractDownloadManager implements DownloadManager {

 	/**
	 * This is a very simple implementation of DownloadManager that
	 * - can be used for unit testing
	 * - entertains only HTTP URLs
	 * - downloads file only in one single thread
	 * - downloads once, no failure recovery or anything
	 * - no checksum check
	 */
	public void downloadFile(File destinationFile, URL[] urls,
			DownloadListener listener, Object checksum,long length,PRIORITY priority) throws DownloadException{
		
		URL url = urls[0];
		if(!url.getProtocol().equalsIgnoreCase("http")) {
			throw new DownloadException(" Only HTTP is supported in this version ");
		}
		
		if(!destinationFile.exists()) {
			try {
				destinationFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new DownloadException("Unable to download from URL : "+url.toString());
			}
		}
		HeadMethod head = new HeadMethod(url.toString());
		
		HttpClient httpClient = new HttpClient();
		try {
			httpClient.executeMethod(head);
			Header[] headers = head.getResponseHeaders();
			
			for(Header header : headers) {
				System.out.println(header);
			}
			
			Header header = head.getResponseHeader("Content-Length");
			Object contentLength = header.getValue();
			Long fileLength = Long.parseLong(contentLength.toString());

			//not doing much with the content-length right now ... 
			System.out.println(length + " : "+fileLength);
			
			
			GetMethod get = new GetMethod(url.toString());
			httpClient.executeMethod(get);
			
			InputStream ins = get.getResponseBodyAsStream();
			FileOutputStream fos = new FileOutputStream(destinationFile);
			
			IOUtils.copy(ins,fos);
	
			System.out.println(" DOWNLOADED FILE");	
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		DownloadManager dm = new SimpleDownloadManager();
		URL[] urls = {new URL("http://localhost/repository/satellite/1000/9000/shrek3.mpg")};
		
		dm.downloadFile(new File("temp"), urls,PRIORITY.NORMAL);
	}

	
}
