/**
 * 
 */
package com.hypefoundry.engine.world.serialization.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hypefoundry.engine.util.serialization.xml.WorldFileSaver;
;

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
	 * @return
	 */
	public static WorldFileSaver create()
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
		Element root = doc.createElement( "World" );
		doc.appendChild( root );
		
		return new XMLWorldFileSaver( doc, root );
	}
	
	// ------------------------------------------------------------------------
	
	private Document		m_document = null;
	private Element			m_element = null;
	
	
	/**
	 * Root element constructor.
	 * 
	 * @param document
	 * @param root			root element
	 */
	protected XMLWorldFileSaver( Document document, Element root)
	{
		m_document = document;
		m_element = root;
	}
	
	@Override
	public void flush( OutputStream stream )
	{
		if ( m_document != null )
		{
			m_document.normalize();
			
			Transformer transformer = null;
			try 
			{
				transformer = TransformerFactory.newInstance().newTransformer();
			} 
			catch( TransformerConfigurationException e ) 
			{
				throw new RuntimeException( e );
			} 
			catch( TransformerFactoryConfigurationError e ) 
			{
				throw new RuntimeException( e );
			}
			transformer.setOutputProperty( OutputKeys.INDENT, "yes" );

			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult( stream );
			DOMSource source = new DOMSource( m_document );
			try 
			{
				transformer.transform( source, result );
			} 
			catch( TransformerException e ) 
			{
				throw new RuntimeException( e );
			}
		}
	}
	
	@Override
	public void setStringValue( String id, String value ) 
	{
		m_element.setAttribute( id, value );
	}

	@Override
	public void setIntValue( String id, int value ) 
	{
		try
		{
			m_element.setAttribute( id, Integer.toString( value ) );
		}
		catch( Exception ex )
		{
		}	
	}

	@Override
	public void setFloatValue( String id, float value ) 
	{
		try
		{
			m_element.setAttribute( id, Float.toString( value ) );
		}
		catch( Exception ex )
		{
		}	
	}

	@Override
	public WorldFileSaver addChild( String id ) 
	{
		Element child = m_document.createElement( id );
		m_element.appendChild( child );
		return new XMLWorldFileSaver( m_document, child );
	}

}
