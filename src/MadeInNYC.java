import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MadeInNYC {

	public boolean debug = false;
	private static String html = "http://nytm.org/made-in-nyc/";
	private Elements companiesHiring;
	public ArrayList<Company> companyList;
	private String fileSaveName = "./companies.xml";
		
	public MadeInNYC() {
		//path  = ClassLoader.getResource(fileSaveName);
		companyList = new ArrayList<Company>();
		try {
			if (loadData()) {
				if (debug)outputCompanyList();
			}
			// populate companiesHiring with latest Made in NYC values
			else {
				companiesHiring = parseHiring();
				parseCompanies();
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Try to delete file
	public void deleteData() {
		try {
			File file = new File(fileSaveName);
			if (file.exists()) {
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
				} else {
					System.out.println("Delete operation is failed.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Saves the list of companies to an xml
	public void saveData() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = docBuilder.newDocument();
		org.w3c.dom.Element rootElement = doc.createElement("companylist");
		
		for (int i =0; i<companyList.size();i++)
		{
			rootElement.appendChild((Node)companyList.get(i).exportXMLNode(doc));
		}
		doc.appendChild(rootElement);
		System.out.println (rootElement.getFirstChild());

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileSaveName));
		transformer.transform(source, result);
		System.out.println ("Successfully Saved XML");
	}

	private boolean loadData() throws ParserConfigurationException, SAXException, IOException
	{
		try {
		File fXmlFile = new File(fileSaveName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList list = doc.getElementsByTagName("company");
		companyList = new ArrayList<Company>();
		for (int i =0; i<list.getLength();i++)
		{
			org.w3c.dom.Element elem = (org.w3c.dom.Element) list.item(i);
			companyList.add(new Company(elem,doc));
		}
		return true;
		}
		catch (FileNotFoundException f) {
			System.out.println("Attempt load but could not find file");
			return false;
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
	}
	
	// Check made-in-nyc site and pulls out the body of links
	private Elements parseHiring() {
		Document doc = null;
		try {
			doc = Jsoup.connect(html).timeout(0).get();
		} catch (IOException e) {
			System.out.println("Made In NYC Site is down.");
			e.printStackTrace();
		}
		Element body = doc.body();
		// Find the article tag
		Element article = body.getElementsByTag("article").first();
		return article.children();
	}

	// Takes the list of links of companies in place from website. Then gets
	// only the ones hiring.
	private void parseCompanies() {
		Elements links = companiesHiring.select("li");
		Elements hiringLinks = links.select("li:contains(hiring)");
		companiesHiring = hiringLinks;

		hiringLinks = companiesHiring.select(":contains(hiring)");
		ListIterator<Element> iterator = hiringLinks.listIterator();

		// skip first item (and every other item) because it is a node
		// containing main website and hiring links
		Element check = null;

		if (debug) {
			int count = 0;
			while (iterator.hasNext() && count < 10) {
				count++;
				String jobText = iterator.next().text()
						.replaceAll("\\(hiring\\)", "");
				check = (iterator.hasNext()) ? iterator.next() : check;

				String link = getLink(check);
				if (ScraperHelper.checkLinkStatus(link))
					iterator.remove();
				else {
					check.text(jobText);
					companyList.add(new Company(link, jobText));
				}
			}

				try {
					saveData();
				} catch (ParserConfigurationException | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			return;
		}

		while (iterator.hasNext()) {
			String jobText = iterator.next().text()
					.replaceAll("\\(hiring\\)", "");
			check = (iterator.hasNext()) ? iterator.next() : check;

			String link = getLink(check);
			if (ScraperHelper.checkLinkStatus(link))
				iterator.remove();
			else {
				check.text(jobText);
				companyList.add(new Company(link, jobText));
			}
		}
			try {
				saveData();
			} catch (ParserConfigurationException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Prints out the companylist
	public void outputCompanyList() {
		for (Company c : companyList) {
			System.out.print("<li>");
			System.out.print("<a href=\"" + c.getJobUrl()
					+ "\" target=\"_blank\">" + c.getName() + "</a>");
			System.out.println("</li>");
		}
	}

	// Get the url out of an element
	private String getLink(Element linkElement) {
		try {
			return (linkElement.select("a").first()).attr("abs:href");
		} catch (Exception e) {
			return null;
		}
	}
}
