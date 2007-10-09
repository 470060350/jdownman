/**
 * 
 */
package net.effigent.jdownman.protocol.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.DownloadException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.io.IOUtils;
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
	
	/**
	 * 
	 */
	public String getProtocol() {
		return "http";
	}

	
	/**
	 * 
	 */
	protected void download(URL url, File destination, long beginRange, long endRange, long totalFileSize) throws DownloadException{
		
		System.out.println(" DOWNLOAD REQUEST RECEIVED "+url.toString()+" \n\tbeginRange : "+beginRange+" - EndRange "+endRange+" \n\t to -> "+destination.getAbsolutePath());

		try {
			if(destination.exists()) {
				destination.delete();
			}
			destination.createNewFile();

			GetMethod get = new GetMethod(url.toString());
			HttpClient httpClient = new HttpClient();
			//set the range header
			Header rangeHeader = new Header();
			rangeHeader.setName("Range");
			rangeHeader.setValue("bytes="+beginRange+"-"+endRange);

			get.setRequestHeader(rangeHeader);
			
			//execute the get method
			httpClient.executeMethod(get);

			int statusCode = get.getStatusCode();
			//if status code is in between 400 & 500 .... throw exception
			if(statusCode >= 400 && statusCode < 500)
				throw new DownloadException("The file does not exist in this location : message from server ->  "+statusCode+" "+get.getStatusText());
			//get the response body as stream 
			InputStream input= get.getResponseBodyAsStream();
			//get the output stream for the file 
			OutputStream output= new FileOutputStream(destination);
			//download file
			int length = IOUtils.copy(input, output);
			
			System.out.println(" Length : "+length);
 			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to figure out the length of the file from the URL : "+e.getMessage());
			throw new DownloadException("Unable to figure out the length of the file from the URL : "+e.getMessage());
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

				int statusCode = head.getStatusCode();
				
				if(statusCode >= 400 && statusCode <500)
					throw new DownloadException("The file does not exist in this location : message from server ->  "+statusCode+" "+head.getStatusText());
				
				
				Header header = head.getResponseHeader("Content-Length");
				Object contentLength = header.getValue();
				fileLength = Long.parseLong(contentLength.toString());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unable to figure out the length of the file from the URL : "+e.getMessage());
				throw new DownloadException("Unable to figure out the length of the file from the URL : "+e.getMessage());
			}

			System.out.println(" File Length at server : "+fileLength);
		
		
		return fileLength;
	}


}
