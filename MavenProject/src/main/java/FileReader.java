import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {
	
	/**
	 * Constructor
	 */
	public FileReader(){
		
	}
	
	public ArrayList<MovieReview> readMovieReviewData(String filename){
		ArrayList<MovieReview> allReviews = new ArrayList<MovieReview>();
		HashMap<String, Integer> tempWordCountMap =  new HashMap<String,Integer>();
		String tempWord;
		boolean bIsPositiveReview;
		int tempCount;
		String fileLine;
		String[] reviewWords;
		
		try {
			Scanner lineScanner = new Scanner(new File(this.getClass().getResource( "/"+filename).toURI()));
			//for each review (each line in the file)
			while(lineScanner.hasNextLine()){
				fileLine = lineScanner.nextLine();
				tempWordCountMap.clear();
				
				//read off the label for the review (positive or negative review)
				bIsPositiveReview =(fileLine.charAt(0) == '1') ? true : false;
				
				fileLine = fileLine.substring(1); //remove the label from the beginning of the string
				reviewWords = fileLine.split("\\:"); //split the string into an array
				
				//for each word in this review
				for(int i = 0; i < reviewWords.length-1; i++){
					//grab the word
					tempWord = reviewWords[i].substring(1).trim();
					//grab the wordCount (convert the character for the word count to an integer)
					tempCount = (int)(reviewWords[i+1].charAt(0) - '0');  
					
					tempWordCountMap.put(tempWord, tempCount);
				}
				
				allReviews.add(new MovieReview(bIsPositiveReview, new HashMap<String, Integer>(tempWordCountMap)));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allReviews;
	}
	
	public ArrayList<Email> readEmailData(String filename){
		ArrayList<Email> allEmails = new ArrayList<Email>();
		HashMap<String, Integer> tempWordCountMap =  new HashMap<String,Integer>();
		String tempWord;
		boolean bIsSpam;
		int tempCount;
		String fileLine;
		String[] emailWords;
		
		try {
			Scanner lineScanner = new Scanner(new File(this.getClass().getResource( "/"+filename).toURI()));
			//for each review (each line in the file)
			while(lineScanner.hasNextLine()){
				fileLine = lineScanner.nextLine();
				tempWordCountMap.clear();
				
				//read off the label for the email (spam or not spam)
				bIsSpam =(fileLine.charAt(0) == '1') ? true : false;
				
				fileLine = fileLine.substring(1); //remove the label from the beginning of the string
				emailWords = fileLine.split("\\:"); //split the string into an array
				
				//for each word in this review
				for(int i = 0; i < emailWords.length-1; i++){
					//grab the word
					tempWord = emailWords[i].substring(1).trim();
					//grab the wordCount (convert the character for the word count to an integer)
					tempCount = (int)(emailWords[i+1].charAt(0) - '0');  
					
					tempWordCountMap.put(tempWord, tempCount);
				}
				
				allEmails.add(new Email(bIsSpam, new HashMap<String, Integer>(tempWordCountMap)));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allEmails;
	}
	
	
	
	
	public static void main(String[] args) {

		FileReader fr = new FileReader();
		String movieReviewFilename = "movie_reviews/rt-test.txt";
		ArrayList<MovieReview> reviews = fr.readMovieReviewData(movieReviewFilename);
		
		int reviewNum = 39;
		String word = "often";
		//System.out.println(reviews.get(reviewNum).getWordCountMap().keySet());
		System.out.println(reviews.get(reviewNum).getWordCountMap().get(word));
		
		
		String emailFilename = "spam_detection/train_email.txt";
		ArrayList<Email> emails = fr.readEmailData(emailFilename);
		int emailNum = 0;
		System.out.println(emails.get(emailNum).getWordCountMap().keySet());
		
	}

}
