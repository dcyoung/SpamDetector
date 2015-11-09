import java.util.ArrayList;
import java.util.HashMap;

public class EmailDataset {

	private ArrayList<Email> allEmails;
	private ArrayList<Email> spamEmails;
	private ArrayList<Email> validEmails;
	private int numEmails, numSpamEmails, numValidEmails;
	
	private HashMap<String, Integer> allEmailWordCounts;
	private HashMap<String, Integer> spamEmailWordCounts;
	private HashMap<String, Integer> validEmailWordCounts;
	private int totalWordCount, totalSpamWordCount, totalValidWordCount;
	
	
	private int k = 1;
	private HashMap<String, Double> spamLikelihoods;
	private HashMap<String, Double> validLikelihoods;
	private HashMap<String, Double> combinedLikelihoods;
	
	
	
	
	public EmailDataset(ArrayList<Email> allEmails){
		this.allEmails = allEmails;
		this.populateSeparatedEmailLists();
		this.populateWordFeqMaps();
		this.calculateLikelihoods();
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
		this.allEmailWordCounts = new HashMap<String, Integer>();
		this.spamEmailWordCounts = new HashMap<String, Integer>();
		this.validEmailWordCounts = new HashMap<String, Integer>();
		
		for(Email email : this.spamEmails){
			for(String key : email.getWordCountMap().keySet()){
				int prevCount = this.spamEmailWordCounts.containsKey(key) ? this.spamEmailWordCounts.get(key) : 0;
				int additionalCount = email.getWordCountMap().get(key);
				this.spamEmailWordCounts.put(key, prevCount + additionalCount);
				this.totalSpamWordCount += additionalCount;
			}
		}
		for(Email email : this.validEmails){
			for(String key : email.getWordCountMap().keySet()){
				int prevCount = this.validEmailWordCounts.containsKey(key) ? this.validEmailWordCounts.get(key) : 0;
				int additionalCount = email.getWordCountMap().get(key);
				this.validEmailWordCounts.put(key, prevCount + additionalCount);
				this.totalValidWordCount += additionalCount;
			}
		}
		
		for(String key : this.spamEmailWordCounts.keySet()){
			int prevCount = this.allEmailWordCounts.containsKey(key) ? this.allEmailWordCounts.get(key) : 0;
			int additionalCount = this.spamEmailWordCounts.get(key);
			this.allEmailWordCounts.put(key, prevCount + additionalCount);
		}
		for(String key : this.validEmailWordCounts.keySet()){
			int prevCount = this.allEmailWordCounts.containsKey(key) ? this.allEmailWordCounts.get(key) : 0;
			int additionalCount = this.validEmailWordCounts.get(key);
			this.allEmailWordCounts.put(key, prevCount + additionalCount);
		}
		
		this.totalWordCount = this.totalSpamWordCount+ this.totalValidWordCount;
	}

	
	private void calculateLikelihoods() {
		int V;
		
		this.spamLikelihoods = new HashMap<String, Double>();
		this.validLikelihoods = new HashMap<String, Double>();
		this.combinedLikelihoods = new HashMap<String, Double>();
		double tempLikelihood;
		
		V = this.spamEmailWordCounts.size();
		for(String key : this.spamEmailWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.spamEmailWordCounts.get(key))/(this.k*V+this.totalSpamWordCount);
			this.spamLikelihoods.put(key, tempLikelihood);
		}
		
		V = this.validEmailWordCounts.size();
		for(String key : this.validEmailWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.validEmailWordCounts.get(key))/(this.k*V+this.totalValidWordCount);
			this.validLikelihoods.put(key, tempLikelihood);
		}
		
		V = this.allEmailWordCounts.size();
		for(String key : this.allEmailWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.allEmailWordCounts.get(key))/(this.k*V+this.totalWordCount);
			this.combinedLikelihoods.put(key, tempLikelihood);
		}
	}

	public double getSpecificLikelihood(String word, boolean bAssumeSpam){
		double likelihood;
		double V;
		if(bAssumeSpam){
			if(this.spamLikelihoods.containsKey(word)){
				likelihood = this.spamLikelihoods.get(word);
			}
			else{
				V = this.spamEmailWordCounts.size();
				likelihood = 1.0*(this.k+0)/(this.k*V+this.totalSpamWordCount);
			}
		}
		else{
			if(this.validLikelihoods.containsKey(word)){
				likelihood = this.validLikelihoods.get(word);
			}
			else{
				V = this.validEmailWordCounts.size();
				likelihood = 1.0*(this.k+0)/(this.k*V+this.totalValidWordCount);
			}
		}
		return likelihood;
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

	public HashMap<String, Integer> getAllEmailWordCounts() {
		return allEmailWordCounts;
	}

	public HashMap<String, Integer> getSpamEmailWordCounts() {
		return spamEmailWordCounts;
	}

	public HashMap<String, Integer> getValidEmailWordCounts() {
		return validEmailWordCounts;
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

	//priors P(class)
	public double getPriorProbabilitySpam(){
		return 1.0*this.numSpamEmails/this.numEmails;
	}
	public double getPriorProbabilityValid(){
		return 1.0*this.numValidEmails/this.numEmails;
	}
	
	//Conditional Probabilities P(word | class)
	public HashMap<String, Double> getSpamLikelihoods() {
		return spamLikelihoods;
	}

	public HashMap<String, Double> getValidLikelihoods() {
		return validLikelihoods;
	}

	public HashMap<String, Double> getCombinedLikelihoods() {
		return combinedLikelihoods;
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
		
		double sum = 0;
		for(String key : mrd.getSpamLikelihoods().keySet()){
			sum += mrd.getSpamLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for spam words");
		
		sum = 0;
		for(String key : mrd.getValidLikelihoods().keySet()){
			sum += mrd.getValidLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for valid words");
		
		sum = 0;
		for(String key : mrd.getCombinedLikelihoods().keySet()){
			sum += mrd.getCombinedLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for all words");
		
	}

}
