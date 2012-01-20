package core.text;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Parses XML documents, largely using the java supplied XML libraries.  This is 
 * very simple at this point, not even setting up a Validator.  Therefore, it is
 * very likely that this will be extended in the future including possibly more
 * sophisticated XML retrieval schemes, etc.
 * 
 * @author ksmall
 */
public class DOM_Parser {

	/** the parser */
	private DocumentBuilder parser;
	
	// TODO -- DTD done correct; currently using total entity hack
	/**
	 * Constructor
	 */
	public DOM_Parser() {
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// if we want a validating parser - I think necessary for some entity symbols (e.g. &amp;)
	// TODO -- http://www.roseindia.net/xml/dom/DOMValidateDTD.shtml
	
	/**
	 * Parses the desired file.
	 * 
	 * @param file	the name of the file to be parsed
	 * @return		the resulting java Document structure
	 */
	public Document parse(File file) {
		Document result = null;
		try {
			result = parser.parse(file);
		}	
		catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}			

	/**
	 * Parses the file represented by the input String
	 * 
	 * @param fileName	the input file String
	 * @return			the resulting java Document structure
	 */
	/*
	public Document parse(String fileName) {
		return parse(new File(fileName));
	}
	*/
	
	public Document parse(Reader reader) {
		Document result = null;
		try {
			result = parser.parse(new InputSource(reader));
		}	
		catch (Exception e) {
			e.printStackTrace();
		}	
		return result;		
	}
	
	//public Document parse(StringBuffer b, boolean ignoreEntities) {
	public Document parse(String record) {
		//String temp = b.toString();
		//temp = temp.replace("&", "[AMP]");
		//temp = temp.replace(";", "]]");
		//return parse(new StringReader(temp));
		//if (ignoreEntities)
		//	return parse(new StringReader(b.toString().replace("&", "[[AMP]]")));
		//else
		//	return parse(new StringReader(b.toString()));
		return parse(new StringReader(record));
	}
	
	public static String[] getNodeValues(Element element, String name) {
		NodeList list = element.getElementsByTagName(name);
		if (list == null)
			return null;
		String[] result = new String[list.getLength()];
		for (int i = 0; i < result.length; i++)
			result[i] = list.item(i).getFirstChild().getNodeValue().trim();
		return result;
	}
	
	// a little faster
	public static String getNodeValue(Element element, String name, int index) {
		NodeList list = element.getElementsByTagName(name);
		//if (list == null)
		if (list.getLength() == 0)
			return null;
		Node node = list.item(index).getFirstChild();
		if (node == null)
			return null;
		return node.getNodeValue().trim();
	}
	
	public static String getNodeValue(Node element, String name) {
		return getNodeValue((Element) element, name, 0);
	}
	
	public static String getNodeValue(Element element, String name) {
		return getNodeValue(element, name, 0);
	}
	
	public static Element getElement(Element element, String name, int index) {
		return (Element) element.getElementsByTagName(name).item(index);
	}
	
	public static Element getElement(Element element, String name) {
		return getElement(element, name, 0);
	}
}
