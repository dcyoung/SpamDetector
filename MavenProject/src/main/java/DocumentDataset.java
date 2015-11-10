import java.util.ArrayList;
import java.util.HashMap;

/**
 * Organizes the training data composed of text documents.
 * Also calculates probability info necessary for flag detection.
 * @author dcyoung
 *
 */
public class DocumentDataset {

	private ArrayList<TextDocument> allDocs;
	//documents known to be flagged (ie: spam email or positive movie review)
	private ArrayList<TextDocument> flaggedDocs;
	private ArrayList<TextDocument> unflaggedDocs;
	private int numDocs, numFlaggedDocs, numUnflaggedDocs;
	
	private HashMap<String, Integer> allDocWordCounts;
	private HashMap<String, Integer> flaggedDocWordCounts;
	private HashMap<String, Integer> unflaggedDocWordCounts;
	private int totalWordCount, totalFlaggedWordCount, totalUnflaggedWordCount;
	
	
	private int k = 1;
	private HashMap<String, Double> flagLikelihoods;
	private HashMap<String, Double> unflaggedLikelihoods;
	private HashMap<String, Double> combinedLikelihoods;
	
	
	
	/**
	 * 
	 * @param allDocs
	 */
	public DocumentDataset(ArrayList<TextDocument> allDocs){
		this.allDocs = allDocs;
		this.populateSeparatedDocumentLists();
		this.populateWordFeqMaps();
		this.calculateLikelihoods();
	}
	

	private void populateSeparatedDocumentLists() {
		this.flaggedDocs = new ArrayList<TextDocument>();
		this.unflaggedDocs = new ArrayList<TextDocument>();
		this.numDocs = this.numFlaggedDocs = this.numUnflaggedDocs = 0;
		for(TextDocument email : this.allDocs){
			if(email.isFlagged()){
				this.flaggedDocs.add(email);
				this.numFlaggedDocs++;
			}
			else{
				this.unflaggedDocs.add(email);
				this.numUnflaggedDocs++;
			}
			this.numDocs++;
		}
	}


	private void populateWordFeqMaps() {
		
		this.totalFlaggedWordCount = this.totalUnflaggedWordCount = 0;
		this.allDocWordCounts = new HashMap<String, Integer>();
		this.flaggedDocWordCounts = new HashMap<String, Integer>();
		this.unflaggedDocWordCounts = new HashMap<String, Integer>();
		
		for(TextDocument document : this.flaggedDocs){
			for(String key : document.getWordCountMap().keySet()){
				int prevCount = this.flaggedDocWordCounts.containsKey(key) ? this.flaggedDocWordCounts.get(key) : 0;
				int additionalCount = document.getWordCountMap().get(key);
				this.flaggedDocWordCounts.put(key, prevCount + additionalCount);
				this.totalFlaggedWordCount += additionalCount;
			}
		}
		for(TextDocument document : this.unflaggedDocs){
			for(String key : document.getWordCountMap().keySet()){
				int prevCount = this.unflaggedDocWordCounts.containsKey(key) ? this.unflaggedDocWordCounts.get(key) : 0;
				int additionalCount = document.getWordCountMap().get(key);
				this.unflaggedDocWordCounts.put(key, prevCount + additionalCount);
				this.totalUnflaggedWordCount += additionalCount;
			}
		}
		
		for(String key : this.flaggedDocWordCounts.keySet()){
			int prevCount = this.allDocWordCounts.containsKey(key) ? this.allDocWordCounts.get(key) : 0;
			int additionalCount = this.flaggedDocWordCounts.get(key);
			this.allDocWordCounts.put(key, prevCount + additionalCount);
		}
		for(String key : this.unflaggedDocWordCounts.keySet()){
			int prevCount = this.allDocWordCounts.containsKey(key) ? this.allDocWordCounts.get(key) : 0;
			int additionalCount = this.unflaggedDocWordCounts.get(key);
			this.allDocWordCounts.put(key, prevCount + additionalCount);
		}
		
		this.totalWordCount = this.totalFlaggedWordCount+ this.totalUnflaggedWordCount;
	}

	
	private void calculateLikelihoods() {
		int V;
		
		this.flagLikelihoods = new HashMap<String, Double>();
		this.unflaggedLikelihoods = new HashMap<String, Double>();
		this.combinedLikelihoods = new HashMap<String, Double>();
		double tempLikelihood;
		
		V = this.flaggedDocWordCounts.size();
		for(String key : this.flaggedDocWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.flaggedDocWordCounts.get(key))/(this.k*V+this.totalFlaggedWordCount);
			this.flagLikelihoods.put(key, tempLikelihood);
		}
		
