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
	
	
	public ArrayList<TextDocument> readDocumentData(String filename){
		ArrayList<TextDocument> allDocuments = new ArrayList<TextDocument>();
		HashMap<String, Integer> tempWordCountMap =  new HashMap<String,Integer>();
		String tempWord;
		boolean bIsFlagged;
		int tempCount;
		String fileLine;
		String[] documentWords;
		
		try {
			Scanner lineScanner = new Scanner(new File(this.getClass().getResource( "/"+filename).toURI()));
			//for each review (each line in the file)
			while(lineScanner.hasNextLine()){
				fileLine = lineScanner.nextLine();
				tempWordCountMap.clear();
				
				//read off the label for the document (spam or not spam/ positive or negative)
				bIsFlagged =(fileLine.charAt(0) == '1') ? true : false;
				
				fileLine = fileLine.substring(1); //remove the label from the beginning of the string
				documentWords = fileLine.split("\\:"); //split the string into an array
				
				//for each word in this document
				for(int i = 0; i < documentWords.length-1; i++){
					//grab the word
					tempWord = documentWords[i].substring(1).trim();
					//grab the wordCount (convert the character for the word count to an integer)
					tempCount = (int)(documentWords[i+1].charAt(0) - '0');  
					
					tempWordCountMap.put(tempWord, tempCount);
				}
				
				allDocuments.add(new TextDocument(bIsFlagged, new HashMap<String, Integer>(tempWordCountMap)));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allDocuments;
	}
	
	
	public static void main(String[] args) {

		FileReader fr = new FileReader();
		String movieReviewFilename = "movie_reviews/rt-test.txt";
		ArrayList<TextDocument> reviews = fr.readDocumentData(movieReviewFilename);
		
		int reviewNum = 39;
		String word = "often";
		//System.out.println(reviews.get(reviewNum).getWordCountMap().keySet());
		System.out.println(reviews.get(reviewNum).getWordCountMap().get(word));
		
		
		String emailFilename = "spam_detection/train_email.txt";
		ArrayList<TextDocument> emails = fr.readDocumentData(emailFilename);
		int emailNum = 0;
		System.out.println(emails.get(emailNum).getWordCountMap().keySet());
		
	}

}
