import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;

import javafx.collections.ObservableList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MadeInNYC {

	public boolean debug = false;

	private static String html = "http://nytm.org/made-in-nyc/";
	public Elements companiesHiring;
	public ArrayList<Company> companyList;
	private ObservableList<ObsCompany> obsCompanylist;
	private String fileSaveName = "src/companies.ser";

	public MadeInNYC() {
		companyList = new ArrayList<Company>();
		if (loadData()) {
			outputCompanyList();
		}
		// populate companiesHiring with latest Made in NYC values
		else {
			companiesHiring = parseHiring();
			parseCompanies();
		}
	}

	// Try to delete file
	public void delete() {
		try {
			File file = new File(fileSaveName);
			if (file.exists()) {
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
				} else {
					System.out.println("Delete operation is failed.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Saves the list of companies to a serialized file
	public void saveData() {
		File output = new File(fileSaveName);
		try {
			boolean bool = output.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(output);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(companyList);
			out.close();
			fileOut.close();
			System.out.println("Saved data in: " + fileSaveName);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	// Attempts to load data from a serialized file. If failure, generates file
	@SuppressWarnings("unchecked")
	private boolean loadData() {
		try {
			FileInputStream fileIn = new FileInputStream(fileSaveName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			companyList = (ArrayList<Company>) in.readObject();
			in.close();
			fileIn.close();
			return true;
		} catch (FileNotFoundException f) {
			System.out.println("Attempt load but could not find file");
			return false;
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		} catch (ClassNotFoundException c) {
			System.out.println("Company class not found");
			c.printStackTrace();
			return false;
		}
	}

	// Check made-in-nyc site and pulls out the body of links
	private Elements parseHiring() {
		Document doc = null;
		try {
			doc = Jsoup.connect(html).timeout(0).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Made In NYC Site is down.");
			e.printStackTrace();
		}
		Element body = doc.body();
		// Find the article tag
		Element article = body.getElementsByTag("article").first();
		return article.children();
	}

	// Takes the list of links of companies in place from website. Then gets
	// only the ones hiring.
	private void parseCompanies() {
		Elements links = companiesHiring.select("li");
		Elements hiringLinks = links.select("li:contains(hiring)");
		companiesHiring = hiringLinks;

		hiringLinks = companiesHiring.select(":contains(hiring)");
		ListIterator<Element> iterator = hiringLinks.listIterator();

		// skip first item (and every other item) because it is a node
		// containing main website and hiring links
		Element check = null;

		if (debug) {
			int count = 0;
			while (iterator.hasNext() && count < 10) {
				count++;
				String jobText = iterator.next().text()
						.replaceAll("\\(hiring\\)", "");
				check = (iterator.hasNext()) ? iterator.next() : check;

				String link = getLink(check);
				if (ScraperHelper.checkLinkStatus(link))
					iterator.remove();
				else {
					check.text(jobText);
					// System.out.println ("Adding " + jobText + " at " + link);
					companyList.add(new Company(link, jobText));
				}
			}
			saveData();
			return;
		}

		while (iterator.hasNext()) {
			String jobText = iterator.next().text()
					.replaceAll("\\(hiring\\)", "");
			check = (iterator.hasNext()) ? iterator.next() : check;

			String link = getLink(check);
			if (ScraperHelper.checkLinkStatus(link))
				iterator.remove();
			else {
				check.text(jobText);
				// System.out.println ("Adding " + jobText + " at " + link);
				companyList.add(new Company(link, jobText));
			}
		}
		saveData();
	}

	// Prints out the companylist
	public void outputCompanyList() {
		for (Company c : companyList) {
			System.out.print("<li>");
			System.out.print("<a href=\"" + c.getJobUrl()
					+ "\" target=\"_blank\">" + c.getName() + "</a>");
			System.out.println("</li>");
		}
	}

	// Get the url out of an element
	private String getLink(Element linkElement) {
		try {
			return (linkElement.select("a").first()).attr("abs:href");
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		MadeInNYC search = new MadeInNYC();
		System.out.println("Completed");
	}
}
