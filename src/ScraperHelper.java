import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.Jsoup;

import javafx.collections.*;

public class ScraperHelper {
	//Check a link if it is active
	public static boolean checkLinkStatus(String link)
	{
		try {
			Jsoup.connect(link).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000).ignoreHttpErrors(true).execute();
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
	
	//Takes an arraylist of Companies and returns it as an observable list
	public static ObservableList<ObsCompany> getObsList(ArrayList<Company> cList)
	{
		List<ObsCompany> oList = new ArrayList<ObsCompany>();
		
		for (Company c: cList)
		{
			oList.add(new ObsCompany(c));
		}
		return FXCollections.observableList(oList);
	}
	
	public static ArrayList<Company> getCompanyList(ObservableList<ObsCompany> oList)
	{
		ArrayList<Company> cList = new ArrayList<Company>();
		
		for (ObsCompany o: oList)
		{
			cList.add(o.getCompany());
		}
		return cList;
	}
}
