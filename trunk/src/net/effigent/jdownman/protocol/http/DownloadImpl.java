/**
 * 
 */
package net.effigent.jdownman.protocol.http;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;
import net.effigent.jdownman.DownloadMonitor;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.log4j.Logger;

/**
 * Download Object for HTTP
 * 
 * @author vipul
 *
 */
public class DownloadImpl extends Download {
	
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(DownloadImpl.class);
	
	@Override
	protected void download(URL url, File destination, long beginRange, long endRange, DownloadMonitor monitor) throws DownloadException{
		
		System.out.println(" DOWNLOAD REQUEST RECEIVED "+url.toString()+" \n\tbeginRange : "+beginRange+" - EndRange "+endRange+" \n\t to -> "+destination.getAbsolutePath());
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected long size() throws DownloadException{
		Long fileLength = -1l;
		URL url = urls[0];
		if(!url.getProtocol().equalsIgnoreCase("http")) {
			throw new DownloadException(" Invalid URL : Protocol is not HTTP. It is : "+url.getProtocol());
		}
		
		HeadMethod head = new HeadMethod(url.toString());
		
		HttpClient httpClient = new HttpClient();

			try {
				httpClient.executeMethod(head);
				Header[] headers = head.getResponseHeaders();
				
//				for(Header header : headers) {
//					System.out.println(header);
//				}
				
				Header header = head.getResponseHeader("Content-Length");
				Object contentLength = header.getValue();
				fileLength = Long.parseLong(contentLength.toString());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unable to figure out the length of the file from the URL : "+e.getMessage());
//				throw new DownloadException("Unable to figure out the length of the file from the URL : "+e.getMessage());
			}

			System.out.println(" File Length at server : "+fileLength);
		
		
		return fileLength;
	}

}
