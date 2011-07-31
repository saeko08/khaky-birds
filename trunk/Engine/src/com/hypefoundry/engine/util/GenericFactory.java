/**
 * 
 */
package com.hypefoundry.engine.util;

import java.util.*;


/**
 * A class that allows to create representations for 
 * a hierarchy of classes.
 * 
 * @author paksas
 *
 */
public class GenericFactory< Template, Representation > 
{
	private class InstantiationDef 
	{
		Class											m_type;
		ObjectFactory< Template, Representation >		m_factory;
		
		InstantiationDef( Class type, ObjectFactory< Template, Representation > factory )
		{
			m_type = type;
			m_factory = factory;
		}
		
	}
	private List< InstantiationDef > 		m_definitions;
	
	/**
	 * Constructor.
	 */
	public GenericFactory()
	{
		m_definitions = new ArrayList< InstantiationDef >();
	}
	
	/**
	 * Registers the class of a visual that should be created
	 * when an entity of the specified type is added.
	 * 
	 * @param type
	 * @param factory
	 */
	public void register( Class type, ObjectFactory< Template, Representation > factory )
	{
		for ( InstantiationDef definition : m_definitions )
		{
			if ( definition.m_type.equals( type ) )
			{
				// a definition already exists - change it
				definition.m_factory = factory;
				return;
			}
		}
		
		// if we got so far, it means there definition wasn't found - so create one
		m_definitions.add( new InstantiationDef( type, factory ) );
	}
	
	/**
	 * Creates a representation for the specified template.
	 * 
	 * @param template
	 * @return
	 */
	public Representation create( Template template ) throws IndexOutOfBoundsException
	{
		// fin a proper factory for the template type
		Class templateType = template.getClass();
		for ( InstantiationDef definition : m_definitions )
		{
			if ( definition.m_type.equals( templateType ) )
			{
				// create the representation
				Representation representation = definition.m_factory.instantiate( template );
				return representation;
			}
		}
		
		throw new IndexOutOfBoundsException( "Factory not registered for the specified template type" );
	}
}
