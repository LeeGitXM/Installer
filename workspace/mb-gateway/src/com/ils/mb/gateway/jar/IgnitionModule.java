/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.mb.gateway.jar;

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
 */
public class IgnitionModule implements Runnable {
	private final static String TAG = "IgnitionModule";
	private final LoggerEx log;
	private final String indir;
	private final String outpath;

	public IgnitionModule(String in,String out ) {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.indir = in;
		this.outpath = out;
	}

	public void run()  {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		JarOutputStream target = null;
		try {
			target = new JarOutputStream(new FileOutputStream(outpath), manifest);
			add(new File(indir), target);
		}
		catch( FileNotFoundException fnfe) {
			log.warnf("%s.run: Failed to find input directory (%s)",TAG,fnfe.getLocalizedMessage());
		}
		catch( IOException ioe) {
			log.warnf("%s.run: Failed to reading input (%s)",TAG,ioe.getLocalizedMessage());
		}
		finally {
			if( target!=null ) {
				try {
					target.close();
				}
				catch(IOException ignore) {}
			}
		}
	}

	/**
	 * Recursively add files to the jar.
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	private void add(File source, JarOutputStream target) throws IOException {
		BufferedInputStream in = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/");
				if (!name.isEmpty()) {
					if (!name.endsWith("/"))
						name += "/";
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile: source.listFiles()) {
					add(nestedFile, target);
				}

				return;
			}

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
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
