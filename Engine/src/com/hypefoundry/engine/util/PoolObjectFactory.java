package com.hypefoundry.engine.util;

/**
 * This factory will create objects and place them in a dedicated pool,
 * so that their instances can be reused instead of being garbaged collected.
 * 
 * It's a way to create objects that are very often created and destroyed, 
 * like event descriptions etc.
 * 
 * @author paksas
 *
 * @param <T>
 */
public interface PoolObjectFactory<T> 
{
	/**
	 * Creates an object in a pool.
	 * 
	 * @return
	 */
	public T createObject();
}
