/**
 * 
 */
package com.hypefoundry.kabloons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hypefoundry.engine.core.FileIO;
import com.hypefoundry.engine.util.Arrays;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataSaver;

/**
 * Manages and persists information about the unlocked levels.
 *  
 * @author Paksas
 */
public class UnlockedLevelsStorage 
{
	private FileIO			m_fileIO;
	private int[]			m_availableLevels = new int[0];
	
	/**
	 * Constructor.
	 * 
	 * @param fileIO
	 */
	public UnlockedLevelsStorage( FileIO fileIO )
	{
		m_fileIO = fileIO;
		
		// load information about the unlocked levels
		try 
		{
			InputStream levelsInfoFile = m_fileIO.readFile( "levelsUnlocked.dat" );
			DataLoader levelsInfoLoader = XMLDataLoader.parse( levelsInfoFile, "LevelsUnlocked" );
			
			int count = levelsInfoLoader.getChildrenCount( "Level" );
			m_availableLevels = new int[ count ];
			int i = 0;
			for ( DataLoader levelNode = levelsInfoLoader.getChild( "Level" ); levelNode != null; levelNode = levelNode.getSibling(), ++i )
			{
				m_availableLevels[i] = levelNode.getIntValue( "idx" );
			}
		} 
		catch ( IOException e ) 
		{
			// the file doesn't exist, so this is the first time the user
			// started the game - thus he can only access level 1		
			unlockLevel( 1 );
		}
	}
	
	/**
	 * Unlocks the specified level.
	 * 
	 * @param levelIdx
	 */
	public void unlockLevel( int levelIdx )
	{
		// first add the level index - if it's already there, then exit
		for ( int i = 0; i < m_availableLevels.length; ++i )
		{
			if ( m_availableLevels[i] == levelIdx )
			{
				// yup - the level's already unlocked
				return;
			}
		}
		
		// that's a freshly unlocked level - add it to the array...
		m_availableLevels = Arrays.append( m_availableLevels, levelIdx );
		
		// ...and persist it
		{
			DataSaver saver = XMLDataSaver.create( "LevelsUnlocked" );
			for ( int i = 0; i < m_availableLevels.length; ++i )
			{
				saver.addChild( "Level" ).setIntValue( "idx", m_availableLevels[i] );
			}
			
			try 
			{
				OutputStream levelsInfoFile = m_fileIO.writeFile( "levelsUnlocked.dat" );
				saver.flush( levelsInfoFile );
			} 
			catch ( IOException e ) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns an array of unlocked level indices.
	 * 
	 * @return
	 */
	public int[] getAvailableLevels() 
	{
		return m_availableLevels;
	}
}
