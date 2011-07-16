package com.hypefoundry.engine.util;


/**
 * Creates an instance of a Representation.
 * 
 * @author paksas
 *
 */
public interface ObjectFactory< Template, Representation > 
{
	/**
	 * The factory method.
	 * 
	 * @param parentTemplate
	 * @return
	 */
	Representation instantiate( Template parentTemplate );
}
