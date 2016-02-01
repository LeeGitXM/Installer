/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.mb.gateway;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/**
 * Create an Ignition .modl from the contents of a specified directory.
 * The files must reside on the same system as the Ignition Gateway.
 * 
 */
public class IgnitionModule implements Runnable {
	private final static String TAG = "IgnitionModule";
	private final static int BUFFER_SIZE = 1024;
	private final LoggerEx log;
	private final GatewayRequestHandler requestHandler;
	private final String indir;
	private final String outpath;

	public IgnitionModule(GatewayRequestHandler rh,String in,String out ) {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.requestHandler = rh;
		this.indir = in.replace("\\", "/");
		this.outpath = out;
	}

	public void run()  {
		String status = null;
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		JarOutputStream target = null;
		try {
			target = new JarOutputStream(new FileOutputStream(outpath), manifest);
			// We want the relative path, so record the original length 
			addJarEntry(new File(indir), target,indir.length()+1 );
		}
		catch( FileNotFoundException fnfe) {
			status = String.format("%s.run: Failed to find input directory (%s)",TAG,fnfe.getLocalizedMessage());
			log.warn(status);
		}
		catch( IOException ioe) {
			status = String.format("%s.run: Failed to reading input (%s)",TAG,ioe.getLocalizedMessage());
			log.warn(status);
		}
		finally {
			if( target!=null ) {
				try {
					target.close();
				}
				catch(IOException ignore) {}
			}
		}
		
		if( status==null ) requestHandler.pushStatus(String.format("Copied artifacts to %s",outpath),true);
		else requestHandler.pushStatus(status,false);
	}

	/**
	 * Recursively add files to the jar.
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	private void addJarEntry(File source, JarOutputStream target,int len) throws IOException {
		BufferedInputStream in = null;
		try {
			String subpath = source.getPath().replace("\\", "/");
			if( len>=subpath.length() ) subpath = "";
			else subpath = subpath.substring(len);
			
			if (source.isDirectory()) {
				if (!subpath.isEmpty()) {
					if (!subpath.endsWith("/"))
						subpath += "/";
					JarEntry entry = new JarEntry(subpath);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile: source.listFiles()) {
					addJarEntry(nestedFile, target,len);
				}
				return;
			}

			JarEntry entry = new JarEntry(subpath);
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[BUFFER_SIZE];
			while (true)   {
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		}
		finally  {
			if (in != null)
				in.close();
		}
	}
}
