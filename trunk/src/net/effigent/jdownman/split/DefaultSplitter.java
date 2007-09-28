/**
 * 
 */
package net.effigent.jdownman.split;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;
import net.effigent.jdownman.Download.STATUS;

/**
 * @author vipul
 *
 */
public class DefaultSplitter implements Splitter {
	
	private long maxChunkSize = 1000000l;  


	/**
	 * 
	 */
	public List<ChunkDownload> split(Download download) {
		
		
		List<ChunkDownload> chunks = new ArrayList<ChunkDownload>();
		
		URL[] urls = download.getUrls();
		
		long size = download.getTotalFileLength();

		
		if(size > maxChunkSize) {
			long beginIndex = 0;
			long endIndex = maxChunkSize -1;
			int count=0;
			boolean lastChunk = false;
			while(true) {
				
				ChunkDownload chunk = download.new ChunkDownload();
				chunk.setBeginRange(beginIndex);
				chunk.setEndRange(endIndex);
				chunk.setStatus(STATUS.PENDING);
				chunk.setId(++count);
				chunks.add(chunk);
				
				if(lastChunk)
					break;
				
				beginIndex =  endIndex+1;
				endIndex = endIndex+maxChunkSize ;

				if(endIndex > size) {
					endIndex = size-1;
					lastChunk = true;
				}
				
			}
			
			
			
		}else {
			ChunkDownload chunk = download.new ChunkDownload();
			chunk.setBeginRange(0);
			chunk.setEndRange(size-1);
			chunk.setStatus(STATUS.PENDING);
			chunk.setId(1);
			chunks.add(chunk); 
			
		}
		

		return chunks ;
	}

}
