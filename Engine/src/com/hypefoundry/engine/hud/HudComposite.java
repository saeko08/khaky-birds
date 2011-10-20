/**
 * 
 */
package com.hypefoundry.engine.hud;

import com.hypefoundry.engine.hud.Frame;
import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.hud.HudElement;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * A hud helper class that is capable of storing many hud elements.
 * 
 * @author Paksas
 *
 */
class HudComposite extends HudElement 
{
	// ------------------------------------------------------------------------
	// Element factories
	// ------------------------------------------------------------------------
	static interface ElementFactory
	{
		HudElement create();
	}
	
	static class ElementFactoryData
	{
		Class< ? extends HudElement >		m_type;
		ElementFactory						m_factory;
		
		ElementFactoryData( Class< ? extends HudElement > type, ElementFactory factory )
		{
			m_type = type;
			m_factory = factory;
		}
	}
	
	private static ElementFactoryData[]	m_elementFactories = {
		new ElementFactoryData( Frame.class, new ElementFactory() { @Override public HudElement create() { return new Frame(); } } ),
	};
	
	/**
	 * Finds a factory capable of instantiating an element of the specified type.
	 * 
	 * @param type
	 * @return
	 */
	private static ElementFactory findElementFactory( String type )
	{
		for ( int i = 0; i < m_elementFactories.length; ++i )
		{
			if ( m_elementFactories[i].m_type.getSimpleName().equals( type ) ) 
			{
				return m_elementFactories[i].m_factory;
			}
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Composite definition
	// ------------------------------------------------------------------------
	private HudElement[]			m_elements = null;
	
	/**
	 * Adds a new HUD element. 
	 * 
	 * @param element
	 */
	void addElement( HudElement element )
	{
		if ( element == null )
		{
			return;
		}
		
		// append the new element to the array of elements
		HudElement[] newArray = null;
		if ( m_elements == null )
		{
			newArray = new HudElement[1];
			newArray[0] = element;
		}
		else
		{
			newArray = new HudElement[ m_elements.length + 1 ];
			for ( int i = 0; i < m_elements.length; ++i )
			{
				newArray[i] = m_elements[i];
			}
			newArray[m_elements.length] = element;
		}
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime )
	{
		for ( int i = 0; i < m_elements.length; ++i )
		{
			if ( m_elements[i] != null )
			{
				m_elements[i].draw( batcher, deltaTime );
			}
		}
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		// resize the elements array
		int childrenCount = loader.getChildrenCount( "Widget" );
		m_elements = new HudElement[childrenCount];
		
		// load all the children
		int childIdx = 0;
		for ( DataLoader child = loader.getChild( "Widget" ); child != null; child = child.getSibling() )
		{
			// determine the child type and create a widget of this type
			String type = child.getStringValue( "type" );
			ElementFactory factory = findElementFactory( type );
			if ( factory != null )
			{
				HudElement element = factory.create();
				element.load( child );
				
				m_elements[childIdx] = element;
				++childIdx;
			}
		}
	}
	
	@Override
	void setParentHud( Hud hud )
	{
		super.setParentHud( hud );
		
		// reparent all the children
		for ( int i = 0; i < m_elements.length; ++i )
		{
			if ( m_elements[i] != null )
			{
				m_elements[i].setParentHud( hud );
			}
		}
	}
}
