/**
 * 
 */
package com.hypefoundry.engine.util.serialization;


/**
 * Reader of a world file section.
 * 
 * @author Paksas
 */
public interface DataLoader 
{
	/**
	 * Reads a string value from the node.
	 * 
	 * @param id				element id
	 * @return
	 */
	String getStringValue( String id );
	
	/**
	 * Reads a boolean value.
	 * 
	 * @param id				element id
	 * @return
	 */
	boolean getBoolValue( String id );
	
	/**
	 * Reads a boolean value and returns a default value if the attribute is not found.
	 * 
	 * @param id				element id
	 * @param defaultValue
	 * @return
	 */
	boolean getBoolValue( String id, boolean defaultValue );
	
	/**
	 * Reads an integer value from the node.
	 * 
	 * @param id				element id
	 * @return
	 */
	int getIntValue( String id );
	
	/**
	 * Reads an integer value from the node and returns a default value if the attribute is not found.
	 * 
	 * @param id				element id
	 * @return
	 */
	int getIntValue( String id, int defaultValue );
	
	/**
	 * Reads a float value from the node.
	 * 
	 * @param id				element id
	 * @return
	 */
	float getFloatValue( String id );
	
	/**
	 * Reads a float value and returns a default value if the attribute is not found.
	 * 
	 * @param id				element id
	 * @return
	 */
	float getFloatValue( String id, float defaultValue );
	
	/**
	 * Returns the number of children with the specified id.
	 * 
	 * @param id
	 * @return
	 */
	int getChildrenCount( String id );
	
	/**
	 * Returns a child node with the specified tag.
	 * 
	 * @param id
	 * @return
	 */
	DataLoader getChild( String id );
	
	/**
	 * If the parent node has many nodes with a similar tag, then this
	 * one will return the next in line.
	 * 
	 * @return
	 */	DataLoader getSibling();
}
