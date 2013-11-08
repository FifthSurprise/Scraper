import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3251576832859316971L;
	private String jobUrl;
	private String name;
	private String notes;
	private int rank;
	private boolean applied;
	public Date lastChecked;
	
	public Company(String setJob, String companyName)
	{
		name = companyName;
		jobUrl = setJob;
		notes = "";
	}

	//Construct from XML node
	public Company (Element company, Document doc)
	{
		name = extractNodeText("name",company);
		jobUrl = extractNodeText("jobUrl",company);
		notes = extractNodeText("notes",company);
		try {
			String dateString = extractNodeText("lastChecked",company);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			lastChecked= (Date)formatter.parse(dateString);
		}
		catch (Exception e)
		{
			//Couldn't parse last date
		}	
		
	}
	private String extractNodeText(String tag, Element c)
	{
		return c.getElementsByTagName(tag).item(0).getTextContent();
	}
	

	public Element exportXMLNode(Document doc) throws ParserConfigurationException
	{
		Element export = doc.createElement("company");		
		export.appendChild(exportTextNode(doc,"name",getName()));	
		export.appendChild(exportTextNode(doc,"notes",getNotes()));
		export.appendChild(exportTextNode(doc,"jobUrl",getJobUrl()));			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try{
			export.appendChild(exportTextNode(doc,"lastChecked",sdf.format(getDate())));
		}
		catch(Exception e)
		{	//Exception due to null date.
			export.appendChild(exportTextNode(doc,"lastChecked",""));
		}
		return export;
	}
	
	private Element exportTextNode(Document doc, String tag, String value)
	{
		Element test = doc.createElement(tag);
		test.appendChild(doc.createTextNode(value));
		return test;
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
		return ScraperHelper.checkLinkStatus(jobUrl);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String n) {
		this.notes = n;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
		if (rank<0)
			this.rank = 0;
		if (rank>5)this.rank = 5;
	}

	public boolean isApplied() {
		return applied;
	}

	public void setApplied(boolean applied) {
		this.applied = applied;
	}
	public Date getDate()
	{
		return lastChecked;
	}
	
	public void setDate(Date d)
	{
		lastChecked = d;
	}
}
