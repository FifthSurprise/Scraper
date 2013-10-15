import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Date;

import javax.net.ssl.SSLHandshakeException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3251576832859316971L;
	private String jobUrl;
	private String name;
	public Date lastChecked;
	
	public Company()
	{
		
	}
	
	public Company(String setJob, String companyName)
	{
		name = companyName;
		jobUrl = setJob;
		lastChecked = new Date();
	}

	public String getName()
	{
		return name;
	}
	public String getJobUrl()
	{
		return jobUrl;
	}
	
	public boolean stillActive()
	{
		try {
			Jsoup.connect(jobUrl).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000).ignoreHttpErrors(true).execute();
		}
		catch(UnknownHostException exception)
		{
			System.out.println ("Unknown Host Exception for " + jobUrl);
			return (true);
		}
		catch (SSLHandshakeException exception2)
		{
			System.out.println ("SSLHandshakeException for " + jobUrl);
			return (true);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println ("Some other failure for " + jobUrl);
			return(true);
		} 
		return (false);
	}
}
