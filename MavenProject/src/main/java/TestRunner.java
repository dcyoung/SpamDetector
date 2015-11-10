import java.util.ArrayList;

public class TestRunner {

	
	public TestRunner(){
		
	}
	
	public double TestSpamDetectionAccuracy(){
		
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
		double accuracy = count / testingEmails.size();
		//System.out.println("Email Spam Detection Accuracy: " + accuracy);
		return accuracy; 
	}
	
	
	public double TestMovieReviewDetectionAccuracy(){
		
		FileReader fr = new FileReader();
		
		String movieReviewTrainingDataFilename = "movie_reviews/rt-train.txt";
		ArrayList<TextDocument> trainingReviews = fr.readDocumentData(movieReviewTrainingDataFilename);
		String movieReviewTestingDataFilename = "movie_reviews/rt-test.txt";
		ArrayList<TextDocument> testingReviews = fr.readDocumentData(movieReviewTestingDataFilename);
		
		DocumentDataset reviewTrainingData = new DocumentDataset(trainingReviews);
		
		FlagDetector positiveMovieReviewDetector = new FlagDetector(reviewTrainingData);
		double count=0;
		for(TextDocument review : testingReviews){
			boolean calculated = positiveMovieReviewDetector.detectFlag(review);
			boolean expected = review.isFlagged();
			if(calculated == expected){count++;};
		}
		double accuracy = count / testingReviews.size();
		//System.out.println("Movie Review Positive Review Detection Accuracy: " + accuracy);
		
		return accuracy;
	}
	
	public static void main(String[] args) {

		TestRunner tr = new TestRunner();
		double spamAccuracy = tr.TestSpamDetectionAccuracy();
		double movieAccuracy = tr.TestMovieReviewDetectionAccuracy();
		System.out.println("Email Spam Detection Accuracy: " + spamAccuracy);
		System.out.println("Movie Review Positive Review Detection Accuracy: " + movieAccuracy);
		
		
	}

}
