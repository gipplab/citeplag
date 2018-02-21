package org.sciplore.cbpd.wiki_conversion;

import java.io.File;
import org.sciplore.preamble.License;

@License (author="Patricia Kopecki, Georgi Steffenhagen")

public class DOMImplementation {
	public static int numberOfFolder = 1;
	public static ReadXML readxml = new ReadXML();

	
	/**
	 * This method reads all files from a folder
	 * @param folder
	 */
	public static void listFilesForFolder(final File folder) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				readxml.listFilesFromFolder(fileEntry);

				numberOfFolder++;

			}
		}

	}

	
	/**
	 * starts the conversion of wiki xml to jats xml
	 * @param args path where wikipedia xml's are stored
	 */
	public static void main(String[] args) {

		final File folder = new File(args[0]);
		listFilesForFolder(folder);
		System.out.println("done");

	}

}
