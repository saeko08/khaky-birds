/**
 * 
 */
package com.hypefoundry.engine.util.serialization;

import java.io.OutputStream;
import com.hypefoundry.engine.util.serialization.WorldFileSaver;


/**
 * Saver of a world file section.
 * 
 * @author Paksas
 */
public interface WorldFileSaver 
{
	/**
	 * Flushes the saver contents to an output stream.
	 * 
	 * @param stream
	 */
	void flush( OutputStream stream );
	
	/**
	 * Saves a string value.
	 * 
	 * @param id				element id
	 * @param value
	 */
	void setStringValue( String id, String value );
	
	/**
	 * Saves an integer value.
	 * 
	 * @param id				element id
	 * @param value
	 */
	void setIntValue( String id, int value );
	
	/**
	 * Saves an float value.
	 * 
	 * @param id				element id
	 * @param value
	 */
	void setFloatValue( String id, float value );
		
	/**
	 * Adds a new child node with the specified tag.
	 * 
	 * @param id
	 * @return
	 */
	WorldFileSaver addChild( String id );
	
}
