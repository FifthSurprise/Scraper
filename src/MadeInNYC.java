import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Pattern;




import javax.net.ssl.SSLHandshakeException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MadeInNYC {
	
	private static String html = "http://nytm.org/made-in-nyc/";
	public Elements companiesHiring;
	private ArrayList <Company> companyList;

	public MadeInNYC()
	{
		companyList = new ArrayList<Company>();
		companiesHiring = parseHiring();
	}
	
	//Check made-in-nyc site and pulls out the body of links
	private Elements parseHiring()
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(html).timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println ("Made In NYC Site is down.");
			e.printStackTrace();
		}
		Element body = doc.body();
		//Find the article tag
		Element article = body.getElementsByTag("article").first();
		return article.children();	 
	}
	
	//Takes the list of links of companies in place from website.  Then gets only the ones hiring.
	private void parseCompanies()
	{
		Elements links = companiesHiring.select("li");
		Elements hiringLinks = links.select("li:contains(hiring)");	
		companiesHiring = hiringLinks;

		hiringLinks = companiesHiring.select(":contains(hiring)");
		ListIterator<Element> iterator = hiringLinks.listIterator();
		
		//skip first item (and every other item) because it is a node containing main website and hiring links
		Element check = null;
		
		while (iterator.hasNext()){
			String jobText= iterator.next().text().replaceAll("[(hiring)]","");
			check = (iterator.hasNext())?iterator.next():check;
			
			String link = getLink(check);
			if (checkLinkStatus(link))
				iterator.remove();
			else
			{
				check.text(jobText);
				companyList.add(new Company (link,jobText));
			}
		}
		outputCompanyList();
	}
	
	//Prints out the companylist
	private void outputCompanyList()
	{
		for (Company c: companyList)
		{
			System.out.print("<li>");
			System.out.print ("<a href=\""+c.jobUrl+"\" target=\"_blank\">" + 
			c.name+ "</a>");
			System.out.println ("</li>");
		}
	}
	
	//Get the url out of an element
	private String getLink(Element linkElement)
	{
		try{
		return (linkElement.select("a").first()).attr("abs:href");
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	//Check if a URL returns a 404
	private boolean checkLinkStatus(String link)
	{
		Connection.Response response = null;
		try {
			response = Jsoup.connect(link).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000).ignoreHttpErrors(true).execute();
		}
		catch(UnknownHostException exception)
		{
			System.out.println ("Unknown Host Exception for " + link);
			return (true);
		}
		catch (SSLHandshakeException exception2)
		{
			System.out.println ("SSLHandshakeException for " + link);
			return (true);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println ("Some other failure for " + link);
			return(true);
		} 
		return (false);
	}
		
	public static void main(String[] args) {
		MadeInNYC search = new MadeInNYC();
		search.parseCompanies();	
	}
}
