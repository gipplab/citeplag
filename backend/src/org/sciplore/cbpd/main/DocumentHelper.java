package org.sciplore.cbpd.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.Reference;
import org.sciplore.preamble.License;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
* Converts pdf documents to tex and extracts relevant document information using <a href="http://aye.comp.nus.edu.sg/parsCit/">ParsCit.
*/

@License (author="Mario Lipinski")

public class DocumentHelper {
	Document doc = new Document();
	File file;
	
	DocumentHelper(File pdf) throws FileNotFoundException {
		this.file = pdf;
		if (!pdf.canRead()) {
			throw new FileNotFoundException();
		}
	}
	
	DocumentHelper(String fileName) throws FileNotFoundException {
		file = new File(fileName);
		if (!file.canRead()) {
			throw new FileNotFoundException();
		}
	}
	
	public Document process() throws Exception {
		String txt = "";
		if (Files.probeContentType(Paths.get(file.getPath())).equals("application/pdf")) {
			txt = new CommandExecutor("pdftotext",file.getPath(), "-").exec();
		} else if (Files.probeContentType(Paths.get(file.getPath())).equals("text/plain") || Files.probeContentType(Paths.get(file.getPath())).equals("text/plain") || Files.probeContentType(Paths.get(file.getPath())).equals("application/octet-stream")) {
			txt = Charset.defaultCharset().decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(file.toURI())))).toString();			
		}
		
		String parsCitXml;
		if (!txt.isEmpty()) {
			txt = cleanText(txt);
			
			extractHeader(txt);
			txt = extractSections(txt);
			
			parsCitXml = runParsCit(txt);
//			String out = file.getPath().split(".pdf")[0] + ".parscit.xml";
//			FileWriter fw = new FileWriter(out);
//			fw.write(parsCitXml);
//			fw.close();
		} else if(Files.probeContentType(Paths.get(file.getPath())).equals("text/xml") || Files.probeContentType(Paths.get(file.getPath())).equals("text/html") ) {
			parsCitXml = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(file.toURI())))).toString();			
		} else {
			throw new Exception("Don't know how to handle file.");
		}
		readParsCitXml(parsCitXml);
		return doc;
	}
	
	 private String runParsCit(String txt) throws Exception {
		File txtFile = File.createTempFile("txt", ".txt");
		String xml = null;
		
		try {
			OutputStream fos = new FileOutputStream(txtFile);
			Writer sw = new OutputStreamWriter(fos, "UTF-8");
			sw.write(txt);
			sw.close();
			fos.close();
			
			if(java.net.InetAddress.getLocalHost().getHostName().equals("absinth")) {
				xml = new CommandExecutor("perl.cmd", "-CSD", "D:\\Progs\\ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_all", txtFile.getAbsolutePath()).exec();
			} else {
				xml = new CommandExecutor("perl", "-CSD", "/opt/ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_all", txtFile.getAbsolutePath()).exec();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			txtFile.delete();
		}
		return xml;
	}
	 
	 private void extractHeader(String txt) throws Exception {
			File txtFile = File.createTempFile("txt", ".txt");
			String xml = null;
			
			try {
				OutputStream fos = new FileOutputStream(txtFile);
				Writer sw = new OutputStreamWriter(fos, "UTF-8");
				sw.write(txt);
				sw.close();
				fos.close();
				
				if(java.net.InetAddress.getLocalHost().getHostName().equals("absinth")) {
					xml = new CommandExecutor("perl.cmd", "-CSD", "D:\\Progs\\ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_header", txtFile.getAbsolutePath()).exec();
				} else {
					xml = new CommandExecutor("perl", "-CSD", "/opt/ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_header", txtFile.getAbsolutePath()).exec();
				}
			} catch (Exception e) {
				throw e;
			} finally {
				txtFile.delete();
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			org.w3c.dom.Document xmlDoc;
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			db = dbf.newDocumentBuilder();
			xmlDoc = db.parse(new InputSource(new StringReader(xml)));
			
			XPathExpression abstractExpr = xpath.compile("/algorithms/algorithm[@name='ParsHed']/variant/abstract");
			XPathExpression titleExpr = xpath.compile("/algorithms/algorithm[@name='ParsHed']/variant/title");
			XPathExpression authorsExpr = xpath.compile("/algorithms/algorithm[@name='ParsHed']/variant/author");
			XPathExpression dotExpr = xpath.compile(".");
			
			String abstractText = (String)abstractExpr.evaluate(xmlDoc, XPathConstants.STRING);
			doc.addData("abstract", abstractText);

			String title = (String)titleExpr.evaluate(xmlDoc, XPathConstants.STRING);
			doc.addData("title", title);
			
			NodeList al = (NodeList)authorsExpr.evaluate(xmlDoc, XPathConstants.NODESET);
			for (int i=0; i<al.getLength(); i++) {
				String author = (String)dotExpr.evaluate(al.item(i), XPathConstants.STRING);
				doc.addAuthor(author);
			}
			
	 }
	 
	 private String extractSections(String txt) throws Exception {
			File txtFile = File.createTempFile("txt", ".txt");
			String xml = null;
			
			try {
				OutputStream fos = new FileOutputStream(txtFile);
				Writer sw = new OutputStreamWriter(fos, "UTF-8");
				sw.write(txt);
				sw.close();
				fos.close();
				
				if(java.net.InetAddress.getLocalHost().getHostName().equals("absinth")) {
					xml = new CommandExecutor("perl.cmd", "-CSD", "D:\\Progs\\ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_section", txtFile.getAbsolutePath()).exec();
				} else {
					xml = new CommandExecutor("perl", "-CSD", "/opt/ParsCit" + File.separator + "bin" + File.separator + "citeExtract.pl", "-m", "extract_section", txtFile.getAbsolutePath()).exec();
				}
			} catch (Exception e) {
				throw e;
			} finally {
				txtFile.delete();
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			org.w3c.dom.Document xmlDoc;
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			db = dbf.newDocumentBuilder();
			xmlDoc = db.parse(new InputSource(new StringReader(xml)));
			
//			XPathExpression abstractExpr = xpath.compile("/algorithms/algorithm[@name='ParsHed']/variant/abstract");
//			XPathExpression titleExpr = xpath.compile("/algorithms/algorithm[@name='ParsHed']/variant/title");
			XPathExpression xExpr = xpath.compile("/algorithms/algorithm[@name='SectLabel']/variant/*");
//			XPathExpression dotExpr = xpath.compile(".");
			
			StringBuilder plain = new StringBuilder();
			StringBuilder markup = new StringBuilder("<section><p>");
			NodeList l = (NodeList)xExpr.evaluate(xmlDoc, XPathConstants.NODESET);
			for (int i=0; i<l.getLength(); i++) {
				Node n = l.item(i);
				String content = cleanText(n.getTextContent());
				String mcontent = StringEscapeUtils.escapeXml(content);
//				String mcontent = content;
				switch(n.getNodeName()) {
					case "sectionHeader":
					case "subsectionHeader":
					case "subsubsectionHeader":
						markup.append("</p></section><section><title>");
						markup.append(mcontent);
						markup.append("</title>\n<p>");
						plain.append(content + "\n");
						break;
					case "reference":
						mcontent = mcontent.replaceAll("\n", "<br/>\n");
					case "table":
					case "figureCaption":
					case "listItem":
					case "figure":
					case "tableCaption":
					case "equation":
					case "footnote":
					case "bodyText":
					case "construct":
						markup.append("</p><p>");
						markup.append(mcontent + "\n");
						plain.append(content + "\n");
						break;
					case "keyword":
					case "author":
					case "title":
					case "address":
					case "email":
					case "page":
					case "affiliation":
						break;
					default:
						plain.append(content + "\n");
						markup.append(mcontent + "\n");
						System.err.println("Unknown section: " + n.getNodeName());
						break;
				}
			}
			markup.append("</p></section>");
			doc.setText(markup.toString().replaceAll("<p></p>", "").replaceAll("<section></section>", ""));
			doc.setPlain(plain.toString());
			return plain.toString();
	 }
	 
	 private void readParsCitXml(String xml) throws Exception {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			org.w3c.dom.Document xmlDoc;
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			db = dbf.newDocumentBuilder();
			xmlDoc = db.parse(new InputSource(new StringReader(xml)));
			
			XPathExpression citationExpr = xpath.compile("/algorithms/algorithm[@name='ParsCit']/citationList/citation");
			XPathExpression citTitleExpr = xpath.compile("title");
			XPathExpression citDateExpr = xpath.compile("date");
			XPathExpression citContextExpr = xpath.compile("contexts/context");
			XPathExpression citAuthorExpr = xpath.compile("authors/author");
			XPathExpression citBooktitleExpr = xpath.compile("booktitle");
			XPathExpression citPagesExpr = xpath.compile("pages");
			XPathExpression citEditorExpr = xpath.compile("editor");
			XPathExpression citMarkerExpr = xpath.compile("marker");
			XPathExpression citCtxPosExpr = xpath.compile("@position");
			XPathExpression dotExpr = xpath.compile(".");
			
			NodeList cl = (NodeList)citationExpr.evaluate(xmlDoc, XPathConstants.NODESET);
			for (int i=0; i<cl.getLength(); i++) {
				Document cDoc = new Document();
				String cTitle = (String)citTitleExpr.evaluate(cl.item(i), XPathConstants.STRING);
				if (!cTitle.isEmpty()) {
					cDoc.addData("title", cTitle);
				}
				
				String cDate = (String)citDateExpr.evaluate(cl.item(i), XPathConstants.STRING);
				if (!cDate.isEmpty()) {
					cDoc.addData("date", cDate);
				}
				
				String cBooktitle = (String)citBooktitleExpr.evaluate(cl.item(i), XPathConstants.STRING);
				if (!cBooktitle.isEmpty()) {
					cDoc.addData("booktitle", cBooktitle);
				}

				String cPages = (String)citPagesExpr.evaluate(cl.item(i), XPathConstants.STRING);
				if (!cPages.isEmpty()) {
					cDoc.addData("pages", cPages);
				}
				
				String cEditor = (String)citEditorExpr.evaluate(cl.item(i), XPathConstants.STRING);
				if (!cEditor.isEmpty()) {
					cDoc.addData("editor", cEditor);
				}

				String cMarker = (String)citMarkerExpr.evaluate(cl.item(i), XPathConstants.STRING);
				
				NodeList citAl = (NodeList)citAuthorExpr.evaluate(cl.item(i), XPathConstants.NODESET);
				for (int j=0; j<citAl.getLength(); j++) {
					String author = (String)dotExpr.evaluate(citAl.item(j), XPathConstants.STRING);
					cDoc.addAuthor(author);
				}
				
				Reference ref = new Reference(doc, cMarker, cDoc);
				doc.addOutReference(ref);

				NodeList citCtx = (NodeList)citContextExpr.evaluate(cl.item(i), XPathConstants.NODESET);
				for (int j=0; j<citCtx.getLength(); j++) {
					String character = (String)citCtxPosExpr.evaluate(citCtx.item(j), XPathConstants.STRING);
					ref.addCitation(Integer.parseInt(character));
				}
				
				int citCnt=0;
				for (Citation c: doc.getCitations()) {
					c.setCount(citCnt++);
				}
			}
	 }
	 
	 private String cleanText(String txt) {
//		txt = txt.replaceAll("[^A-Za-z0-9 \\n`~@#|:;'\"\\Q.,-!?[]()<>{}$%^&*_+\\/\\E]", "");
//		txt = txt.replaceAll("[\\s\\-]*\n\\s*", "\n");
		txt = txt.replaceAll("\\-\n", "-\ufeff\n");
		txt = txt.replaceAll(" +", " ");
//		txt = txt.replaceAll("\n+", "\n");
		return txt.trim();
	 }
}
