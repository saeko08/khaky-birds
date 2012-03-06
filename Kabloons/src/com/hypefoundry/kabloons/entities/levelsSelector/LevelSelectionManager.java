/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import com.hypefoundry.engine.core.FileIO;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.utils.UnlockedLevelsStorage;


/**
 * @author Paksas
 *
 */
public class LevelSelectionManager extends Entity
{
	int[]					m_availableLevels;

	
	/**
	 * Constructor.
	 * @param fileIO
	 */
	public LevelSelectionManager( FileIO fileIO )
	{
		UnlockedLevelsStorage storage = new UnlockedLevelsStorage( fileIO );
		m_availableLevels = storage.getAvailableLevels();
	}

	/**
	 * Checks if the specified level can be selected at all.
	 * 
	 * @param level
	 * @return
	 */
	public boolean canBeSelected( LevelItem level ) 
	{
		if ( level == null )
		{
			return  false;
		}
		
		for ( int i = 0; i < m_availableLevels.length; ++i )
		{
			if ( level.m_levelIdx == m_availableLevels[i] )
			{
				return true;
			}
		}
		
		return false;
	}
}
