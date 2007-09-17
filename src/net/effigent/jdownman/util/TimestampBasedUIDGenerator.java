/**
 * 
 */
package net.effigent.jdownman.util;

/**
 * @author vipul
 *
 */
public class TimestampBasedUIDGenerator implements UIDGenerator {

 
	/* (non-Javadoc)
	 * @see net.effigent.jdownman.util.UIDGenerator#generateNewUID()
	 */
	public String generateNewUID() {
		return Long.toString(System.currentTimeMillis());
	}

}
