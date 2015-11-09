import java.util.ArrayList;
import java.util.HashMap;

public class MovieReviewDataset {

	private ArrayList<MovieReview> allReviews;
	private ArrayList<MovieReview> positiveReviews;
	private ArrayList<MovieReview> negativeReviews;
	private int numReviews, numPosReviews, numNegReviews;
	
	private HashMap<String, Integer> allRevWordFreq;
	private HashMap<String, Integer> posRevWordFreq;
	private HashMap<String, Integer> negRevWordFreq;
	private int totalWordCount, totalPosWordCount, totalNegWordCount;
	
	
	public MovieReviewDataset(ArrayList<MovieReview> allReviews){
		this.allReviews = allReviews;
		this.populateSeparatedReviewLists();
		this.populateWordFeqMaps();
	}
	
	
	private void populateSeparatedReviewLists() {
		this.positiveReviews = new ArrayList<MovieReview>();
		this.negativeReviews = new ArrayList<MovieReview>();
		this.numReviews = this.numPosReviews = this.numNegReviews = 0;
		for(MovieReview rev : this.allReviews){
			if(rev.isPositive()){
				this.positiveReviews.add(rev);
				this.numPosReviews++;
			}
			else{
				this.negativeReviews.add(rev);
				this.numNegReviews++;
			}
			this.numReviews++;
		}
	}


	private void populateWordFeqMaps() {
		
		this.totalPosWordCount = this.totalNegWordCount = 0;
		this.allRevWordFreq = new HashMap<String, Integer>();
		this.posRevWordFreq = new HashMap<String, Integer>();
		this.negRevWordFreq = new HashMap<String, Integer>();
		
		for(MovieReview rev : this.positiveReviews){
			for(String key : rev.getWordCountMap().keySet()){
				int prevCount = this.posRevWordFreq.containsKey(key) ? this.posRevWordFreq.get(key) : 0;
				int additionalCount = rev.getWordCountMap().get(key);
				this.posRevWordFreq.put(key, prevCount + additionalCount);
				this.totalPosWordCount += additionalCount;
			}
		}
		for(MovieReview rev : this.negativeReviews){
			for(String key : rev.getWordCountMap().keySet()){
				int prevCount = this.negRevWordFreq.containsKey(key) ? this.negRevWordFreq.get(key) : 0;
				int additionalCount = rev.getWordCountMap().get(key);
				this.negRevWordFreq.put(key, prevCount + additionalCount);
				this.totalNegWordCount += additionalCount;
			}
		}
		
		for(String key : this.posRevWordFreq.keySet()){
			int prevCount = this.allRevWordFreq.containsKey(key) ? this.allRevWordFreq.get(key) : 0;
			int additionalCount = this.posRevWordFreq.get(key);
			this.allRevWordFreq.put(key, prevCount + additionalCount);
		}
		for(String key : this.negRevWordFreq.keySet()){
			int prevCount = this.allRevWordFreq.containsKey(key) ? this.allRevWordFreq.get(key) : 0;
			int additionalCount = this.negRevWordFreq.get(key);
			this.allRevWordFreq.put(key, prevCount + additionalCount);
		}
		
		
		
		
		
		this.totalWordCount = this.totalPosWordCount+ this.totalNegWordCount;
	}



	public ArrayList<MovieReview> getAllReviews() {
		return this.allReviews;
	}

	public ArrayList<MovieReview> getPositiveReviews() {
		return this.positiveReviews;
	}

	public ArrayList<MovieReview> getNegativeReviews() {
		return this.negativeReviews;
	}

	public int getNumReviews() {
		return this.numReviews;
	}

	public int getNumPosReviews() {
		return this.numPosReviews;
	}

	public int getNumNegReviews() {
		return this.numNegReviews;
	}
	
	public HashMap<String, Integer> getAllRevWordFreq() {
		return allRevWordFreq;
	}

	public HashMap<String, Integer> getPosRevWordFreq() {
		return posRevWordFreq;
	}

	public HashMap<String, Integer> getNegRevWordFreq() {
		return negRevWordFreq;
	}


	public int getTotalWordCount() {
		return totalWordCount;
	}

	public int getTotalPosWordCount() {
		return totalPosWordCount;
	}

	public int getTotalNegWordCount() {
		return totalNegWordCount;
	}

	
	
	
	public static void main(String[] args) {
		FileReader fr = new FileReader();
		String movieReviewFilename = "movie_reviews/rt-test.txt";
		ArrayList<MovieReview> allReviews = fr.readMovieReviewData(movieReviewFilename);
		
		MovieReviewDataset mrd = new MovieReviewDataset(allReviews);
		System.out.println(mrd.getNumNegReviews() + " Negative Reviews");
		System.out.println(mrd.getNumPosReviews() + " Positive Reviews");
		System.out.println(mrd.getNumReviews() + " Total Reviews");
		System.out.println(mrd.getTotalPosWordCount() + " Positive Word Instances");
		System.out.println(mrd.getTotalNegWordCount() + " Negative Word Instances");
		System.out.println(mrd.getTotalWordCount() + " Total Word Instances");
	}

}
