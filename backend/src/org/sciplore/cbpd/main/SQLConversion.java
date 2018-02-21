package org.sciplore.cbpd.main;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import org.sciplore.preamble.License;

import org.json.*;

/**
 * Uses the JSON-output by Handler.java to convert it to SQL-Queries to fill the DB-Schema described in /database/
*/

@License (author="Moritz Bock")

public class SQLConversion {
	
	// The JSON-Object with the data in it.
	private JSONObject json;
	
	// A representation of the database with public variables.
	private Database database = new Database();
	
	// The allowed data. Will be filled with allwedDataStringArray at construction
	private ArrayList<String> allowedData = new ArrayList<String>();
	
	// will automatically imported to allowedData at construction.
	private static String[] allowedDataStringArray = {
			"pubmed",
			"pmc",
			"doi",
			"medline",
			"title",
			"journal",
			"file",
			"xmlfile",
			"txtfile",
			"type",
			"year",
			"month",
			"author_key",
			"title_key",
			"pages",
			"date",
			"booktitle"
	};
	
	
	/**
	 * 
	 * @param args
	 * 			# of arguments: min. 1, max. 1
	 *            1. argument : Path to the JSON-Output-file by Handler.java
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}
		String json = SQLConversion.readFile(args[0], StandardCharsets.UTF_8);
		SQLConversion conv = new SQLConversion(json);
		System.out.println(conv.getSQL());
		Database db = conv.getDatabase(); // debug-object
		System.out.println(db);
		System.out.println(" -- Conversion finished\n");
		
	}
	
	/**
	 * Constructor
	 * 
	 * Sets the JSON to output the SQL from.
	 * 
	 * @param json
	 * 			The JSON-String to convert to SQL.
	 */
	public SQLConversion(String json)
	{
		this.setJSON(json);
		this.allowedData = new ArrayList<String>();
		this.allowedData.addAll(Arrays.asList(this.allowedDataStringArray));
	}
	
	/**
	 * Creates the database object from the json object.
	 * Does not instanciate (so does not remove as well) one of them.
	 * 
	 * @throws Exception
	 */
	public void createDatabaseFromJSON() throws Exception
	{
		this.parseDocument(json.getJSONObject("doc1"));
		
		this.parseDocument(json.getJSONObject("doc2"));
		
		this.parsePattern(json.getJSONArray("pattern"));
	}
	
	/**
	 * Converts the JSON to SQL.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String getSQL() throws Exception
	{
		this.createDatabaseFromJSON();
		
		return this.database.export();
	}
	
	/**
	 * Reads a file and outputs its contents.
	 * 
	 * @param path
	 * 			Path to the file.
	 * @param encoding
	 * 			The file-encoding.
	 * @return
	 * 			String: The content of the file.
	 * @throws IOException
	 */
	static String readFile(String path, Charset encoding) 
	  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	/**
	 * Setter for the JSON-String to be converted.
	 * @param json
	 */
	public void setJSON(String json)
	{
		this.json = new JSONObject(json);
	}
	
	/**
	 * Getter for the database object
	 * 
	 * @return
	 * 			Database
	 */
	public Database getDatabase() {
		return this.database;
	}

	/**
	 * Parses one object from the JSON-Object
	 * 
	 * @param jo
	 * 			The JSONObject containing the document data
	 * @throws Exception
	 */
	private long parseDocument(JSONObject jo) throws Exception
	{
		long document_id = jo.getLong("document_id");

		if(!jo.isNull("Text"))
			this.parseText(jo.getJSONObject("Text").getJSONObject("Text"), document_id);
		
		if(!jo.isNull("Author"))
			this.parseAuthor(jo.getJSONArray("Author"), document_id);
		
		if(!jo.isNull("Citation"))
			this.parseCitation(jo.getJSONObject("Citation").getJSONArray("Citation"), document_id);
		
		if(!jo.isNull("data"))
			this.parseDocumentData(jo.getJSONArray("data"), document_id);
		
		if(!jo.isNull("inReference"))
			throw new Exception("inReference found but no inReference parsing implemented!");
		
		if(!jo.isNull("Reference"))
			this.parseReference(jo.getJSONArray("Reference"), document_id);

		if(!jo.isNull("CitationpatternMember"))
		{
			JSONObject citpatmem = jo.getJSONObject("CitationpatternMember");
			if(!citpatmem.isNull("CitationpatternMember"))
			{
				JSONArray ja = citpatmem.getJSONArray("CitationpatternMember");
				this.parseCitationpatternMember(ja);
			}
		}
			
		//if(!jo.isNull("pattern"))
		//	this.parsePattern(jo.getJSONArray("pattern"), doc.document_id);
		System.out.println(" -- [WARNING] Inner-doc pattern-parsing is disabled!");
		
		if(!jo.isNull("TextpatternMember"))
			this.parseTextpatternMember(jo.getJSONObject("TextpatternMember").getJSONArray("TextpatternMember"), document_id);
		
		return document_id;
	}
	
