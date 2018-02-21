package org.sciplore.cbpd.alg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.sciplore.cbpd.main.CommandExecutor;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.EncoplotContainer;
import org.sciplore.cbpd.model.Pattern;
import org.sciplore.cbpd.model.TextpatternMember;
import org.sciplore.preamble.License;

/**
* Employs the text-based tool Encoplot (the code can be found in this <a href="http://ceur-ws.org/Vol-502/paper2.pdf">paper</a> by <a href="http://www.brainsignals.de/users/cristian.grozea/">Christian Grozea</a>) to idenify matching text segments.
*/

@License
public class Encoplot {
	List<Pattern> patterns = new ArrayList<Pattern>();
	Document doc1;
	Document doc2;

	public Encoplot(Document doc1, Document doc2) {
		this.doc1 = doc1;
		this.doc2 = doc2;
	}
	
	public List<Pattern> process() throws Exception {
		File txtFile1 = File.createTempFile("doc1", ".txt");
		File txtFile2 = File.createTempFile("doc2", ".txt");
		String output;
		
		int bCnt = 0;
		String doc1Plain = doc1.getPlain().getFulltext();
		TreeMap<Integer,Integer> doc1CntMap = new TreeMap<Integer, Integer>();
		doc1CntMap.put(0, 0);
		for (int i=0; i < doc1Plain.length(); i++) {
			bCnt += doc1Plain.substring(i, i+1).getBytes().length;
			doc1CntMap.put(bCnt, i+1);
		}
		bCnt = 0;
		String doc2Plain = doc2.getPlain().getFulltext();
		TreeMap<Integer,Integer> doc2CntMap = new TreeMap<Integer, Integer>();
		doc2CntMap.put(0, 0);
		for (int i=0; i < doc2Plain.length(); i++) {
			bCnt += doc2Plain.substring(i, i+1).getBytes().length;
			doc2CntMap.put(bCnt, i+1);
		}
		
		try {
			OutputStream fos = new FileOutputStream(txtFile1);
			Writer sw = new OutputStreamWriter(fos, "UTF-8");
			sw.write(doc1Plain.toLowerCase());
			sw.close();
			fos.close();
			
			fos = new FileOutputStream(txtFile2);
			sw = new OutputStreamWriter(fos, "UTF-8");
			sw.write(doc2Plain.toLowerCase());
			sw.close();
			fos.close();
			
			output = new CommandExecutor("python2", "/opt/CbPD Backend/Encoplot Similarity.py", txtFile1.getAbsolutePath(), txtFile2.getAbsolutePath()).exec();
		} catch (Exception e) {
			throw e;
		} finally {
			txtFile1.delete();
			txtFile2.delete();
		}
		
		EncoplotContainer data = null;
		JAXBContext jc = JAXBContext.newInstance(EncoplotContainer.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setProperty("eclipselink.media-type", "application/json");
		unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		data = unmarshaller.unmarshal(new StreamSource(new StringReader(output)), EncoplotContainer.class).getValue();
		
		for (Pattern p : data.getPattern()) {
			p.setDocument1(doc1);
			p.setDocument2(doc2);
		}
		
		Map<Integer, TextpatternMember> doc1m = new TreeMap<Integer, TextpatternMember>();
		for (TextpatternMember m : data.getTextpatternMemberDoc1()) {
			m.setStartCharacter(doc1CntMap.floorEntry(m.getStartCharacter()).getValue());
			m.setEndCharacter(doc1CntMap.floorEntry(m.getEndCharacter()).getValue());
			doc1m.put(m.getStartCharacter(), m);
		}
		doc1.getTextpatternMember().addAll(doc1m.values());
		
		Map<Integer, TextpatternMember> doc2m = new TreeMap<Integer, TextpatternMember>();
		for (TextpatternMember m : data.getTextpatternMemberDoc2()) {
			m.setStartCharacter(doc2CntMap.floorEntry(m.getStartCharacter()).getValue());
			m.setEndCharacter(doc2CntMap.floorEntry(m.getEndCharacter()).getValue());
			doc2m.put(m.getStartCharacter(), m);
		}
		doc2.getTextpatternMember().addAll(doc2m.values());
		
		return data.getPattern();
	}
}
