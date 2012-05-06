/**
 * 
 */
package com.hypefoundry.engine.movies;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class MovieEntityVisual extends EntityVisual 
{
	private MovieEntity			m_movie;
	private AnimationPlayer		m_player = new AnimationPlayer();
	
	private float				m_lifeTimer;
	private MovieEndListener	m_listener;
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 * @param listener
	 */
	public MovieEntityVisual( ResourceManager resMgr, Entity entity, MovieEndListener listener ) 
	{
		super(entity);
		
		m_listener = listener;
		m_movie = (MovieEntity)entity;
		
		Animation animation = resMgr.getResource( Animation.class, m_movie.m_path );
		int animIdx = m_player.addAnimation( animation );
		m_player.select( animIdx );
		
		m_lifeTimer = animation.getDuration();
	}


	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();

		TextureRegion region = m_player.getTextureRegion( deltaTime );
		batcher.drawSprite( pos, bs, region );
		
		// decrease the life timer
		m_lifeTimer -= deltaTime;
		if ( m_lifeTimer <= 0.0f )
		{
			// the animation has ended, we no longer need to display it
			m_listener.onMovieEnded();
		}
	}
}
