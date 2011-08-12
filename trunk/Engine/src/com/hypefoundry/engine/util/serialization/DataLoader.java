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
	 * Reads an integer value from the node.
	 * 
	 * @param id				element id
	 * @return
	 */
	int getIntValue( String id );
	
	/**
	 * Reads a float value from the node.
	 * 
	 * @param id				element id
	 * @return
	 */
	float getFloatValue( String id );
	
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
