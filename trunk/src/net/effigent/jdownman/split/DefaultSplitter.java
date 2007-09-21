/**
 * 
 */
package net.effigent.jdownman.split;

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


	/**
	 * 
	 */
	public List<ChunkDownload> split(Download download) {
		
		
		List<ChunkDownload> chunks = new ArrayList<ChunkDownload>();
		
		URL[] urls = download.getUrls();

		ChunkDownload chunk = download.new ChunkDownload();
		chunk.setBeginRange(0);
		chunk.setEndRange(100);
		chunk.setId(1);
		chunk.setUrl(urls[0]);
		chunks.add(chunk);

		chunk = download.new ChunkDownload();
		chunk.setBeginRange(101);
		chunk.setEndRange(200);
		chunk.setId(2);
		chunk.setUrl(urls[0]);
		chunks.add(chunk);

		chunk = download.new ChunkDownload();
		chunk.setBeginRange(201);
		chunk.setEndRange(300);
		chunk.setId(3);
		chunk.setUrl(urls[0]);
		chunks.add(chunk);

		return chunks ;
	}

}
