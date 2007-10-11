/**
 * 
 */
package net.effigent.jdownman.bind;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;

import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class UnixCatBinder implements Binder {
	
	private static final Logger logger = Logger.getLogger(UnixCatBinder.class);

	
	/**
	 * 
	 */
	public UnixCatBinder() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.bind.Binder#bindDownload(net.effigent.jdownman.Download)
	 */
	public void bindDownload(Download download) throws BindingException {
		//get all the chunks ... it is assumed that they are already sorted wrt their ids
		// 
		List<ChunkDownload> chunks = download.getChunks();
		
		//get the destinationFile 
		File destination = download.getFile();
		if(!destination.exists()) {
			try {
				destination.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to bind the chunks : Unable to create the destination file "+e.getMessage());
				throw new BindingException("Unable to bind the chunks : Unable to create the destination file "+e.getMessage(),e);
			}
		}
		
		
		

		try {
		
			// merge each chunk
			for(ChunkDownload chunk : chunks) {
				String filePath = chunk.getChunkFilePath();
				File chunkFile = new File(filePath);
				String catCommand = "cat "+chunkFile.getAbsolutePath()+" >> "+destination.getAbsolutePath();
				System.out.println("\n\n\t\t$$$$$$$$$$$$$$ Executing CAT command : "+catCommand);
				Process p = Runtime.getRuntime().exec(catCommand);
			
				System.out.println("$$$$$$$$$ Executed CAT " + p.waitFor()+"\n\n");
				
				chunkFile.delete();
			}
			//delete the work directory.
			download.getWorkDir().delete();
			// now the download is complete.
			download.complete();
		} catch (IOException e) {
			logger.error("IO Exception while copying the chunk "+e.getMessage(),e);
			e.printStackTrace();
			throw new BindingException("IO Exception while copying the chunk "+e.getMessage(),e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException while copying the chunk "+e.getMessage(),e);
			e.printStackTrace();
			throw new BindingException("InterruptedException while copying the chunk "+e.getMessage(),e);
		}

	}

}
