package org.sciplore.cbpd.wiki_conversion;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.sciplore.preamble.License;

@License (author="Patricia Kopecki, Georgi Steffenhagen")

public class ExtractXMLsFromWikiDump {
	String[] names = null;

	
	/**
	 * This method extracts single xml files from the big wiki xml dump and
	 * writes them into a directory.
	 * @throws IOException
	 */
	public void process() throws IOException {
		// target_dir = insert here the path where the wiki xml dump is located
		String target_dir = "C://Users//Trapicia//Desktop//citeplag-workspace//wiki-pages";
		File source = new File(target_dir);
		File[] files = source.listFiles();

		int count = 0;
		int counterString = 0;
		int z = 1;

		String line;
		String lineNext;

		File actualFile = null;
		BufferedReader buffReader = null;
		FileWriter fileWriter = null;
		BufferedWriter writer = null;

		for (File f : files) {

			if (f.isFile()) {

				buffReader = new BufferedReader(new FileReader(f));

				while ((line = buffReader.readLine()) != null) {

					if (line.contains("</mediawiki>")) {
						buffReader.close();
						return;
					}

					if (line.contains("<mediawiki")) {
						while (!(buffReader.readLine()).contains("</siteinfo>")) {

						}

					}

					count++;
					counterString++;

					if (count % 10000 == 0) {
						z++;

					}
					if (count == 10001) {
						count = 0;
					}

					actualFile = createFileAndDirectory(f, count, z,
							counterString);

					fileWriter = new FileWriter(actualFile);
					writer = new BufferedWriter(fileWriter);

					writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
							+ "\n");
					if (line.contains("<page>")) {
						writer.write(line + "\n");
					}

					while (!(lineNext = buffReader.readLine())
							.contains("</page>")) {
						writer.write(lineNext + "\n");

					}

					writer.write(lineNext);
					writer.flush();
					writer.close();

				}

			}

		}
	}

	
	
	/**
	 * This method creates a new directory after every 10.000 extracted wiki xml files.
	 * @param f wikipedia file
	 * @param counter counter to count how many single xml files have been created
	 * @param z counter
	 * @param counterString counter in String format
	 * @return returns the final xml file 
	 * @throws IOException
	 */
	private File createFileAndDirectory(File f, int counter, int z,
			int counterString) throws IOException {
		// insert here the path to the directory
		String dirName = "C://Users//Trapicia//Desktop//citeplag-workspace//wiki-done";

		if (counter <= 10001) {

			dirName = dirName + z;
			System.out.println("dirName: " + dirName);

		}

		Integer count = new Integer(counterString);
		String counterString1 = count.toString();

		String fileName = f.getName().replace("s", "_" + counterString1);

		File dir = new File(dirName);
		dir.mkdir();

		File actualFile = new File(dir, fileName);
		System.out.println("actualFile: " + actualFile);
		actualFile.createNewFile();

		return actualFile;
	}


	/**
	 * The main method starts the extraction of the single xml files from the wiki xml dump
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ExtractXMLsFromWikiDump converter = new ExtractXMLsFromWikiDump();
		converter.process();
		System.out.println("done");
	}

}
