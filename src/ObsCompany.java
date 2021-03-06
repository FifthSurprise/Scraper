import java.util.Date;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObsCompany {
	/**
	 * 
	 */
	private SimpleStringProperty jobUrl;
	private SimpleStringProperty name;
	private SimpleStringProperty notes;
	private SimpleIntegerProperty rank;
	private SimpleBooleanProperty applied;
	private SimpleObjectProperty<Date> lastChecked;

	public ObsCompany(Company c) {
		name = new SimpleStringProperty(c.getName());
		jobUrl = new SimpleStringProperty(c.getJobUrl());
		notes = (new SimpleStringProperty(c.getNotes()));
		lastChecked = new SimpleObjectProperty<Date>(c.getDate());
		rank = new SimpleIntegerProperty(c.getRank());
		applied = new SimpleBooleanProperty(c.isApplied());
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

	//Converts the ObservableCompany into a normal company for serialization
	public Company getCompany() {
		Company c = new Company(jobUrl.get(), name.get());
		c.setNotes(getNotes());
		c.setDate(lastChecked.getValue());
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
	public void setApplied(boolean a) {
		applied.set(a);
	}
	public boolean isApplied() {
		return applied.get();
	}
	
	public Date getDate()
	{
		return lastChecked.getValue();
	}
	public void setDate()
	{
		Date d = new Date();
		lastChecked = new SimpleObjectProperty<Date>(d);
	}
}
