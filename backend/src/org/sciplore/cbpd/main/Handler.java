package org.sciplore.cbpd.main;

import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.sciplore.cbpd.alg.BibliographicCoupling;
import org.sciplore.cbpd.alg.CitChunk;
import org.sciplore.cbpd.alg.Encoplot;
import org.sciplore.cbpd.alg.GCT;
import org.sciplore.cbpd.alg.LCCS;
import org.sciplore.cbpd.alg.LCCSDist;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.ResponseContainer;
import org.sciplore.preamble.License;

/**
* Executable main program.
*/

@License (author="Mario Lipinski")

public class Handler {
	public static FileWriter fw;
	/**
	 * 
	 *
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			throw new IllegalArgumentException();
		}

		ResponseContainer r = new ResponseContainer();

//		double start = 0;
		
//		start = System.nanoTime();
		Document doc1 = new DocumentHelper(args[0]).process();
//		System.err.println("Import document 1: " + ((System.nanoTime() - start) / (1000*1000*1000)));
		
//		start = System.nanoTime();
		Document doc2 = new DocumentHelper(args[1]).process();
//		System.err.println("Import document 2: " + ((System.nanoTime() - start) / (1000*1000*1000)));

		r.setDoc1(doc1);
		r.setDoc2(doc2);

//		start = System.nanoTime();
		r.disambiguate();
//		System.err.println("Disambiguation: " + ((System.nanoTime() - start) / (1000*1000*1000)));

		// Bibliographic Coupling
//		start = System.nanoTime();
		r.addPatterns(new BibliographicCoupling(doc1, doc2).process());
//		System.err.println("Bibliographic Coupling: " + ((System.nanoTime() - start) / (1000*1000*1000)));

		// LCCS
//		start = System.nanoTime();
		LCCS lccs = new LCCS(doc1, doc2);
		r.addPatterns(lccs.getLccsPattern());
//		System.err.println("LCCS: " + ((System.nanoTime() - start) / (1000*1000*1000)));

		// LCCS dist.
//		start = System.nanoTime();
		LCCSDist lccsDist = new LCCSDist(doc1, doc2);
		r.addPatterns(lccsDist.getLccsDistPattern());
//		System.err.println("LCCS dist.: " + ((System.nanoTime() - start) / (1000*1000*1000)));
		
		// GCT
//		start = System.nanoTime();
		GCT gct = new GCT(doc1, doc2);
		r.addPatterns(gct.getGctPattern(21));
//		System.err.println("GCT: " + ((System.nanoTime() - start) / (1000*1000*1000)));
		
		// Citation Chunking
//		start = System.nanoTime();
		CitChunk citchunk = new CitChunk(doc1, doc2);
		r.addPatterns(citchunk.getCitChunkPattern(40));
//		System.err.println("Citation Chunking: " + ((System.nanoTime() - start) / (1000*1000*1000)));

		// Encoplot
//		start = System.nanoTime();
		r.addPatterns(new Encoplot(doc1, doc2).process());
//		System.err.println("Encoplot: " + ((System.nanoTime() - start) / (1000*1000*1000)));

//		start = System.nanoTime();
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jc = JAXBContext.newInstance(ResponseContainer.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty("eclipselink.media-type", "application/json");
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.marshal(r, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println(sw.toString());
//		System.err.println("Output: " + ((System.nanoTime() - start) / (1000*1000*1000)));
	}
}