		V = this.unflaggedDocWordCounts.size();
		for(String key : this.unflaggedDocWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.unflaggedDocWordCounts.get(key))/(this.k*V+this.totalUnflaggedWordCount);
			this.unflaggedLikelihoods.put(key, tempLikelihood);
		}
		
		V = this.allDocWordCounts.size();
		for(String key : this.allDocWordCounts.keySet()){
			tempLikelihood = 1.0*(this.k+this.allDocWordCounts.get(key))/(this.k*V+this.totalWordCount);
			this.combinedLikelihoods.put(key, tempLikelihood);
		}
	}

	public double getSpecificLikelihood(String word, boolean bAssumeFlag){
		double likelihood;
		double V;
		if(bAssumeFlag){
			if(this.flagLikelihoods.containsKey(word)){
				likelihood = this.flagLikelihoods.get(word);
			}
			else{
				V = this.flaggedDocWordCounts.size();
				likelihood = 1.0*(this.k+0)/(this.k*V+this.totalFlaggedWordCount);
			}
		}
		else{
			if(this.unflaggedLikelihoods.containsKey(word)){
				likelihood = this.unflaggedLikelihoods.get(word);
			}
			else{
				V = this.unflaggedDocWordCounts.size();
				likelihood = 1.0*(this.k+0)/(this.k*V+this.totalUnflaggedWordCount);
			}
		}
		return likelihood;
	}
	
	
	public ArrayList<TextDocument> getAllDocs() {
		return allDocs;
	}

	public ArrayList<TextDocument> getFlaggedDocs() {
		return flaggedDocs;
	}

	public ArrayList<TextDocument> getUnflaggedDocs() {
		return unflaggedDocs;
	}

	public int getNumDocs() {
		return numDocs;
	}

	public int getNumFlaggedDocs() {
		return numFlaggedDocs;
	}

	public int getNumUnflaggedDocs() {
		return numUnflaggedDocs;
	}

	public HashMap<String, Integer> getAllDocWordCounts() {
		return allDocWordCounts;
	}

	public HashMap<String, Integer> getFlaggedDocWordCounts() {
		return flaggedDocWordCounts;
	}

	public HashMap<String, Integer> getUnflaggedDocWordCounts() {
		return unflaggedDocWordCounts;
	}

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public int getTotalFlaggedWordCount() {
		return totalFlaggedWordCount;
	}

	public int getTotalUnflaggedWordCount() {
		return totalUnflaggedWordCount;
	}

	//priors P(class)
	public double getPriorProbabilityFlagged(){
		return 1.0*this.numFlaggedDocs/this.numDocs;
	}
	public double getPriorProbabilityUnflagged(){
		return 1.0*this.numUnflaggedDocs/this.numDocs;
	}
	
	//Conditional Probabilities P(word | class)
	public HashMap<String, Double> getFlaggedLikelihoods() {
		return flagLikelihoods;
	}

	public HashMap<String, Double> getUnflaggedLikelihoods() {
		return unflaggedLikelihoods;
	}

	public HashMap<String, Double> getCombinedLikelihoods() {
		return combinedLikelihoods;
	}

	
	
	
	public static void main(String[] args) {
		System.out.println("Emails:");
		FileReader fr = new FileReader();
		String emailTrainingDataFilename = "spam_detection/train_email.txt";
		ArrayList<TextDocument> allEmails = fr.readDocumentData(emailTrainingDataFilename);
		
		DocumentDataset mrd = new DocumentDataset(allEmails);
		System.out.println(mrd.getNumUnflaggedDocs() + " Valid Emails");
		System.out.println(mrd.getNumFlaggedDocs() + " Spam Emails");
		System.out.println(mrd.getNumDocs() + " Total Emails");
		System.out.println(mrd.getTotalFlaggedWordCount() + " Spam Word Instances");
		System.out.println(mrd.getTotalUnflaggedWordCount() + " Valid Word Instances");
		System.out.println(mrd.getTotalWordCount() + " Total Word Instances");
		
		double sum = 0;
		for(String key : mrd.getFlaggedLikelihoods().keySet()){
			sum += mrd.getFlaggedLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for spam words");
		
		sum = 0;
		for(String key : mrd.getUnflaggedLikelihoods().keySet()){
			sum += mrd.getUnflaggedLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for valid words");
		
		sum = 0;
		for(String key : mrd.getCombinedLikelihoods().keySet()){
			sum += mrd.getCombinedLikelihoods().get(key);
		}
		System.out.println(sum + " sum of likelihoods for all words");
		
		
		
		
		System.out.println("Movie Reviews:");
		String movieReviewFilename = "movie_reviews/rt-test.txt";
		ArrayList<TextDocument> allReviews = fr.readDocumentData(movieReviewFilename);
		
		DocumentDataset dds= new DocumentDataset(allReviews);
		System.out.println(dds.getNumUnflaggedDocs() + " Negative Reviews");
		System.out.println(dds.getNumFlaggedDocs() + " Positive Reviews");
		System.out.println(dds.getNumDocs() + " Total Reviews");
		System.out.println(dds.getTotalFlaggedWordCount() + " Positive Word Instances");
		System.out.println(dds.getTotalUnflaggedWordCount() + " Negative Word Instances");
		System.out.println(dds.getTotalWordCount() + " Total Word Instances");
		
	}

}
