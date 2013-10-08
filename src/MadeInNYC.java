import java.io.IOException;
import java.net.UnknownHostException;
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
	
	public MadeInNYC()
	{
		companiesHiring = parseHiring();
	}
	
	private Elements parseHiring()
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(html).timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println ("failure");
			e.printStackTrace();
		}
		Element body = doc.body();
		
		//Find the article tag
		Element article = body.getElementsByTag("article").first();
		
		return article.children();
		
		//return article.getElementsContainingText("hiring"); 
	}
	
	private void parseCompanies()
	{
		Elements links = companiesHiring.select("li");
		//Elements hiringLinks = companiesHiring.first().getElementsContainingOwnText("(hiring)");
		Elements hiringLinks = links.select("li:contains(hiring)");
		//System.out.println (hiringLinks);
		//System.out.println (companiesHiring.first().children().get(3));
		
		companiesHiring = hiringLinks;
	}

	private void strip404()
	{
		Elements result = new Elements();
		Elements hiringLinks = companiesHiring.select(":contains(hiring)");
		ListIterator<Element> iterator = hiringLinks.listIterator();
		
		//skip first item (and every other item) because it is a node containing main website and hiring links
		Element check = null;
		
		//while (iterator.hasNext()){
			String jobText= iterator.next().text().replaceAll("[(hiring)]","");
			System.out.println("jobtext = " + jobText);
			check = (iterator.hasNext())?iterator.next():check;
			
			String link = getLink(check);
			System.out.println ("Checking: " + link);
			if (checkLinkStatus(link))
				iterator.remove();
			
			check.text(jobText);
			result.add(check);
			
			//check = (iterator.hasNext())?iterator.next():check;
		//}

		ListIterator<Element> resultIterator = result.listIterator();
	
		//Output results of the application to log
		while (resultIterator.hasNext()){
			Element output =resultIterator.next();
			System.out.print("<li>");
			System.out.print ("<a href=\""+getLink(output)+"\" target=\"_blank\">" + 
			output.select("a").text()+ "</a>");
			System.out.println ("</li>");
		}
		
	}
	
	private String getLink(Element linkElement)
	{
		return (linkElement.select("a").first()).attr("abs:href");
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
	
	private Company parseCompany(){
		return new Company();
	}
	
	
	public static void main(String[] args) {
		MadeInNYC search = new MadeInNYC();
		search.parseCompanies();
		search.strip404();
		//System.out.println ("Link is 404 " + search.check404("http://www.admeld.com/about/jobs/"));
	}
}
