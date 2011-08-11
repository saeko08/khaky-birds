/**
 * 
 */
package com.hypefoundry.engine.world.serialization.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.hypefoundry.engine.world.serialization.WorldFileSaver;

/**
 * An XML world file saver.
 * 
 * @author Paksas
 */
public class XMLWorldFileSaver implements WorldFileSaver 
{

	/**
	 * A factory method that instantiates a new XML nodes hierarchy
	 * into which the world contents will be saved.
	 * 
	 * @param stream
	 * @return
	 */
	public static WorldFileSaver create( OutputStream stream )
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try 
		{
			db = dbf.newDocumentBuilder();
		} 
		catch ( ParserConfigurationException e ) 
		{
			throw new RuntimeException( e );
		}
	
		// parse the data the stream contains
		Document doc = db.newDocument();
		doc.appendChild(arg0)
		
		
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 */
	protected XMLWorldFileSaver()
	{
		
	}
	
	@Override
	public void setStringValue( String id, String value ) 
	{
	}

	@Override
	public void setIntValue( String id, int value ) 
	{
	}

	@Override
	public void setFloatValue( String id, float value ) 
	{
	}

	@Override
	public WorldFileSaver addChild( String id ) 
	{
		return null;
	}

	@Override
	public WorldFileSaver addSibling() 
	{
		return null;
	}

}
