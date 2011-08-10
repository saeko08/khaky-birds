package com.hypefoundry.engine.test.util;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

public class BitwiseOperationTest extends AndroidTestCase 
{
	class WorldStatusChange
	{
		// List< Integer > 	m_indices;				// 8 + 8 + n*8
		int					m_newState;				// 4 b
													// -------
													// 20 + n*8 ==> 5b
		short				m_indices;				// 5b
		
		WorldStatusChange( int newState )
		{
			m_newState = 0;
			m_indices = 0;
		}
		
		WorldStatusChange add( int idx )
		{
			m_indices = (short) (m_indices | ( 1 << idx ));
			return this;
		}
		
		void execute( int[] objects )
		{
			short val = 1;
			for ( int i = 0; i < 15; ++i )
			{
				boolean isSet = ( m_indices & val ) == val;
				if ( isSet )
				{
					objects[ i ] = m_newState;
				}
				val = (short) (val << 1);
			}
		}
	}
	
	public void testAnd()
	{
		final int ALIVE = 1;
		final int KILLED = 0;
		
		// on the server, there are 16 live agents
		int[] objects = new int[16];
		for( int i = 0; i < objects.length; ++i )
		{
			objects[i] = ALIVE;
		}
		
		// at my local computer, I've just thrown a granade that killed two guys
		WorldStatusChange msg = new WorldStatusChange( KILLED ).add( 0 ).add( 5 );
		
		// I'm sending the message to the server, and the server executes it,
		// updating the agent's state
		msg.execute( objects );
		
		// killed in action
		assertEquals( 0, objects[0] );
		assertEquals( 0, objects[5] );	
	}
}
