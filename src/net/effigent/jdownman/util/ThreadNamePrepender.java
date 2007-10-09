/**
 * 
 */
package net.effigent.jdownman.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author vipul
 *
 */
public class ThreadNamePrepender  extends PrintStream{

	private boolean print = false;
	
	/**
	 * 
	 * @param out
	 * @param print
	 */
	public ThreadNamePrepender(OutputStream out,boolean print) {
		super(out);
		this.print = print;
		// TODO Auto-generated constructor stub
	}

	 /**
	  * 
	  * @param out
	  * @param print
	  */
	public ThreadNamePrepender(OutputStream out) {
		super(out);
		this.print = true;
	}

	/**
	 * 
	 */
	public void println(String str) {
		
		if(print) {
			str = Thread.currentThread().getName() + " : " + str;
			super.println(str);
		}
		return;
	}

}
