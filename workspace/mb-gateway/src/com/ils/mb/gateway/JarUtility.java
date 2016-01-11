/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.mb.gateway;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * A class with some utility methods used by the Master Builder. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class JarUtility {
	private final String TAG = "JarUtility";
	private final LoggerEx log;
	private final GatewayContext context;

	public JarUtility(GatewayContext ctx) {
		this.context = ctx;
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}
	
	/**
	 * Search the modules that are installed into Ignition, looking
	 * for containing a file with the indicated name.
	 * @param marker name of file that denotes the correct module
	 * @return a path to the module.
	 */
	public Path internalModuleContaining(String marker) {
		Path result = null;
		Path jarDirPath = Paths.get(context.getUserlibDir().getAbsolutePath(),"modules");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(jarDirPath, "*.{jar,modl}")) {
			for (Path entry: stream) {
				log.infof("%s.internalModuleContaining: Inspecting %s ..",TAG,entry.toString());
				if(jarAtPathContains(entry,marker)) {
					result=entry;
					break;
				}
			}
		}
		catch (DirectoryIteratorException die) {
			log.infof("%s.internalModuleContaining: Error iterating %s (%s)",TAG,jarDirPath.toString(),die.getCause().getLocalizedMessage());
		}
		catch (IOException ioe) {
			log.infof("%s.internalModuleContaining: IO error iterating %s (%s)",TAG,jarDirPath.toString(),ioe.getLocalizedMessage());
		}
		return result;
	}
	
	/**
	 * Inspect the subject jar file for the presence of a named file.
	 * @param jarPath PATH to the jar file
	 * @param name of the marker file
	 * @return true if the marker file exiss in the jar
	 */
	public boolean jarAtPathContains(Path jarPath,String marker) {
		boolean answer = false;
		try {
			JarFile jar = new JarFile(jarPath.toFile());
			if( jar.getJarEntry(marker)!=null ) answer = true;
			jar.close();
		}
		catch(IOException ioe) {
			log.infof("%s.jarAtPathContains: IO error converting %s to jar (%s)",TAG,jarPath.toString(),ioe.getLocalizedMessage());
		}
		return answer;
	}
	
	/**
	 * Iterate over the files in a jar and copy into a directory.
	 * Make any intermediate directories required.
	 */
	public void unJarToDirectory(Path jarPath,Path destPath) {
		try {
			JarFile jar = new JarFile(jarPath.toFile());
			Enumeration<JarEntry> entryWalker = jar.entries();
			String destination = destPath.toString();
			while (entryWalker.hasMoreElements()) {
				JarEntry entry = entryWalker.nextElement();
				Path toWrite = Paths.get(destination,entry.getName());
				if (entry.isDirectory()) {
					toWrite.toFile().mkdirs();
					continue;
				}
				InputStream in = new BufferedInputStream(jar.getInputStream(entry));
				OutputStream out = new BufferedOutputStream(new FileOutputStream(toWrite.toFile()));
				byte[] buffer = new byte[2048];
				for (;;) {
					int nBytes = in.read(buffer);
					if (nBytes <= 0) {
						break;
					}
					out.write(buffer, 0, nBytes);
				}
				out.flush();
				out.close();
				in.close();
			}
			jar.close();
		}
		catch(IOException ioe) {
			log.infof("%s.unJarToDirectory: IO error converting %s from jar (%s)",TAG,jarPath.toString(),ioe.getLocalizedMessage());
		}
	}
}
