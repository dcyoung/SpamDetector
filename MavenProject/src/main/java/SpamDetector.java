import java.util.ArrayList;

public class SpamDetector {

	EmailDataset trainedData;
	
	public SpamDetector(EmailDataset trainedData){
		this.trainedData = trainedData;
	}
	
	public boolean detectSpam(Email email){
		//assuming spam email
		double postProbIsSpam = Math.log(trainedData.getPriorProbabilitySpam());
		//assuming valid email
		double postProbIsValid = Math.log(trainedData.getPriorProbabilityValid());
		
		double PWord_i;
		//for each word in the email
		for(String word : email.getWordCountMap().keySet()){
			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < email.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, true));
				postProbIsSpam += PWord_i;
			}
		
			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < email.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, false));
				postProbIsValid += PWord_i;
			}
			
		}
		
		if(postProbIsSpam > postProbIsValid){
			return true;
		}
		return false;
	}
	
	
	
	
	public static void main(String[] args) {
		FileReader fr = new FileReader();
		String emailTrainingDataFilename = "spam_detection/train_email.txt";
		ArrayList<Email> trainingEmails = fr.readEmailData(emailTrainingDataFilename);
		String emailTestingDataFilename = "spam_detection/test_email.txt";
		ArrayList<Email> testingEmails = fr.readEmailData(emailTestingDataFilename);
		
		EmailDataset mrd = new EmailDataset(trainingEmails);
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
		
		
		SpamDetector spamDetector = new SpamDetector(mrd);
		double count=0;
		for(Email email : testingEmails){
			boolean calculated = spamDetector.detectSpam(email);
			boolean expected = email.isSpam();
			if(calculated == expected){count++;};
			//System.out.println("Expected " + expected + ", determined " + calculated);
		}
		System.out.println(count/testingEmails.size());
		
	}

}
