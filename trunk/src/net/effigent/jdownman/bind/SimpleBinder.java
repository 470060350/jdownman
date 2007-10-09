/**
 * 
 */
package net.effigent.jdownman.bind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author vipul
 *
 */
public class SimpleBinder implements Binder {

	private static final Logger logger = Logger.getLogger(SimpleBinder.class);
 
	/**
	 * 
	 */
	public void bindDownload(Download download) throws BindingException {
		//get all the chunks ... it is assumed that they are already sorted wrt their ids
		// 
		List<ChunkDownload> chunks = download.getChunks();
		
		//get the destinationFile 
		File destination = download.getFile();
		
		FileOutputStream fos = null;
		try {
			fos = FileUtils.openOutputStream(destination);
		
			// merge each chunk
			for(ChunkDownload chunk : chunks) {
				String filePath = chunk.getChunkFilePath();
				InputStream ins = null;
				try {
					File chunkFile = new File(filePath);
					ins = FileUtils.openInputStream(chunkFile);
					
					IOUtils.copy(ins, fos);
					//delete the chunk after the data has been moved to the actual file.
					chunkFile.delete();
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
						ins.close();
				}
				
			}
			//delete the work directory.
			download.getWorkDir().delete();
			// now the download is complete.
			download.complete();
		} catch (IOException e) {
			logger.error("IO Exception while copying the chunk "+e.getMessage(),e);
			e.printStackTrace();
			throw new BindingException("IO Exception while copying the chunk "+e.getMessage(),e);
		}
		finally {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("IO Exception while copying closing stream of the target file "+e.getMessage(),e);
				e.printStackTrace();
				throw new BindingException("IO Exception while copying closing stream of the target file "+e.getMessage(),e);
			}

		}

	}

}
