/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.ai.gateway.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.CharBuffer;
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
 * A class with some utility methods used by the Application Installer. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class JarUtility {
	private final String CLSS = "JarUtility";
	private final LoggerEx log;
	private final GatewayContext context;

	public JarUtility(GatewayContext ctx) {
		this.context = ctx;
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}
	
	/**
	 * Search the modules that are installed into Ignition, looking
	 * for one containing a file with the indicated name.
	 * @param marker name of file that denotes the correct module
	 * @return a path to the module.
	 */
	public Path internalModuleContaining(String marker) {
		Path result = null;
		Path jarDirPath = Paths.get(context.getUserlibDir().getAbsolutePath(),"modules");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(jarDirPath, "*.{jar,modl}")) {
			for (Path entry: stream) {
				log.infof("%s.internalModuleContaining: Inspecting %s ..",CLSS,entry.toString());
				if(jarAtPathContains(entry,marker)) {
					result=entry;
					break;
				}
			}
		}
		catch (DirectoryIteratorException die) {
			log.warnf("%s.internalModuleContaining: Error iterating %s (%s)",CLSS,jarDirPath.toString(),die.getCause().getLocalizedMessage());
		}
		catch (IOException ioe) {
			log.warnf("%s.internalModuleContaining: IO error iterating %s (%s)",CLSS,jarDirPath.toString(),ioe.getLocalizedMessage());
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
			log.infof("%s.jarAtPathContains: IO error converting %s to jar (%s)",CLSS,jarPath.toString(),ioe.getLocalizedMessage());
		}
		return answer;
	}
	/**
	 * @param entryName is a path relative to the jar file
	 * @return the contents of the file within the jar as a String
	 */
	public String readFileFromJar(String entryName,Path jarPath)  {
		String result = "";
		JarFile jar = null;
		try {
			
			jar = new JarFile(jarPath.toFile());

			JarEntry entry = jar.getJarEntry(entryName);
			if( entry!=null ) {
				InputStream is = jar.getInputStream(entry);
				result =  stringFromInputStream(is);
				is.close();
			}
			else {
				log.warnf("%s.readFileFromJar: %s not found in %s",CLSS,entryName,jarPath.toFile().getAbsolutePath());
			}
		}
		catch(IOException ioe) {
			log.warnf("%s.readFileFromJar: IO error reading %s (%s)",CLSS,entryName,ioe.getLocalizedMessage());
		}
		finally {
			if(jar!=null) {
				try {
					jar.close();
				}
				catch(IOException ignore) {}
			}
		}
		return result;
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
			log.infof("%s.unJarToDirectory: IO error converting %s from jar (%s)",CLSS,jarPath.toString(),ioe.getLocalizedMessage());
		}
	}

	// convert InputStream to String. Input has line terminators
	private String stringFromInputStream(InputStream in) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder out = new StringBuilder();
	    String newLine = System.getProperty("line.separator");
	    String line;
	    try {
	    	while ((line = reader.readLine()) != null) {
	    		out.append(line);
	    		out.append(newLine);
	    	}
	    } 
	    catch(IOException ioe) {
	    	log.infof("%s.stringFromInputStream: IO error readinf from jar (%s)",CLSS,ioe.getLocalizedMessage());
	    }
	    finally {
	    	try {
	    		reader.close();
	    	}
	    	catch(IOException ignore) {}
	    }
	    return out.toString();
		
	}
}
