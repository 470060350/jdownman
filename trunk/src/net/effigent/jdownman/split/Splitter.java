/**
 * 
 */
package net.effigent.jdownman.split;

import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;

/**
 * @author vipul
 *
 */
public interface Splitter {

	/**
	 * This method accepts a download object and populates it 
	 * with the downloadable chunks ... 
	 * 
	 * @param download
	 * @return
	 */
	List<ChunkDownload> split(Download download);

	/**
	 * @return the maxChunkSize
	 */
	public long getMaxChunkSize() ;

}
