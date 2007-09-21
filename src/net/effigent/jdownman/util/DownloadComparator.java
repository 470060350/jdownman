/**
 * 
 */
package net.effigent.jdownman.util;

import java.util.Comparator;

import net.effigent.jdownman.Download;

/**
 * @author vipul
 *
 */
public class DownloadComparator implements Comparator<Download> {

	/**
	 * 
	 */
	public DownloadComparator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public int compare(Download o1, Download o2) {
		if(o1.getPriority().getValue() > o2.getPriority().getValue())
			return -1;
		if(o1.getPriority().getValue() < o2.getPriority().getValue())
			return 1;

		if(o1.getDownloadRequestTime().after(o1.getDownloadRequestTime()))
			return 1;
		else
			return -1;
		
	}

}
