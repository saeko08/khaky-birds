package com.hypefoundry.engine.util;

import java.util.ArrayList;
import java.util.List;


/**
 * A pool of objects.
 * 
 * @author paksas
 *
 * @param <T>
 */
public class Pool<T> 
{
	private final List<T> 				m_freeObjects;
	private final PoolObjectFactory<T> 	m_factory;
	private final int	 				m_maxSize;
	
	/**
	 * Constructor.
	 * 
	 * @param factory			the factory that will be creating the objects in this pool
	 * @param maxSize			how many objects can the pool contain at most
	 */
	public Pool( PoolObjectFactory<T> factory, int maxSize ) 
	{
		m_factory = factory;
		m_maxSize = maxSize;
		m_freeObjects = new ArrayList<T>( maxSize );
	}
	
	/**
	 * Returns an instance of an object from the pool.
	 * 
	 * @return
	 */
	public T newObject() 
	{
		T object = null;
		if ( m_freeObjects.size() == 0 )
		{
			object = m_factory.createObject();
		}
		else
		{
			object = m_freeObjects.remove( m_freeObjects.size() - 1 );
		}
		return object;
	}
	
	/**
	 * Frees up an object created in the pool.
	 * 
	 * @param object
	 */
	public void free( T object ) 
	{
		if ( m_freeObjects.size() < m_maxSize )
		{
			m_freeObjects.add(object);
		}
	}
	
	
}
