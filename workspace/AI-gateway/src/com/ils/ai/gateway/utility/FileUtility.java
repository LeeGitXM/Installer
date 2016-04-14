/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.ai.gateway.utility;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/**
 * A class with some utility methods used by the Application Installer. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class FileUtility {
	private final String TAG = "FileUtility";
	private final boolean DEBUG = false;
	private final LoggerEx log;

	public FileUtility() {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}

	/**
	 * 
	 * @param source
	 * @param destination
	 * @return error string. A null indicates success.
	 */
	public String copyFile(String source,String destination) {
		String error = null;
		if(DEBUG) log.infof("%s.copyFile: copy %s to %s",TAG,source,destination);
		try {
			Path srcPath  = Paths.get(source);
			Path destPath = Paths.get(destination);
			destPath.getParent().toFile().mkdirs();   // Create any intervening directories
			Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
		}
		catch( InvalidPathException ipe) {
			error = String.format("%s.copyFile: one or both of %s and %s is invalid (%s)",TAG,source,destination,ipe.getLocalizedMessage());
			log.info(error);
		}
		catch( IOException ioe) {
			error = String.format("%s.copyFile: Exception copying %s to %s (%s)",TAG,source,destination,ioe.getLocalizedMessage());
			log.info(error);
		}
		return error;
	}
	
	/**
	 * Visit each file/subdirectory in the tree under the specified 
	 * root directory. Delete it.
	 * @param directory to be deleted along with its contents.
	 */
	public String deleteDirectory(Path directory) {
		String error = null;
		try {
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
		}
		catch(IOException ioe) {
			error = String.format("%s.deleteDirectory: Exception deleting %s (%s)",TAG,directory.toString(),ioe.getLocalizedMessage());
		}
		return error;
	}
	
	/**
	 * Delete it a single file.
	 * @param file to be deleted along with its contents.
	 */
	public String deleteFile(Path file) {
		String error = null;
		try {
			Files.delete(file);
		}
		catch(IOException ioe) {
			error = String.format("%s.deleteFile: Exception deleting %s (%s)",TAG,file.toString(),ioe.getLocalizedMessage());
		}
		return error;
	}
	/**
	 * Write the text 
	 * @param text
	 * @param destination
	 * @return error string. A null indicates success.
	 */
	public String stringToFile(String text,String destination) {
		String error = null;
		if(DEBUG) log.infof("%s.stringToFile: creating %s",TAG,destination);
		try {
			Path destPath = Paths.get(destination);
			// Create any intervening directories .. make sure that there are some.
			if( destPath.getParent().toFile().exists() || destPath.getParent().toFile().mkdirs() ) {
				destPath.getParent().toFile().setWritable(true);
				Files.write(destPath, text.getBytes(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE);
			}
			else {
				error = String.format("%s.stringToFile: Failed to create intervening directories when writing %s",TAG,destination);
				log.info(error);
			}
		}
		catch( InvalidPathException ipe) {
			error = String.format("%s.stringToFile: path %s is invalid (%s)",TAG,destination,ipe.getLocalizedMessage());
			log.info(error);
		}
		catch( IOException ioe) {
			error = String.format("%s.stringToFile: Exception writing to %s (%s)",TAG,destination,ioe.getLocalizedMessage());
			log.info(error);
		}
		catch( UnsupportedOperationException usoe) {
			error = String.format("%s.stringToFile: Unsupported operation writing to %s (%s)",TAG,destination,usoe.getLocalizedMessage());
			log.info(error);
		}
		catch( SecurityException se) {
			error = String.format("%s.stringToFile: Security exception writing to %s (%s)",TAG,destination,se.getLocalizedMessage());
			log.info(error);
		}
		return error;
	}

}