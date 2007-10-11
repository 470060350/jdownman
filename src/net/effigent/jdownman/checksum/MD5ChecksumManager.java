/**
 * 
 */
package net.effigent.jdownman.checksum;

import java.io.File;


/**
 * @author vipul
 *
 */
public class MD5ChecksumManager implements ChecksumManager {
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public Object calculateFileChecksum(File file) {
		return new String(file.getName());
	}
	
	
	/**
	 * 
	 * @param checksumObject
	 * @return
	 */
	public String toChecksumString(Object checksumObject) {
		return checksumObject.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public Object toChecksumObject(String checksumString) {
		return checksumString;
	}
	
	/**
	 * 
	 * @param checksum1
	 * @param checksum2
	 * @return
	 */
	public boolean compareEqual(Object checksum1, Object checksum2) {
		return checksum1.toString().equalsIgnoreCase(checksum2.toString());
	}

}
