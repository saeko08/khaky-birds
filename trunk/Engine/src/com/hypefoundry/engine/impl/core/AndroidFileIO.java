package com.hypefoundry.engine.impl.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

import com.hypefoundry.engine.core.FileIO;

/**
 * An implementation of the FileIO interface using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidFileIO implements FileIO 
{
	private AssetManager		m_assets;
	private String				m_externalStoragePath;
	
	public AndroidFileIO( AssetManager assets )
	{
		m_assets = assets;
		m_externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	@Override
	public InputStream readAsset( String fileName ) throws IOException 
	{
		return m_assets.open( fileName );
	}

	@Override
	public InputStream readFile( String fileName ) throws IOException 
	{
		return new FileInputStream( m_externalStoragePath + fileName );
	}

	@Override
	public OutputStream writeFile( String fileName ) throws IOException 
	{
		File fullPath = new File( m_externalStoragePath + fileName );
		File dirPath = fullPath.getParentFile();
		dirPath.mkdirs();
		return new FileOutputStream( fullPath );
	}
}
