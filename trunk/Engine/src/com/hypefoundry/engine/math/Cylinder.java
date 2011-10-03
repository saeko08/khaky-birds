/**
 * 
 */
package com.hypefoundry.engine.math;

/**
 * Mathematical cylinder representation.
 * 
 * @author Paksas
 */
public class Cylinder
{
	/**
	 * Sets the cylinder parameters.
	 * 
	 * @param origin
	 * @param end
	 * @param radius
	 */
	public void set( Vector3 origin, Vector3 end, float radius )
	{
		
	}
	
	/**
	 * Checks if a ray overlaps the cylinder.
	 * 
	 * @param ray
	 * @return
	 */
	public boolean doesOverlap( Ray ray )
	{
		return false;
	}
	
	/**
	 * Checks if a bounding box overlaps the cylinder.
	 * 
	 * @param box
	 * @return
	 */
	public boolean doesOverlap( BoundingBox box )
	{
		return false;
	}
}
