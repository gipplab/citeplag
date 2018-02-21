package org.sciplore.cbpd.main;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.sciplore.cbpd.alg.CitChunk;
import org.sciplore.cbpd.model.Document;
import org.sciplore.preamble.License;

/**
* Executable main program for local test data.
*/

@License (author="Mario Lipinski")

public class TestHandler {

	/**
	 * 
	 *
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Document.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Document doc1 = (Document)unmarshaller.unmarshal(new File("testdata" + File.separator + "test1.xml"));
		Document doc2 = (Document)unmarshaller.unmarshal(new File("testdata" + File.separator + "test2.xml"));
		
		CitChunk checker =new CitChunk(doc1, doc2);
		
		System.out.println(checker.getCitChunkPattern(42));
	}
}