	/**
	 * Parses the citationpattern member from an JSON array.
	 * 
	 * @param ja
	 * 			The JSONArray containing the citationpattern members.
	 */
	public void parseCitationpatternMember(JSONArray ja)
	{
		for(int i = 0; i < ja.length(); i++)
		{
			JSONObject jo = ja.getJSONObject(i);
			Citeplag_citationpattern_member c = new Citeplag_citationpattern_member();
			c.pattern_member_id = jo.getLong("pattern_member_id");
			c.pattern_id = jo.getLong("pattern_id");
			c.count = jo.getLong("count");
			c.document_id = jo.getLong("document_id");
			c.gap = jo.getInt("gap");
			c.db_citation_id = jo.getLong("db_citation_id");
			database.citeplag_citationpattern_member.add(c);
		}
	}
	
	/**
	 * Parses the Text from an JSON document array.
	 * This will only insert in the database if the text is set and doc_id matches the value of "document_id" from the Object.
	 * 
	 * @param jo
	 * 			The JSONObject containing the keys "document_id" and (optional, but needed for inserting in database) fulltext
	 * @param doc_id
	 * 			The doc_id which should match the value of "document_id" from the jo.
	 */
	private void parseText(JSONObject jo, long doc_id)
	{
		if(jo.getLong("document_id") == doc_id && !jo.isNull("fulltext"))
		{
			Citeplag_document_text doc = new Citeplag_document_text();
			doc.document_id = jo.getLong("document_id");
			doc.fulltext = jo.getString("fulltext");
			database.citeplag_document_text.add(doc);
		}
	}
	
	/**
	 * Parses an Array of Patterns
	 * 
	 * @param ja
	 * 			The array containing the patterns.
	 */
	private void parsePattern(JSONArray ja)
	{
		for(int i = 0; i < ja.length(); i++)
		{
			JSONObject pat = ja.getJSONObject(i);
			Citeplag_pattern p = new Citeplag_pattern();
			p.document_id1 = pat.getLong("document_id1");
			p.document_id2 = pat.getLong("document_id2");
			p.pattern_id = pat.getLong("pattern_id");
			p.pattern_score = pat.getInt("pattern_score");
			

			// It seems that in the most occurences procedure is a string containing a number.
			// That would result in an exception. So we'll convert it if that happends.
			try {
				p.procedure = pat.getInt("procedure");
			} 
			catch(JSONException e)
			{
				p.procedure = new Integer(pat.getString("procedure"));
			}
			database.citeplag_pattern.add(p);
		}
	}
	
	/**
	 * Parses the reference of a document.
	 * 
	 * @param ja
	 * 			An array containing the references.
	 * @param doc_id
	 * 			The id of the document containing the references.
	 * @throws Exception
	 */
	private void parseReference(JSONArray ja, long doc_id) throws Exception
	{
		for(int i = 0; i < ja.length(); i++)
		{
			JSONObject ref = ja.getJSONObject(i);
			Citeplag_reference r = new Citeplag_reference();
			r.db_reference_id = ref.getLong("db_reference_id");
			r.cont_document_id = doc_id;
			r.doc_reference_id = ref.getString("doc_reference_id");
			r.ref_document_id = this.parseDocument(ref.getJSONObject("Document"));
			database.citeplag_reference.add(r);
		}
	}
	
	/**
	 * Parses the citations of an document
	 * 
	 * @param ja
	 * 			The Array containing the citations
	 * @param doc_id
	 * 			The document containing the citations.
	 */
	private void parseCitation(JSONArray ja, long doc_id)
	{
		for(int i = 0; i < ja.length(); i++)
		{
			JSONObject cit = ja.getJSONObject(i);
			Citeplag_citation c = new Citeplag_citation();
			c.db_citation_id = cit.getLong("db_citation_id");
			c.document_id = doc_id;
			c.db_reference_id = cit.getLong("db_reference_id");
			c.count = cit.getInt("count");
			c.character = cit.getLong("character");
			c.word = cit.getInt("word");
			c.sentence = cit.getInt("sentence");
			c.paragraph = cit.getInt("paragraph");
			
			// It seems that in the most occurences section is 0.
			// That would result in an exception.
			try {
				c.section = cit.getString("section");
			} 
			catch(JSONException e)
			{
				c.section = new Integer(cit.getInt("section")).toString();
			}
			this.database.citeplag_citation.add(c);
		}
	}
	
