import java.io.Serializable;
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
	public Date lastChecked;
	
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
		return ScraperHelper.checkLinkStatus(jobUrl);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
}
