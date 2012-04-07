package com.hypefoundry.engine.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetFileDescriptor;

/**
 * An interface for handling file input output.
 * 
 * @author paksas
 *
 */
public interface FileIO 
{
	/**
	 * Returns an asset file descriptor to the specified asset.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public AssetFileDescriptor getAssetFileDescriptor( String fileName ) throws IOException;
	
	/**
	 * Creates an input stream using which one will be able to read
	 * an asset file shipped along with the application.
	 * 
	 * The fileName should contain a path to an asset relative
	 * to the /assets directory ( which is considered the root 
	 * directory to all the assets )
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public InputStream readAsset( String fileName ) throws IOException;
	
	/**
	 * Creates an input stream using which one will be able to read a file
	 * from an external storage device.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public InputStream readFile( String fileName ) throws IOException;
	
	/**
	 * Creates an output stream using which one will be able to write to a file
	 * on an external storage device.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public OutputStream writeFile( String fileName ) throws IOException;
}
