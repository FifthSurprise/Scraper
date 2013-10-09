import java.util.Date;
public class Company {

	public String jobUrl;
	public String name;
	public Date lastChecked;
	private boolean hiring = false;
	
	public Company()
	{
		
	}
	
	public Company(String setJob, String companyName)
	{
		name = companyName;
		jobUrl = setJob;
		hiring = true;
		lastChecked = new Date();
	}

}
