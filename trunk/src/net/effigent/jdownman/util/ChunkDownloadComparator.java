/**
 * 
 */
package net.effigent.jdownman.util;

import java.util.Comparator;

import net.effigent.jdownman.Download.ChunkDownload;;

/**
 * @author vipul
 *
 */
//public class ChunkDownloadComparator implements Comparator<ChunkDownload> {
//look at the link below to figure out why it's a Runnable and not ChunkDownload anymore 
//http://forum.java.sun.com/thread.jspa?forumID=534&threadID=749191
public class ChunkDownloadComparator implements Comparator<Runnable> {

	/**
	 * 
	 */
	public ChunkDownloadComparator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public int compare(Runnable r1, Runnable r2) {
		//note this may throw a ClassCastException .. but what can we do when there are limitations
		//http://forum.java.sun.com/thread.jspa?forumID=534&threadID=749191
		ChunkDownload o1 = (ChunkDownload)r1;
		ChunkDownload o2 = (ChunkDownload)r2;
		
		if(o1.getParentDownload().getPriority().getValue() > o2.getParentDownload().getPriority().getValue())
			return -1;
		if(o1.getParentDownload().getPriority().getValue() < o2.getParentDownload().getPriority().getValue())
			return 1;

		if(o1.getParentDownload().getDownloadRequestTime().after(o1.getParentDownload().getDownloadRequestTime()))
			return 1;
		else
			return -1;
		
	}

}
