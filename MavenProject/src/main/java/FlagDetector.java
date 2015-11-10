import java.util.ArrayList;

/**
 * Detects whether or not a document is flagged based off the training data
 * Flagged can mean spam for emails, or positive reviews for movie reviews
 * @author dcyoung
 * 
 */
public class FlagDetector {

	DocumentDataset trainedData;
	
	/**
	 * Constructor
	 * @param trainedData
	 */
	public FlagDetector(DocumentDataset trainedData){
		this.trainedData = trainedData;
	}
	
	/**
	 * 
	 * @param document
	 * @return true if the document should be flagged
	 */
	public boolean detectFlag(TextDocument document){
		//assuming flagged document (spam email or positive review)
		double postProbIsFlagged = Math.log(trainedData.getPriorProbabilityFlagged());
		//assuming unflagged document (valid email or negative review)
		double postProbIsUnflagged = Math.log(trainedData.getPriorProbabilityUnflagged());
		
		double PWord_i;
		//for each word in the document
		for(String word : document.getWordCountMap().keySet()){
			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < document.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, true));
				postProbIsFlagged += PWord_i;
			}
		
			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < document.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, false));
				postProbIsUnflagged += PWord_i;
			}
		}
		
		if(postProbIsFlagged > postProbIsUnflagged){
			return true;
		}
		return false;
	}
	
	
	
	
	public static void main(String[] args) {
		FileReader fr = new FileReader();
		String emailTrainingDataFilename = "spam_detection/train_email.txt";
		ArrayList<TextDocument> trainingEmails = fr.readDocumentData(emailTrainingDataFilename);
		String emailTestingDataFilename = "spam_detection/test_email.txt";
		ArrayList<TextDocument> testingEmails = fr.readDocumentData(emailTestingDataFilename);
		
		DocumentDataset emailTrainingData = new DocumentDataset(trainingEmails);

		FlagDetector spamDetector = new FlagDetector(emailTrainingData);
		double count=0;
		for(TextDocument email : testingEmails){
			boolean calculated = spamDetector.detectFlag(email);
			boolean expected = email.isFlagged();
			if(calculated == expected){count++;};
			//System.out.println("Expected " + expected + ", determined " + calculated);
		}
		System.out.println("Email Spam Detection Accuracy: " + count/testingEmails.size());
		
		
//		System.out.println(emailTrainingData.getNumUnflaggedDocs() + " Valid Emails");
//		System.out.println(emailTrainingData.getNumFlaggedDocs() + " Spam Emails");
//		System.out.println(emailTrainingData.getNumDocs() + " Total Emails");
//		System.out.println(emailTrainingData.getTotalFlaggedWordCount() + " Spam Word Instances");
//		System.out.println(emailTrainingData.getTotalUnflaggedWordCount() + " Valid Word Instances");
//		System.out.println(emailTrainingData.getTotalWordCount() + " Total Word Instances");
		
//		double sum = 0;
//		for(String key : mrd.getFlaggedLikelihoods().keySet()){
//			sum += mrd.getFlaggedLikelihoods().get(key);
//		}
//		System.out.println(sum + " sum of likelihoods for spam words");
//		
//		sum = 0;
//		for(String key : mrd.getUnflaggedLikelihoods().keySet()){
//			sum += mrd.getUnflaggedLikelihoods().get(key);
//		}
//		System.out.println(sum + " sum of likelihoods for valid words");
//		
//		sum = 0;
//		for(String key : mrd.getCombinedLikelihoods().keySet()){
//			sum += mrd.getCombinedLikelihoods().get(key);
//		}
//		System.out.println(sum + " sum of likelihoods for all words");
		
		
		
		
		
		
		
		
		
		String movieReviewTrainingDataFilename = "movie_reviews/rt-train.txt";
		ArrayList<TextDocument> trainingReviews = fr.readDocumentData(movieReviewTrainingDataFilename);
		String movieReviewTestingDataFilename = "movie_reviews/rt-test.txt";
		ArrayList<TextDocument> testingReviews = fr.readDocumentData(movieReviewTestingDataFilename);
		
		DocumentDataset reviewTrainingData = new DocumentDataset(trainingReviews);
		
		FlagDetector positiveMovieReviewDetector = new FlagDetector(reviewTrainingData);
		count=0;
		for(TextDocument review : testingReviews){
			boolean calculated = positiveMovieReviewDetector.detectFlag(review);
			boolean expected = review.isFlagged();
			if(calculated == expected){count++;};
		}
		System.out.println("Movie Review Positive Review Detection Accuracy: " + count/testingReviews.size());
		
	}

}
