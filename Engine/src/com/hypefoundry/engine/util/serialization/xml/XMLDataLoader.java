/**
 * 
 */
package com.hypefoundry.engine.util.serialization.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;
;

/**
 * An XML world file parser.
 *  
 * @author Paksas
 *
 */
public class XMLDataLoader implements DataLoader 
{

	/**
	 * A factory method that instantiates the node hierarchy
	 * based on the specified input stream that contains an XML file data.
	 * 
	 * @param stream
	 * @param rootNodeTag		tag of the root node
	 * @return
	 */
	public static DataLoader parse( InputStream stream, String rootNodeTag )
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
		Document doc = null;
		try 
		{
			doc = db.parse( stream );
		} 
		catch ( SAXException e ) 
		{
			throw new RuntimeException( e );
		} 
		catch ( IOException e ) 
		{
			throw new RuntimeException( e );
		}
		
		// normalize the document
		doc.getDocumentElement().normalize();
		NodeList worldNodesList = doc.getElementsByTagName( rootNodeTag );
		if ( worldNodesList.getLength() == 0 )
		{
			// there are no world definitions in the file
			return null;
		}
		else
		{
			// find an index of the first element
			int count = worldNodesList.getLength();
			for ( int i = 0; i < count; ++i )
			{
				if ( worldNodesList.item(i).getNodeType() == Node.ELEMENT_NODE )
				{
					return new XMLDataLoader( worldNodesList, i );
				}
			}
			
			// no element was found
			return null;
		}
	}
	
	// --------------------------------------------------------------------
	
	private NodeList			m_siblings;
	private int 				m_elemIdx;
	private Element 			m_xmlElement;

	/**
	 * Constructor.
	 * 
	 * @param xmlElement
	 */
	protected XMLDataLoader( NodeList siblings, int elemIdx )
	{
		m_siblings = siblings;
		m_elemIdx = elemIdx;
		
		m_xmlElement = (Element)m_siblings.item( m_elemIdx );
	}
	
	@Override
	public String getStringValue( String id ) 
	{
		return m_xmlElement.getAttribute( id );
	}
	
	@Override
	public boolean getBoolValue( String id )
	{
		String val = m_xmlElement.getAttribute( id );
		return val.equalsIgnoreCase( "true" );
	}
	
	@Override
	public boolean getBoolValue( String id, boolean defaultValue )
	{
		if ( m_xmlElement.hasAttribute( id ) )
		{
			String val = m_xmlElement.getAttribute( id );
			return val.equalsIgnoreCase( "true" );
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public int getIntValue( String id ) 
	{
		int val = 0;
		try
		{
			val =Integer.parseInt( m_xmlElement.getAttribute( id ) );
		}
		catch( Exception ex )
		{
		}
		return val;
	}

	@Override
	public float getFloatValue( String id )
	{
		float val = 0;
		try
		{
			val = Float.parseFloat( m_xmlElement.getAttribute( id ) );
		}
		catch( Exception ex )
		{
		}
		return val;
	}

	@Override
	public int getChildrenCount( String id ) 
	{
		NodeList children = m_xmlElement.getElementsByTagName( id );
		int childrenCount = children.getLength();
		int count = children.getLength();
		for ( int i = 0; i < count; ++i )
		{
			Node childNode = children.item(i); 
			if ( childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getParentNode() != m_xmlElement )
			{
				--childrenCount;
			}
		}
		
		return childrenCount < 0 ? 0 : childrenCount;
	}

	@Override
	public DataLoader getChild( String id ) 
	{			
		NodeList children = m_xmlElement.getElementsByTagName( id );
		
		// find an index of the first element
		int count = children.getLength();
		for ( int i = 0; i < count; ++i )
		{
			Node childNode = children.item(i); 
			if ( childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getParentNode() == m_xmlElement )
			{
				return new XMLDataLoader( children, i );
			}
		}
		
		// no child element with the specified tag was found
		return null;
	}

	@Override
	public DataLoader getSibling() 
	{
		// find an index of the first element
		int count = m_siblings.getLength();
		for ( int i = m_elemIdx + 1; i < count; ++i )
		{
			Node childNode = m_siblings.item(i); 
			if ( childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getParentNode() == m_xmlElement.getParentNode() )
			{
				return new XMLDataLoader( m_siblings, i );
			}
		}
					
		// no sibling element with this tag was found
		return null;
	}
	
}
