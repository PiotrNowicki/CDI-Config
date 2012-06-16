package eu.awaketech.cdiconfig;

import java.io.File;
import java.io.FileFilter;

/**
 * Selects only files that are within <code>WEB-INF</code> directory and have <code>properties</code>
 * extension.
 * 
 * @author Piotr Nowicki
 */
public class PropertyFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {

		// TODO: Is this ever possible in real-life?
		if (pathname == null) {
			return false;
		}

		// We're not investigating sub-directories
		boolean isDirectory = pathname.isDirectory();

		/*
		 * FIXME: Change to something more appropriate - user can have a dir named "classes" on the regular
		 * path - should it also be scanned for 'properties'?
		 */
		boolean isWebInfResource = pathname.getAbsolutePath().contains("classes/");

		if (isDirectory || !isWebInfResource) {
			return false;
		}

		String extension = getExtension(pathname.getName());

		if (extension.equals("properties")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * Returns filename extension. Returns empty String if no extension is defined. E.g.:
	 * <ul>
	 * <li><code>myFile.dat</code>, returns <code>dat</code></li>
	 * <li><code>myFile.with.dots.properties</code>, returns <code>properties</code></li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * This method never returns null and is null-argument safe.
	 * </p>
	 * 
	 * @param filename
	 * @return extension of the <code>filename</code> without the trailing dot.
	 */
	protected String getExtension(String filename) {
		if (filename == null) {
			return "";
		}

		int lastDotIdx = filename.lastIndexOf(".");

		if (lastDotIdx == -1) {
			return "";
		}

		return filename.substring(lastDotIdx + 1);
	}
}
