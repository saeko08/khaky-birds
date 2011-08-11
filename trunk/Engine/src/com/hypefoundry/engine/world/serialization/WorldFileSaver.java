/**
 * 
 */
package com.hypefoundry.engine.world.serialization;

import java.io.OutputStream;

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
	
	/**
	 * Adds a new sibling node to the parent. The sibling node will have the same tag
	 * as this node.
	 * 
	 * @return
	 */
	WorldFileSaver addSibling();
}
