/**
 * 
 */
package com.hypefoundry.engine.hud;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.hud.widgets.frame.*;
import com.hypefoundry.engine.hud.widgets.button.*;
import com.hypefoundry.engine.hud.widgets.image.*;
import com.hypefoundry.engine.hud.widgets.animation.*;
import com.hypefoundry.engine.hud.widgets.counter.*;
import com.hypefoundry.engine.hud.widgets.checkbox.*;


/**
 * Hud display.
 * 
 * @author Paksas
 *
 */
public class Hud extends Resource
{
	// ------------------------------------------------------------------------
	// Template factories definitions
	// ------------------------------------------------------------------------
	private static interface HudTemplateFactory
	{
		HudWidgetVisualTemplate create();
	}
	
	private static class TemplateDefinition
	{
		Class< ? extends HudWidgetVisualTemplate >		m_type;
		HudTemplateFactory								m_factory;
		
		TemplateDefinition( Class< ? extends HudWidgetVisualTemplate > type, HudTemplateFactory factory )
		{
			m_type = type;
			m_factory = factory;
		}
	}
	
	private static TemplateDefinition[]		m_definitions = {
		new TemplateDefinition( DefaultFrameVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new DefaultFrameVisualTemplate(); } } ),
		new TemplateDefinition( CustomFrameVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new CustomFrameVisualTemplate(); } } ),
		new TemplateDefinition( DefaultButtonVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new DefaultButtonVisualTemplate(); } } ),
		new TemplateDefinition( ImageButtonVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new ImageButtonVisualTemplate(); } } ),
		new TemplateDefinition( AnimatedButtonVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new AnimatedButtonVisualTemplate(); } } ),
		new TemplateDefinition( ImageVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new ImageVisualTemplate(); } } ),
		new TemplateDefinition( AnimationVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new AnimationVisualTemplate(); } } ),
		new TemplateDefinition( CounterVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new CounterVisualTemplate(); } } ),
		new TemplateDefinition( CustomCheckboxVisualTemplate.class, new HudTemplateFactory() { @Override public HudWidgetVisualTemplate create() { return new CustomCheckboxVisualTemplate(); } } ),
	};
	
	private static HudTemplateFactory findTemplateFactory( String type )
	{
		for ( int i = 0; i < m_definitions.length; ++i )
		{
			if ( m_definitions[i].m_type.getSimpleName().equals( type ) )
			{
				return m_definitions[i].m_factory;
			}
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Templates to user-defined classes associations
	// ------------------------------------------------------------------------

	private static class TemplateAssociation
	{
		String						m_className;
		HudWidgetVisualTemplate		m_template;
		
		TemplateAssociation( String className, HudWidgetVisualTemplate template )
		{
			m_className = className;
			m_template = template;
		}
	}
	
	private List< TemplateAssociation >		m_templates = new ArrayList< TemplateAssociation >();
	
	/**
	 * Creates a visual representation of a widget.
	 * 
	 * @param renderer
	 * @param widget
	 * @return
	 */
	HudWidgetVisual createVisual( HudRenderer renderer, HudWidget widget )
	{		
		int count = m_templates.size();
		for ( int i = 0; i < count; ++i )
		{
			TemplateAssociation association = m_templates.get(i);
			
			if ( association.m_className.equals( widget.m_visualName ) )
			{
				return association.m_template.instantiate( renderer, widget );
			}
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Resource implementation
	// ------------------------------------------------------------------------
	@Override
	public void load() 
	{
		m_templates.clear();
		
		InputStream stream = null;
		try 
		{
			stream = m_game.getFileIO().readAsset( m_assetPath );
		} 
		catch ( IOException e ) 
		{
			throw new RuntimeException( e );
		}
		
		// parse the animation data
		DataLoader hudNode = XMLDataLoader.parse( stream, "HUD" );
		if ( hudNode != null )
		{
			for ( DataLoader child = hudNode.getChild( "Template" ); child != null; child = child.getSibling() )
			{
				String type = child.getStringValue( "type" );
				HudTemplateFactory templateFactory = findTemplateFactory( type );
				
				if ( templateFactory != null )
				{
					HudWidgetVisualTemplate template = templateFactory.create();
					template.load( m_resMgr, child );
					
					String className = child.getStringValue( "class" );
					m_templates.add( new TemplateAssociation( className, template ) );
					
				}
			}

		}
	}

	@Override
	public void release() 
	{
	}
}
