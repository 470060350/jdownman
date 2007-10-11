/**
 * 
 */
package net.effigent.jdownman.checksum;

import java.io.File;

/**
 * @author vipul
 *
 */
public interface ChecksumManager {
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public Object calculateFileChecksum(File file);
	
	
	/**
	 * 
	 * @param checksumObject
	 * @return
	 */
	public String toChecksumString(Object checksumObject) ;
	
	/**
	 * 
	 * @return
	 */
	public Object toChecksumObject(String string) ;
	
	/**
	 * 
	 * @param checksum1
	 * @param checksum2
	 * @return
	 */
	public boolean compareEqual(Object checksum1, Object checksum2);

}
