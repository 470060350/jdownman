/**
 * 
 */
package net.effigent.jdownman.split;

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
	 * @return the maxChunkSize
	 */
	public long getMaxChunkSize() {
		return maxChunkSize;
	}


	/**
	 * @param maxChunkSize the maxChunkSize to set
	 */
	public void setMaxChunkSize(long maxChunkSize) {
		this.maxChunkSize = maxChunkSize;
	}


	/**
	 * 
	 */
	public List<ChunkDownload> split(Download download) {
		
		//create an empty list of chunks
		List<ChunkDownload> chunks = new ArrayList<ChunkDownload>();
		// get the size of the download .. 
		long size = download.getTotalFileLength();

		//if the size of the file is greater than the max-chunk size .... break the download into chunks
		if(size > maxChunkSize) {
			long beginIndex = 0;
			long endIndex = maxChunkSize -1; // i.e. 0-9999 if the chunk size is 10000
			int count=0;
			boolean lastChunk = false;
			while(true) {
				//create a new chunk from the download 
				ChunkDownload chunk = download.new ChunkDownload();
				chunk.setBeginRange(beginIndex);
				chunk.setEndRange(endIndex);
				chunk.setStatus(STATUS.PENDING);
				chunk.setSize(endIndex - beginIndex+1);
				chunk.setId(++count);// assign the ID for this chunk.
				//add the chunk to the list
				chunks.add(chunk);
				//break if this was the last chunk
				if(lastChunk)
					break;
				//if this was not the last chunk .... begin index fo the next chunk is 1 + endIndex of this one 
				beginIndex =  endIndex+1;
				// end index is current chunk's end index plus maxChunkSize
				endIndex = endIndex+maxChunkSize ;

				//BUT .. if the endIndex goes beyond the file size .... 
					//reset end index to size -1 .. AND set the last chunk flag as true
				if(endIndex > size) {
					endIndex = size-1;
					lastChunk = true;
				}
				
			}
			
		}else {
			//if the file size is either undeterminable ... or less than the max chunk size
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