	/**
	 * Parses the author(s) of an document.
	 * 
	 * @param ja
	 * 			The JSONArray containing the author(s) (as Strings)
	 * 			The last space in the string is the splitpoint for Firstname and Lastname (so Lastname should not contains spaces)
	 * @param doc_id
	 * 			The id of the document the authors created
	 */
	private void parseAuthor(JSONArray ja, long doc_id)
	{
		for(int i = 0; i < ja.length(); i++)
		{
			String name = ja.getString(i);
			int s = name.lastIndexOf(' ');
			Citeplag_authors a = new Citeplag_authors();
			a.document_id = doc_id;
			if(s == -1) 
			{
				a.firstname = "";
				a.lastname = name;
			}
			else
			{
				a.firstname = name.substring(0, s);
				a.lastname = name.substring(s+1);
			}
			database.citeplag_authors.add(a);
		}
	}
	
	/**
	 * Parses TextpatternMembers of a document.
	 * 
	 * @param ja
	 * 			The array containing the textpattern members.
	 * @param doc_id
	 * 			The document id
	 */
	private void parseTextpatternMember(JSONArray ja, long doc_id)
	{
		for(int i = 0; i < ja.length(); i++){
			JSONObject c = ja.getJSONObject(i);
			Citeplag_textpattern_member tpm = new Citeplag_textpattern_member();
			tpm.pattern_member_id = c.getLong("patternMemberId");
			tpm.pattern_id = c.getLong("pattern_id");
			tpm.document_id = doc_id;
			tpm.start_character = c.getLong("start_character");
			tpm.end_character = c.getLong("end_character");
			database.citeplag_textpattern_member.add(tpm);
		}
	}
	
	/**
	 * Parses the document data
	 * 
	 * @param ja
	 * 			The array containing the document data
	 * @param doc_id
	 * 			The id of the document the data belongs to
	 */
	private void parseDocumentData(JSONArray ja, long doc_id)
	{
		for(int i = 0; i < ja.length(); i++)
		{
			JSONObject obj = ja.getJSONObject(i);
			String type = obj.getString("type");
			String value = obj.getString("value");
			if(this.allowedData.contains(type) && !value.isEmpty())
			{
				Citeplag_document_data data = new Citeplag_document_data();
				data.document_id = doc_id;
				data.type = type;
				data.value = value;
				database.citeplag_document_data.add(data);
			}
			else
			{
				System.out.println(" -- [WARNING] Skipped citeplag_document_data");
				System.out.println(" -- type: " + type.replace("\n", "\n --") + " value: " + value.replace("\n", "\n --"));
			}
		}
	}
	
	/**
	 * A model of the database to easily add data to.
	 * The export-method of this class exports an SQL-dump.
	 * 
	 * @author Moritz Bock
	 */
	class Database {
		/*
		 * The Tables in the database
		 */
		ArrayList<Citeplag_document_text>          citeplag_document_text          = new ArrayList<Citeplag_document_text>();
		ArrayList<Citeplag_document_data>          citeplag_document_data          = new ArrayList<Citeplag_document_data>();
		ArrayList<Citeplag_authors>                citeplag_authors                = new ArrayList<Citeplag_authors>();
		ArrayList<Citeplag_citationpattern_member> citeplag_citationpattern_member = new ArrayList<Citeplag_citationpattern_member>();
		ArrayList<Citeplag_pattern>                citeplag_pattern                = new ArrayList<Citeplag_pattern>();
		ArrayList<Citeplag_textpattern_member>     citeplag_textpattern_member     = new ArrayList<Citeplag_textpattern_member>();
		ArrayList<Citeplag_reference>              citeplag_reference              = new ArrayList<Citeplag_reference>();
		ArrayList<Citeplag_citation>               citeplag_citation               = new ArrayList<Citeplag_citation>();
		
