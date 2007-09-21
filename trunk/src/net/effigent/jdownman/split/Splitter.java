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
	 * 
	 * @param download
	 * @return
	 */
	List<ChunkDownload> split(Download download);

}
