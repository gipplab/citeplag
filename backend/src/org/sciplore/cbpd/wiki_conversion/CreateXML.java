package org.sciplore.cbpd.wiki_conversion;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.sciplore.preamble.License;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@License (author="Patricia Kopecki, Georgi Steffenhagen")

public class CreateXML {
	ReadXML readXML = new ReadXML();
	
	/**
	 * This method creates the new JATS XML.
	 * @param counter counts how many xml files have already been created
	 * @param xmlFile the xml file from wikipedia
	 */
	public void createXML(int counter, String xmlFile) {

		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element article = document.createElement("article");
			article.appendChild(document.createTextNode(""));
			document.appendChild(article);

			Attr articleType = document.createAttribute("article-type");
			articleType.setValue("wikipedia-article");
			article.setAttributeNode(articleType);

			Attr xmlLang = document.createAttribute("xml:lang");
			xmlLang.setValue("de");
			article.setAttributeNode(xmlLang);

			Element front = document.createElement("front");
			front.appendChild(document.createTextNode(""));
			article.appendChild(front);

			Element journalMeta = document.createElement("journal-meta");
			journalMeta.appendChild(document.createTextNode(""));
			front.appendChild(journalMeta);

			Element journalID = document.createElement("journal-id");
			journalID.appendChild(document.createTextNode(""));
			journalMeta.appendChild(journalID);

			Element issn = document.createElement("issn");
			issn.appendChild(document.createTextNode(""));
			journalMeta.appendChild(issn);

			Element articleMeta = document.createElement("article-meta");
			articleMeta.appendChild(document.createTextNode(""));
			front.appendChild(articleMeta);

			Element articleID = document.createElement("article-id");
			articleID.appendChild(document
					.createTextNode(ReadXML.pageIDElement));
			articleMeta.appendChild(articleID);

			Element articleCategories = document
					.createElement("article-categories");
			articleCategories.appendChild(document.createTextNode(""));
			articleMeta.appendChild(articleCategories);

			Element subjGroup = document.createElement("subj-group");
			subjGroup.appendChild(document.createTextNode(""));
			articleCategories.appendChild(subjGroup);

			Element subject = document.createElement("subject");
			subject.appendChild(document.createTextNode(ReadXML.modelElement));
			subjGroup.appendChild(subject);

			Element titleGroup = document.createElement("title-group");
			titleGroup.appendChild(document.createTextNode(""));
			articleMeta.appendChild(titleGroup);

			Element articleTitle = document.createElement("article-title");
			articleTitle.appendChild(document
					.createTextNode(ReadXML.titleElement));
			titleGroup.appendChild(articleTitle);

			Element contribGroup = document.createElement("contrib-group");
			contribGroup.appendChild(document.createTextNode(""));
			articleMeta.appendChild(contribGroup);

			Element contrib = document.createElement("contrib");
			contrib.appendChild(document.createTextNode(""));
			contribGroup.appendChild(contrib);

			Attr contribType = document.createAttribute("contrib-type");
			contribType.setValue("author");
			contrib.setAttributeNode(contribType);

			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(""));
			contrib.appendChild(name);

			Element surname = document.createElement("surname");
			surname.appendChild(document.createTextNode(""));
			name.appendChild(surname);

			Element givenNames = document.createElement("given-names");
			givenNames.appendChild(document
					.createTextNode(ReadXML.usernameElement));
			name.appendChild(givenNames);

			Element pubDate = document.createElement("pub-date");
			pubDate.appendChild(document.createTextNode(""));
			articleMeta.appendChild(pubDate);

			Attr pubType = document.createAttribute("pub-type");
			pubType.setValue("epub");
			pubDate.setAttributeNode(pubType);

			Element year = document.createElement("year");
			year.appendChild(document.createTextNode(ReadXML.timestampElement));
			pubDate.appendChild(year);

			Element body = document.createElement("body");
			body.appendChild(document.createTextNode(""));
			article.appendChild(body);

			Element sec = document.createElement("sec");
			sec.appendChild(document.createTextNode(""));
			body.appendChild(sec);

			Element title = document.createElement("title");
			title.appendChild(document.createTextNode(""));
			sec.appendChild(title);

			Element p = document.createElement("p");
			p.appendChild(document.createTextNode(ReadXML.textElement));
		//	System.out.println(ReadXML.textElement);
			sec.appendChild(p);

			Element back = document.createElement("back");
			back.appendChild(document.createTextNode(""));
			article.appendChild(back);

			Element refList = document.createElement("ref-list");
			refList.appendChild(document.createTextNode(""));
			back.appendChild(refList);

			if (ReadXML.citation != null) {

				for (Map.Entry<String, List<String>> entry : ReadXML.citation
						.entrySet()) {

					String entryValue1 = "";
					String entryKey1 = "";
					entryKey1 = entry.getKey().toString();
					entryValue1 = entry.getValue().toString();

					Element ref = document.createElement("ref");
					ref.appendChild(document.createTextNode(""));
					refList.appendChild(ref);

					Attr refId = document.createAttribute("id");
					refId.setValue(entryValue1);
					ref.setAttributeNode(refId);

					Element elementCitation = document
							.createElement("element-citation");
					elementCitation.appendChild(document.createTextNode(""));
					ref.appendChild(elementCitation);

					Attr publicationType = document
							.createAttribute("publication-type");
					publicationType.setValue("journal");
					elementCitation.setAttributeNode(publicationType);

					Element comment = document.createElement("comment");
					comment.appendChild(document.createTextNode(entryKey1));
					elementCitation.appendChild(comment);
				}
			}
			if (ReadXML.citation3final != null) {

				for (Map.Entry<String, List<String>> entry : ReadXML.citation3final
						.entrySet()) {

					String entryValue = "";
					String entryKey = "";
					entryKey = entry.getKey().toString();
					entryValue = entry.getValue().toString();

					Element ref = document.createElement("ref");
					ref.appendChild(document.createTextNode(""));
					refList.appendChild(ref);

					Attr refId = document.createAttribute("id");
					refId.setValue(entryValue);
					ref.setAttributeNode(refId);

					Element elementCitation = document
							.createElement("element-citation");
					elementCitation.appendChild(document.createTextNode(""));
					ref.appendChild(elementCitation);

					Attr publicationType = document
							.createAttribute("publication-type");
					publicationType.setValue("journal");
					elementCitation.setAttributeNode(publicationType);

					Element comment = document.createElement("comment");
					comment.appendChild(document.createTextNode(entryKey));
					elementCitation.appendChild(comment);
				}
			}

			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource domSource = new DOMSource(document);

			File newName = new File(xmlFile
					.replace("page", "jats"));

			// insert here the path where the file should be written to
			File file = new File(
					"C://Users//Trapicia//Desktop//citeplag-workspace//wiki-jats-"
							+ DOMImplementation.numberOfFolder);
			file.mkdir();
			StreamResult streamResult = new StreamResult(file + "//" + newName);
			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