		/**
		 * SQL-Dump the data currently stored in this object
		 * 
		 * @return
		 */
		public String export()
		{
			StringBuilder sql = new StringBuilder("");
			
			
			sql.append("\n\n -- Filling citeplag_document_data \n\n");
			for(int i = 0; i < citeplag_document_data.size(); i++)
			{
				Citeplag_document_data d = citeplag_document_data.get(i);
				Insert insert = new Insert("citeplag_document_data");
				insert.add("document_id", d.document_id);
				insert.add("type", d.type);
				insert.add("value", d.value);
				sql.append(insert.toString());
			}

			sql.append("\n\n -- Filling citeplag_document_text \n\n");
			for(int i = 0; i < citeplag_document_text.size(); i++)
			{
				Citeplag_document_text t = citeplag_document_text.get(i);
				Insert insert = new Insert("citeplag_document_text");
				insert.add("document_id", t.document_id);
				insert.add("fulltext", t.fulltext);
				sql.append(insert.toString());
			}

			sql.append("\n\n -- Filling citeplag_authors \n\n");
			for(int i = 0; i < citeplag_authors.size(); i++)
			{
				Citeplag_authors a = citeplag_authors.get(i);
				Insert insert = new Insert("citeplag_authors");
				insert.add("author_id", a.author_id);
				insert.add("document_id", a.document_id);
				insert.add("firstname", a.firstname);
				insert.add("lastname", a.lastname);
				sql.append(insert.toString());
			}

			sql.append("\n\n -- Filling citeplag_reference \n\n");
			for(int i = 0; i < citeplag_reference.size(); i++)
			{
				Citeplag_reference r = citeplag_reference.get(i);
				Insert insert = new Insert("citeplag_reference");
				insert.add("db_reference_id", r.db_reference_id);
				insert.add("cont_document_id", r.cont_document_id);
				insert.add("doc_reference_id", r.doc_reference_id);
				insert.add("ref_document_id", r.ref_document_id);
				sql.append(insert.toString());
			}
			
			sql.append("\n\n -- Filling citeplag_citation \n\n");
			for(int i = 0; i < citeplag_citation.size(); i++)
			{
				Citeplag_citation c = citeplag_citation.get(i);
				Insert insert = new Insert("citeplag_citation");
				insert.add("db_citation_id", c.db_citation_id);
				insert.add("document_id", c.document_id);
				insert.add("doc_reference_id", c.doc_reference_id);
				insert.add("db_reference_id", c.db_reference_id);
				insert.add("count", c.count);
				insert.add("character", c.character);
				insert.add("word", c.word);
				insert.add("sentence", c.sentence);
				insert.add("paragraph", c.paragraph);
				insert.add("section", c.section);
				sql.append(insert.toString());
			}

			sql.append("\n\n -- Filling citeplag_pattern \n\n");
			for(int i = 0; i < citeplag_pattern.size(); i++)
			{
				Citeplag_pattern p = citeplag_pattern.get(i);
				Insert insert = new Insert("citeplag_pattern");
				insert.add("pattern_id", p.pattern_id);
				insert.add("document_id1", p.document_id1);
				insert.add("document_id2", p.document_id2);
				insert.add("procedure", p.procedure);
				insert.add("pattern_score", p.pattern_score);
				sql.append(insert.toString());
			}
			
			sql.append("\n\n -- Filling citeplag_citationpattern_member \n\n");
			for(int i = 0; i < citeplag_citationpattern_member.size(); i++)
			{
				Citeplag_citationpattern_member m = citeplag_citationpattern_member.get(i);
				Insert insert = new Insert("citeplag_citationpattern_member");
				insert.add("pattern_member_id", m.pattern_member_id);
				insert.add("pattern_id", m.pattern_id);
				insert.add("count", m.count);
				insert.add("document_id", m.document_id);
				insert.add("gap", m.gap);
				insert.add("db_citation_id", m.db_citation_id);
				sql.append(insert.toString());
			}
			
			sql.append("\n\n -- Filling citeplag_textpattern_member \n\n");
			for(int i = 0; i < citeplag_textpattern_member.size(); i++)
			{
				Citeplag_textpattern_member t = citeplag_textpattern_member.get(i);
				Insert insert = new Insert("citeplag_textpattern_member");
				insert.add("pattern_member_id", t.pattern_member_id);
				insert.add("pattern_id", t.pattern_id);
				insert.add("document_id", t.document_id);
				insert.add("start_character", t.start_character);
				insert.add("end_character", t.end_character);
				sql.append(insert.toString());
			}
			
			return sql.toString();
		}
		
