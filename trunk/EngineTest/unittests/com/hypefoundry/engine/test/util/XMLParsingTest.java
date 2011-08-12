package com.hypefoundry.engine.test.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.test.AndroidTestCase;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLParsingTest extends AndroidTestCase 
{
	public void testSimpleXML()
	{
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?> <testTag>dupa</testTag>";
		InputStream xmlStream = null;
		try 
		{
			xmlStream = new ByteArrayInputStream( xml.getBytes("UTF-8") );
		} 
		catch (UnsupportedEncodingException e1) 
		{
			assertTrue( false );
		}

		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try 
		{
			db = dbf.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e) 
		{
			assertTrue( false );
		}
	
		Document doc = null;
		try 
		{
			doc = db.parse( xmlStream );
		} 
		catch (SAXException e) 
		{
			assertTrue( false );
		} 
		catch (IOException e) 
		{
			assertTrue( false );
		}
		
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName( "testTag" );
		assertEquals( 1, nList.getLength() );
		assertEquals( Node.ELEMENT_NODE, nList.item(0).getNodeType() );
		
		Element eElement = (Element)nList.item(0);
		assertNotNull( eElement );
		
		NodeList textFNList = eElement.getChildNodes();
		assertEquals( "dupa", ((Node)textFNList.item(0)).getNodeValue().trim() );
	}
}
