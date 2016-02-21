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
		try {
			Path srcPath  = Paths.get(source);
			Path destPath = Paths.get(destination);
			destPath.getParent().toFile().mkdirs();   // Create any intervening directories
			Files.copy(srcPath, destPath, StandardCopyOption.COPY_ATTRIBUTES,StandardCopyOption.REPLACE_EXISTING);
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
	public void deleteDirectory(Path directory) throws IOException{

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
	
	/**
	 * Write the text 
	 * @param text
	 * @param destination
	 * @return error string. A null indicates success.
	 */
	public String stringToFile(String text,String destination) {
		String error = null;
		try {
			Path destPath = Paths.get(destination);
			destPath.getParent().toFile().mkdirs();   // Create any intervening directories
			Files.write(destPath, text.getBytes(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE);
		}
		catch( InvalidPathException ipe) {
			error = String.format("%s.stringToFile: path %s is invalid (%s)",TAG,destination,ipe.getLocalizedMessage());
			log.info(error);
		}
		catch( IOException ioe) {
			error = String.format("%s.stringToFile: Exception writing to %s (%s)",TAG,destination,ioe.getLocalizedMessage());
			log.info(error);
		}
		return error;
	}

}