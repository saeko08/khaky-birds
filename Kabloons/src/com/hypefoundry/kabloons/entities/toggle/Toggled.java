/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

/**
 * A marker interface that describes an entity the state of which can be toggled by a toggle.
 * @author Paksas
 *
 */
public interface Toggled 
{
	/**
	 * Tells if the entity is switched on or off.
	 * 
	 * @return
	 */
	boolean isSwitchedOn();
	
	/**
	 * Switches the state of controlled entity.
	 */
	void toggle();

	/**
	 * Returns a tag of the device. A respective toggle that operates
	 * this entity will be marked with the same tag.
	 */
	String getTag();
}
