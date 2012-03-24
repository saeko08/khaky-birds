/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.checkbox;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.CaptionedHudWidget;
import com.hypefoundry.engine.hud.ClickableHudWidget;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class CheckboxWidget extends HudWidget  implements ClickableHudWidget, CaptionedHudWidget
{
	public enum State
	{
		Checked( 0, "Checked" ),
		Unchecked( 1, "Unchecked" );
		
		public int		m_value;
		public String 	m_xmlTag;
		
		State( int value, String xmlTag )
		{
			m_value = value;
			m_xmlTag = xmlTag;
		}
	}
	
	public String				m_caption				= "";
	public State				m_state					= State.Unchecked;
	

	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		m_caption		= loader.getStringValue( "caption" );
	}

	@Override
	public void onButtonPressed( int id ) 
	{
		m_layout.onButtonPressed( this );
	}

	@Override
	public String getCaption() 
	{
		return m_caption;
	}

	/**
	 * Toggles between the checked and unchecked state.
	 */
	public void toggleState() 
	{
		if ( m_state == State.Checked )
		{
			m_state = State.Unchecked;
		}
		else
		{
			m_state = State.Checked;
		}	
	}

	/**
	 * Checks if the checkbox is checked.
	 * 
	 * @return
	 */
	public boolean isChecked() 
	{
		return m_state == State.Checked;
	}

	/**
	 * Changes the state of the checkbox.
	 * 
	 * @param check
	 */
	public void setChecked( boolean check ) 
	{
		m_state = check ? State.Checked : State.Unchecked;	
	}
}
