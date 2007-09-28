/**
 * 
 */
package net.effigent.jdownman.bind;

import net.effigent.jdownman.Download;

/**
 * @author vipul
 *
 */
public interface Binder {
	
	/**
	 * 
	 * @param download
	 * @throws BindingException
	 */
	public void bindDownload(Download download) throws BindingException;

}
