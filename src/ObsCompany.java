import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObsCompany {
	/**
	 * 
	 */
	private SimpleStringProperty jobUrl;
	private SimpleStringProperty name;
	private SimpleStringProperty notes;
	private SimpleIntegerProperty rank;

	public Date lastChecked;

	public ObsCompany(Company c) {
		name = new SimpleStringProperty(c.getName());
		jobUrl = new SimpleStringProperty(c.getJobUrl());
		notes = (new SimpleStringProperty(c.getNotes()));
		lastChecked = new Date();
		rank = new SimpleIntegerProperty(c.getRank());
	}

	public String getName() {
		return name.get();
	}
	public void setName(String str) {
		name.set(str);
	}
	public String getJobUrl() {
		return jobUrl.get();
	}
	public void setJobUrl(String str) {
		jobUrl.set(str);
	}

	public Company getCompany() {
		Company c = new Company(jobUrl.get(), name.get());
		c.setNotes(notes.get());
		return c;
	}

	public boolean stillActive() {
		return ScraperHelper.checkLinkStatus(name.get());
	}

	public String getNotes() {
		return notes.get();
	}

	public void setNotes(String str) {
		notes.set(str);
	}

	public int getRank() {
		return rank.get();
	}

	public void setRank(int r) {
		rank.set(r);
	}
}
