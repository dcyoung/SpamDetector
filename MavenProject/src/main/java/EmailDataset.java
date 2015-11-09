import java.util.ArrayList;
import java.util.HashMap;

public class EmailDataset {

	private ArrayList<Email> allEmails;
	private ArrayList<Email> spamEmails;
	private ArrayList<Email> validEmails;
	private int numEmails, numSpamEmails, numValidEmails;
	
	private HashMap<String, Integer> allEmailWordFreq;
	private HashMap<String, Integer> spamEmailWordFreq;
	private HashMap<String, Integer> validEmailWordFreq;
	private int totalWordCount, totalSpamWordCount, totalValidWordCount;
	
	
	public EmailDataset(ArrayList<Email> allEmails){
		this.allEmails = allEmails;
		this.populateSeparatedEmailLists();
		this.populateWordFeqMaps();
	}
	
	
	private void populateSeparatedEmailLists() {
		this.spamEmails = new ArrayList<Email>();
		this.validEmails = new ArrayList<Email>();
		this.numEmails = this.numSpamEmails = this.numValidEmails = 0;
		for(Email email : this.allEmails){
			if(email.isSpam()){
				this.spamEmails.add(email);
				this.numSpamEmails++;
			}
			else{
				this.validEmails.add(email);
				this.numValidEmails++;
			}
			this.numEmails++;
		}
	}


	private void populateWordFeqMaps() {
		
		this.totalSpamWordCount = this.totalValidWordCount = 0;
		this.allEmailWordFreq = new HashMap<String, Integer>();
		this.spamEmailWordFreq = new HashMap<String, Integer>();
		this.validEmailWordFreq = new HashMap<String, Integer>();
		
		for(Email email : this.spamEmails){
			for(String key : email.getWordCountMap().keySet()){
				int prevCount = this.spamEmailWordFreq.containsKey(key) ? this.spamEmailWordFreq.get(key) : 0;
				int additionalCount = email.getWordCountMap().get(key);
				this.spamEmailWordFreq.put(key, prevCount + additionalCount);
				this.totalSpamWordCount += additionalCount;
			}
		}
		for(Email email : this.validEmails){
			for(String key : email.getWordCountMap().keySet()){
				int prevCount = this.validEmailWordFreq.containsKey(key) ? this.validEmailWordFreq.get(key) : 0;
				int additionalCount = email.getWordCountMap().get(key);
				this.validEmailWordFreq.put(key, prevCount + additionalCount);
				this.totalValidWordCount += additionalCount;
			}
		}
		
		for(String key : this.spamEmailWordFreq.keySet()){
			int prevCount = this.allEmailWordFreq.containsKey(key) ? this.allEmailWordFreq.get(key) : 0;
			int additionalCount = this.spamEmailWordFreq.get(key);
			this.allEmailWordFreq.put(key, prevCount + additionalCount);
		}
		for(String key : this.validEmailWordFreq.keySet()){
			int prevCount = this.allEmailWordFreq.containsKey(key) ? this.allEmailWordFreq.get(key) : 0;
			int additionalCount = this.validEmailWordFreq.get(key);
			this.allEmailWordFreq.put(key, prevCount + additionalCount);
		}
		
		this.totalWordCount = this.totalSpamWordCount+ this.totalValidWordCount;
	}

	

	
	public ArrayList<Email> getAllEmails() {
		return allEmails;
	}

	public ArrayList<Email> getSpamEmails() {
		return spamEmails;
	}

	public ArrayList<Email> getValidEmails() {
		return validEmails;
	}

	public int getNumEmails() {
		return numEmails;
	}

	public int getNumSpamEmails() {
		return numSpamEmails;
	}

	public int getNumValidEmails() {
		return numValidEmails;
	}

	public HashMap<String, Integer> getAllEmailWordFreq() {
		return allEmailWordFreq;
	}

	public HashMap<String, Integer> getSpamEmailWordFreq() {
		return spamEmailWordFreq;
	}

	public HashMap<String, Integer> getValidEmailWordFreq() {
		return validEmailWordFreq;
	}

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public int getTotalSpamWordCount() {
		return totalSpamWordCount;
	}

	public int getTotalValidWordCount() {
		return totalValidWordCount;
	}


	public static void main(String[] args) {
		FileReader fr = new FileReader();
		String emailTrainingDataFilename = "spam_detection/train_email.txt";
		ArrayList<Email> allEmails = fr.readEmailData(emailTrainingDataFilename);
		
		EmailDataset mrd = new EmailDataset(allEmails);
		System.out.println(mrd.getNumValidEmails() + " Valid Emails");
		System.out.println(mrd.getNumSpamEmails() + " Spam Emails");
		System.out.println(mrd.getNumEmails() + " Total Emails");
		System.out.println(mrd.getTotalSpamWordCount() + " Spam Word Instances");
		System.out.println(mrd.getTotalValidWordCount() + " Valid Word Instances");
		System.out.println(mrd.getTotalWordCount() + " Total Word Instances");
	}

}
