/**
 * 
 */
package com.hypefoundry.engine.math;

/**
 * 
 * A mathematical ray.
 * 
 * @author Paksas
 */
public final class Ray
{
	public Vector3		m_origin;
	private Vector3		m_direction;
	private float 		m_length;
	
	
	/**
	 * Constructor.
	 * 
	 * @param ox	origin x
	 * @param oy	origin y
	 * @param oz	origin z
	 * @param dx	direction x
	 * @param dy	direction y
	 * @param dz	direction z
	 */
	public Ray( float ox, float oy, float oz, float dx, float dy, float dz )
	{
		m_origin		= new Vector3( ox, oy, oz );
		m_direction		= new Vector3( dx, dy, dz );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	public Ray( Vector3 origin, Vector3 direction )
	{
		m_origin = origin;
		m_direction = direction;
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Sets a new direction of the ray.
	 * 
	 * @param direction
	 */
	public void setDirection( final Vector3 direction )
	{
		m_direction.set( direction );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Sets a new direction of the ray.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setDirection( float dx, float dy, float dz )
	{
		m_direction.set( dx, dy, dz );
		m_length = m_direction.mag();
		m_direction.normalize();
	}
	
	/**
	 * Returns the direction of the ray.
	 *  
	 * @return
	 */
	public Vector3 getDirection()
	{
		return m_direction;
	}
	
	/**
	 * Returns the length of the ray.
	 * 
	 * @return
	 */
	public float getLength() 
	{
		return m_length;
	}
}
