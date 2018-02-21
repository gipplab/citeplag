package org.sciplore.cbpd.wiki_conversion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.mylyn.wikitext.core.parser.*;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.sciplore.preamble.License;

@License (author="Patricia Kopecki, Georgi Steffenhagen")

public class ReadXML {

	public static String titleElement;

	public static String pageIDElement;

	public static String timestampElement;

	public static String modelElement;

	public static String usernameElement;

	public static String textElement = "";

	public static File xmlFile;
	public static String filePath;

	static public int deleteCount = 0;
	static public Integer refInteger;

	static public String key;
	static public String value;

	static public Map<String, List<String>> citation;
	static public Map<String, List<String>> citation3;
	static public Map<String, List<String>> citation3final;

	static public List<String> matcherlist = new ArrayList<String>();

	public long start;
	public int counter = 0;
	public long sum;

	public ReadXML() {

	}

	/**
	 * This method reads every wiki xml file from the given folder It passes the
	 * xml file and its path to the readXML method
	 * 
	 * @param folder
	 *            folder from which the wiki xml file is read
	 */
	public void listFilesFromFolder(final File folder) {
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			start = System.currentTimeMillis();
			System.out.println("start: " + start);
			File file = listOfFiles[i];

			counter++;
			System.out.println("counter: " + counter);
			if (file.isFile()) {
			String stringXML = file.getName().toString();
			
			String folderToString = folder.toString().replace("\\", "//");
			filePath = folderToString + "//" + stringXML;

			readXML(stringXML, filePath);

		}
		}
	}

	/**
	 * This method reads the xmlFile using the DOM Parser. It converts the wiki
	 * markup into html and extracts the references from the wiki xml file.
	 * 
	 * @param xmlFile
	 *            the wiki xml file
	 * @param filePath
	 *            the path to the wiki xml file
	 */
	public void readXML(String xmlFile, String filePath) {
		try {

			File xmlFile1 = new File(filePath);

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile1);

			doc.getDocumentElement().normalize();
			NodeList nodeListPage = doc.getElementsByTagName("page");
			NodeList nodeListRevision = doc.getElementsByTagName("revision");
			NodeList nodeListContributor = doc
					.getElementsByTagName("contributor");

			for (int temp = 0; temp < nodeListPage.getLength(); temp++) {
				Node node = nodeListPage.item(temp);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element page = (Element) node;
					if (page.getElementsByTagName("title").item(0) != null) {
						titleElement = page.getElementsByTagName("title")
								.item(0).getTextContent();

					}
					titleElement = "<h1>" + titleElement + "</h1><p>";
					if (titleElement.contains("File:")
							|| titleElement.contains("Category:")
							|| titleElement.contains("Wikipedia:")
							|| titleElement.contains("Portal:")) {
						deleteCount++;

						return;
					}
					if (page.getElementsByTagName("id").item(0) != null) {
						pageIDElement = page.getElementsByTagName("id").item(0)
								.getTextContent();
					}

					if (page.getElementsByTagName("text").item(0) != null) {

						String markupWiki = page.getElementsByTagName("text")
								.item(0).getTextContent();

						MarkupParser markupParser = new MarkupParser();
						markupParser.setMarkupLanguage(new MediaWikiLanguage());
						String htmlContent = markupParser
								.parseToHtml(markupWiki);

						textElement = htmlContent;

						
						
				//-------------------------------------------------------------------		
						
						
						citation3 = new HashMap<String, List<String>>();
						citation3final = new HashMap<String, List<String>>();

						List<String> valueSet3 = new ArrayList<String>();

						Pattern pattern3 = Pattern.compile("<ref name=(.*?)>");
						Matcher matcher3 = pattern3.matcher(page
								.getElementsByTagName("text").item(0)
								.getTextContent());

						String stringMatch = "";
						int counter = 0;

						while (matcher3.find()) {

							stringMatch = matcher3.group(1);
							stringMatch = stringMatch.trim();

							if (stringMatch.length() > 0
									&& stringMatch
											.charAt(stringMatch.length() - 1) == '/') {
								stringMatch = stringMatch.substring(0,
										stringMatch.length() - 1);
							}

							if (stringMatch.isEmpty()) {

							} else {

								if (citation3.containsKey(stringMatch)) {
									counter++;
									citation3.get(stringMatch).add(
											"B" + counter);

								} else {
									counter++;
									valueSet3 = new ArrayList<String>();
									valueSet3.add("B" + counter);
									citation3.put(stringMatch, valueSet3);

								}
							}
						}
						
						
						String stringMatch1 = "";
						String stringMatch2 = "";
						List<String> entryValue = new ArrayList<String>();

						for (Iterator<Map.Entry<String, List<String>>> it = citation3
								.entrySet().iterator(); it.hasNext();) {
							Map.Entry<String, List<String>> entry = it.next();

							entryValue = entry.getValue();

							Pattern pattern7 = Pattern.compile("<ref name="
									+ entry.getKey() + ">(.*?)</ref>");
							Matcher matcher7 = pattern7.matcher(page
									.getElementsByTagName("text").item(0)
									.getTextContent());

							while (matcher7.find()) {

								stringMatch1 = matcher7.group(1);

							}

							citation3final.put(stringMatch1, entryValue);

							Pattern patternS7 = Pattern.compile("<p>(.*?)</p>");
						
							Matcher matcherS7 = patternS7.matcher(stringMatch1);


							while (matcherS7.find()) {

								stringMatch2 = matcherS7.group(1);
								matcherlist.add(stringMatch2);

							}

							for (int x = 0; x < matcherlist.size(); x++) {

								if (textElement.contains(matcherlist.get(x))) {

									textElement = textElement.replace(
											matcherlist.get(x), "");
								}
							}

						}

						Pattern pattern8 = Pattern.compile("<ref name=(.*?)>");
						Matcher matcher8 = pattern8.matcher(page
								.getElementsByTagName("text").item(0)
								.getTextContent());

						List<String> citList8 = new ArrayList<String>();
						String stringMatch3 = "";

						while (matcher8.find()) {

							stringMatch3 = matcher8.group(1);
							if (stringMatch3.length() > 0
									&& stringMatch3.charAt(stringMatch3
											.length() - 1) == '/') {
								stringMatch3 = stringMatch3.substring(0,
										stringMatch3.length() - 1);
							}
							citList8.add(stringMatch3);

						}

						int counti8 = 0;
						for (int z = 0; z < citList8.size(); z++) {

							if (textElement.contains(citList8.get(z))) {

								counti8++;
								Integer intcount = new Integer(counti8);
								refInteger = intcount;

								String stringint = "[" + intcount.toString()
										+ "]";
								String replaceString = citList8.get(z);

								if (replaceString.isEmpty()) {

								} else {
									textElement = textElement.replaceFirst(
											Pattern.quote(replaceString),
											stringint);
								}
							}
						}

						Pattern pattern = Pattern.compile("<ref>(.*?)</ref>");
						Matcher matcher = pattern.matcher(page
								.getElementsByTagName("text").item(0)
								.getTextContent());
						citation = new HashMap<String, List<String>>();
						List<String> valueSet;
						int count = counter;
						String s = "";
						while (matcher.find()) {

							s = matcher.group(1);

							if (s.isEmpty()) {

							} else {

								if (citation.containsKey(s)) {
									count++;
									citation.get(s).add("B" + count);

								} else {
									count++;
									valueSet = new ArrayList<String>();
									valueSet.add("B" + count);
									citation.put(s, valueSet);

								}
							}
						}

						Pattern pattern1 = Pattern.compile("<ref>(.*?)</ref>");
						Matcher matcher1 = pattern1.matcher(page
								.getElementsByTagName("text").item(0)
								.getTextContent());

						List<String> citList = new ArrayList<String>();
						List<String> stringMatch4List = new ArrayList<String>();
						String stringMatch4 = "";
						String stringMatch5 = "";

						while (matcher1.find()) {

							stringMatch4 = matcher1.group(1); 
							
							stringMatch4List.add(stringMatch4);

						}

						for (int g = 0; g < stringMatch4List.size(); g++) {

							Pattern patternStr = Pattern
									.compile("<p>(.*?)</p>");
							Matcher matcherStr = patternStr.matcher(stringMatch4List.get(g));
							
							while (matcherStr.find()) {

								stringMatch5 = matcherStr.group(1); 
								
								citList.add(stringMatch5);

							}
						}

						int counti = counti8;
						for (int z = 0; z < citList.size(); z++) {

							if (textElement.contains(citList.get(z))) {
								counti++;
								Integer intcount = new Integer(counti);
								refInteger = intcount;

								String stringint = "[" + intcount.toString()
										+ "]";

								String replaceString = citList.get(z);

								if (replaceString.isEmpty()) {

								} else {
									textElement = textElement.replaceFirst(
											Pattern.quote(replaceString),
											stringint);
								}
							}
						}
					}
					
		//-------------------------------------------------------------------
					textElement = titleElement + textElement;

				}

				for (int temp1 = 0; temp1 < nodeListRevision.getLength(); temp1++) {
					Node node1 = nodeListRevision.item(temp1);

					if (node1.getNodeType() == Node.ELEMENT_NODE) {

						Element revision = (Element) node1;

						if (revision.getElementsByTagName("timestamp").item(0) != null) {

							timestampElement = revision
									.getElementsByTagName("timestamp").item(0)
									.getTextContent().substring(0, 10);

						}

						if (revision.getElementsByTagName("model").item(0) != null) {
							modelElement = revision
									.getElementsByTagName("model").item(0)
									.getTextContent();
						}
					}
				}

				for (int temp2 = 0; temp2 < nodeListContributor.getLength(); temp2++) {
					Node node2 = nodeListContributor.item(temp2);

					if (node2.getNodeType() == Node.ELEMENT_NODE) {

						Element contributor = (Element) node2;

						if (contributor.getElementsByTagName("username")
								.item(0) != null) {
							usernameElement = contributor
									.getElementsByTagName("username").item(0)
									.getTextContent();
						}

					}
				}
				long stop = System.currentTimeMillis();
				System.out.println("stop: " + stop);
				long end = stop - start;
				System.out.println("time used for reading: " + end);

				sum = sum + end;
				System.out.println("summe : " + sum);
				System.out.println("durchschnittszeit für conversion: " + sum / counter);

				int counter = 0;
				CreateXML createXML = new CreateXML();

				createXML.createXML(counter, xmlFile);
				counter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
