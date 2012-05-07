/**
 * 
 */
package com.hypefoundry.kabloons.entities.tutorial;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.tutorial.StartTutorial.State;

/**
 * @author Paksas
 *
 */
public class StartTutorialVisual extends EntityVisual 
{
	private TextureRegion[]		m_stateImages = new TextureRegion[State.values().length];
	private TextureRegion		m_fingerImage;
	private BoundingBox			m_fingerBounds = new BoundingBox();
	private StartTutorial		m_tutorial;
	
	private Spline[]			m_fingerPaths = new Spline[State.values().length];
	private float				m_fingerTimeline = 0.0f;
	private final float 		m_fingerSpeed = 1.0f;
	
	private Vector3				m_fingerPos = new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public StartTutorialVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_tutorial = (StartTutorial)entity;
		for ( int i = 0; i < m_tutorial.m_stateImagePaths.length; ++i )
		{
			if ( m_tutorial.m_stateImagePaths[i] != null && m_tutorial.m_stateImagePaths[i].length() > 0 )
			{
				m_stateImages[i] = resMgr.getResource( TextureRegion.class, m_tutorial.m_stateImagePaths[i] );
			}
		}
		
		m_fingerImage = resMgr.getResource( TextureRegion.class, m_tutorial.m_fingerImage );
		m_fingerBounds = new BoundingBox( -1.38f, -0.885f, 0.39f, 0.885f );
		// define finger paths for each gesture
		{
			// place a left fan
			m_fingerPaths[0] = new Spline();
			m_fingerPaths[0].addPoint( new Vector3( 1.5f, 0.0f, 0.0f ) );
			m_fingerPaths[0].addPoint( new Vector3( -0.5f, 0.0f, 0.0f ) );
		}
		
		{
			// remove the fan
			m_fingerPaths[1] = new Spline();
			m_fingerPaths[1].addPoint( new Vector3( -0.5f, -0.5f, 0.0f ) );
			m_fingerPaths[1].addPoint( new Vector3( 0.5f, 0.5f, 0.0f ) );
			m_fingerPaths[1].addPoint( new Vector3( -0.5f, -0.5f, 0.0f ) );
		}
		
		{
			// place a right fan
			m_fingerPaths[2] = new Spline();
			m_fingerPaths[2].addPoint( new Vector3( 0.0f, 0.0f, 0.0f ) );
			m_fingerPaths[2].addPoint( new Vector3( 2.0f, 0.0f, 0.0f ) );
		}
		
		{
			// release the boy
			m_fingerPaths[3] = new Spline();
			m_fingerPaths[3].addPoint( new Vector3( 0.0f, -1.0f, 0.0f ) );
			m_fingerPaths[3].addPoint( new Vector3( 0.0f, 1.0f, 0.0f ) );
		}

	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{		
		int stateIdx = m_tutorial.m_state.ordinal();
		TextureRegion currRegion = m_stateImages[ stateIdx ];
		if ( currRegion == null )
		{
			return;
		}
		
		Vector3 pos = m_tutorial.m_stateImagePos[stateIdx];
		BoundingBox bs = m_tutorial.m_stateImageBounds[stateIdx];			
		batcher.drawSprite( pos, bs, currRegion );
		
		// animate the gesture
		float pathDist = 0.0f;
		{
			m_fingerTimeline += deltaTime;
			if ( m_fingerTimeline > m_fingerSpeed )
			{
				m_fingerTimeline -= (int)( m_fingerTimeline / m_fingerSpeed ) * m_fingerSpeed;
			}
			
			float timeSlider = m_fingerTimeline / m_fingerSpeed;
			pathDist = m_fingerPaths[stateIdx].length() * timeSlider;
		}
		m_fingerPaths[stateIdx].getPosition( pathDist, m_fingerPos );
		m_fingerPos.add( m_tutorial.m_gesturePos );
		batcher.drawSprite( m_fingerPos, m_fingerBounds, m_fingerImage );
	}

}