		public String toString()
		{
			StringBuilder s = new StringBuilder(" -- Insertion-Query-Stats:");
			s.append("\n --   citeplag_document_text: ");
			s.append(citeplag_document_text.size());
			s.append("\n --   citeplag_document_data: ");
			s.append(citeplag_document_data.size());
			s.append("\n --   citeplag_authors: ");
			s.append(citeplag_authors.size());
			s.append("\n --   citeplag_citationpattern_member: ");
			s.append(citeplag_citationpattern_member.size());
			s.append("\n --   citeplag_pattern: ");
			s.append(citeplag_pattern.size());
			s.append("\n --   citeplag_textpattern_member: ");
			s.append(citeplag_textpattern_member.size());
			s.append("\n --   citeplag_reference: ");
			s.append(citeplag_reference.size());
			s.append("\n --   citeplag_citation: ");
			s.append(citeplag_citation.size());
			return s.toString();
		}
		
		/**
		 * Insert Query-Builder
		 * 
		 * @author Moritz Bock
		 */
		class Insert {
			
			// The data to print out in the insert query
			// The String array has to elements:
			//     [0] The column-name
			//     [1] The value
			private ArrayList<String[]> data = new ArrayList<String[]>();
			
			// Table name
			private String table;
			
			/**
			 * Constructor
			 * 
			 * @param table
			 * 			The table to insert the data to.
			 */
			public Insert(String table) { this.table = table; }
			
			/**
			 * Add a value to a column
			 * Nulls will be ignored
			 * 
			 * @param index
			 * 			column-name
			 * @param value
			 * 			value
			 */
			public void add(String index, String value) 
			{ 
				if(value == null) return; 
				String[] s = { index, value.replace("'", "''") }; 
				if(!value.isEmpty()) data.add(s); 
			}
			
			/**
			 * Add a value to a column
			 * Nulls will be ignored
			 * 
			 * @param index
			 * 			column-name
			 * @param value
			 * 			value
			 */
			public void add(String index, Integer value) { 
				if(value != null) 
					this.add(index, value.toString()); 
			}
			
			/**
			 * Add a value to a column
			 * Nulls will be ignored
			 * 
			 * @param index
			 * 			column-name
			 * @param value
			 * 			value
			 */
			public void add(String index, Long value) { 
				if(value != null) 
					this.add(index, value.toString()); 
			}
			
			/**
			 * Return an insert-sql-query from the current object.
			 * @return
			 * 			The sql-query
			 */
			public String getQuery() {
				StringBuilder string = new StringBuilder("INSERT INTO `" + this.table + "` ");
				
				StringBuilder keys = new StringBuilder("(");
				StringBuilder values = new StringBuilder("(");
				
				String prefix = "";
				for(int i = 0; i < data.size(); i++)
				{
					String[] s = data.get(i);
					keys.append(prefix);
					values.append(prefix);
					prefix = ", ";
					keys.append("`" + s[0] + "`");
					values.append("'" + s[1] + "'");
				}
				
				keys.append(")");
				values.append(")");
				
				string.append(keys);
				string.append(" VALUES ");
				string.append(values);
				string.append(";\n");
				return string.toString();
			}
			
			/**
			 * Return an insert-sql-query from the current object.
			 * @return
			 * 			The sql-query
			 */
			public String toString() { return this.getQuery(); }
		}
	}
	

	/**
	 * Classes representing the tables in the database
	 * containing the structure of the tables.
	 */
	
	class Citeplag_document_text { 
		Long document_id; 
		String fulltext;
	}
	
	class Citeplag_document_data { 
		Long document_id; 
		String type;
		String value;
	}
	
	class Citeplag_authors {
		Long author_id;
		Long document_id;
		String lastname;
		String firstname;
	}
	
	class Citeplag_citationpattern_member {
		Long pattern_member_id;
		Long pattern_id;
		Long count;
		Long document_id;
		Integer gap;
		Long db_citation_id;
	}
	
	class Citeplag_pattern {
		Long pattern_id;
		Long document_id1;
		Long document_id2;
		Integer procedure;
		Integer pattern_score;
	}
	
	class Citeplag_textpattern_member {
		Long pattern_member_id;
		Long pattern_id;
		Long document_id;
		Long start_character;
		Long end_character;
	}
	
	class Citeplag_reference {
		Long db_reference_id;
		Long cont_document_id;
		String doc_reference_id;
		Long ref_document_id;
	}
	
	class Citeplag_citation {
		Long db_citation_id;
		Long document_id;
		String doc_reference_id;
		Long db_reference_id;
		Integer count;
		Long character;
		Integer word;
		Integer sentence;
		Integer paragraph;
		String section;
	}
}
