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
	private boolean applied;
	public Date lastChecked;
	
	public Company(String setJob, String companyName)
	{
		name = companyName;
		jobUrl = setJob;
		
		notes = "";
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

	/**
	 * @return the applied
	 */
	public boolean isApplied() {
		return applied;
	}

	/**
	 * @param applied the applied to set
	 */
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
