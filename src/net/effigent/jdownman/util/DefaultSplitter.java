/**
 * 
 */
package net.effigent.jdownman.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;

/**
 * @author vipul
 *
 */
public class DefaultSplitter implements Splitter {

	/* (non-Javadoc)
	 * @see net.effigent.jdownman.util.Splitter#split(net.effigent.jdownman.Download)
	 */
	public List<ChunkDownload> split(Download download) {
		
		
		URL[] urls = download.getUrls();
		ChunkDownload chunk = download.new ChunkDownload();

		chunk.setBeginRange(0);
		chunk.setEndRange(-1);
		chunk.setId(1);
		chunk.setUrl(urls[0]);
		List<ChunkDownload> chunks = new ArrayList<ChunkDownload>();
		chunks.add(chunk);

		return chunks ;
	}

}
