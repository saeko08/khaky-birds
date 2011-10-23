/**
 * 
 */
package com.hypefoundry.engine.hud;


import java.util.List;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.widgets.*;


/**
 * A hud helper class that is capable of storing many hud elements.
 * 
 * @author Paksas
 *
 */
public class HudComposite extends HudWidget 
{
	// ------------------------------------------------------------------------
	// Element factories
	// ------------------------------------------------------------------------
	static interface ElementFactory
	{
		HudWidget create();
	}
	
	static class ElementFactoryData
	{
		Class< ? extends HudWidget >		m_type;
		ElementFactory						m_factory;
		
		ElementFactoryData( Class< ? extends HudWidget > type, ElementFactory factory )
		{
			m_type = type;
			m_factory = factory;
		}
	}
	
	private static ElementFactoryData[]	m_elementFactories = {
		new ElementFactoryData( FrameWidget.class, new ElementFactory() { @Override public HudWidget create() { return new FrameWidget(); } } ),
		new ElementFactoryData( ButtonWidget.class, new ElementFactory() { @Override public HudWidget create() { return new ButtonWidget(); } } ),
		new ElementFactoryData( ImageWidget.class, new ElementFactory() { @Override public HudWidget create() { return new ImageWidget(); } } ),
		new ElementFactoryData( AnimationWidget.class, new ElementFactory() { @Override public HudWidget create() { return new AnimationWidget(); } } ),
		new ElementFactoryData( CounterWidget.class, new ElementFactory() { @Override public HudWidget create() { return new CounterWidget(); } } ),
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
	private HudWidget[]			m_elements = null;
	
	/**
	 * Adds a new HUD element. 
	 * 
	 * @param element
	 */
	void addElement( HudWidget element )
	{
		if ( element == null )
		{
			return;
		}
		
		// append the new element to the array of elements
		HudWidget[] newArray = null;
		if ( m_elements == null )
		{
			newArray = new HudWidget[1];
			newArray[0] = element;
		}
		else
		{
			newArray = new HudWidget[ m_elements.length + 1 ];
			for ( int i = 0; i < m_elements.length; ++i )
			{
				newArray[i] = m_elements[i];
			}
			newArray[m_elements.length] = element;
		}
	}
	
	@Override
	void initialize( HudWidget parentWidget, HudLayout layout )
	{
		super.initialize( parentWidget, layout );
		
		for ( int i = 0; i < m_elements.length; ++i )
		{
			if ( m_elements[i] != null )
			{
				m_elements[i].initialize( this, layout );
			}
		}
	}

	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		// resize the elements array
		int childrenCount = loader.getChildrenCount( "Widget" );
		m_elements = new HudWidget[childrenCount];
		
		// load all the children
		int childIdx = 0;
		for ( DataLoader child = loader.getChild( "Widget" ); child != null; child = child.getSibling() )
		{
			// determine the child type and create a widget of this type
			String type = child.getStringValue( "type" );
			ElementFactory factory = findElementFactory( type );
			if ( factory != null )
			{
				HudWidget element = factory.create();
				element.load( resMgr, child );
				
				m_elements[childIdx] = element;
				++childIdx;
			}
		}
	}
	
	@Override
	void gatherWidgets( List< HudWidget > widgets )
	{
		super.gatherWidgets( widgets );
		
		for ( int i = 0; i < m_elements.length; ++i )
		{
			if ( m_elements[i] != null )
			{
				m_elements[i].gatherWidgets( widgets );
			}
		}
	}
}
