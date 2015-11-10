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
	public boolean detectFlag(TextDocument document, boolean bernoulli){
		//assuming flagged document (spam email or positive review)
		double postProbIsFlagged = Math.log(trainedData.getPriorProbabilityFlagged());
		//assuming unflagged document (valid email or negative review)
		double postProbIsUnflagged = Math.log(trainedData.getPriorProbabilityUnflagged());

		double PWord_i;
		//for each word in the document
		for(String word : document.getWordCountMap().keySet()){
			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < document.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, true, bernoulli));
				postProbIsFlagged += PWord_i;
			}

			//be sure to consider each instance of the same word as a new W_i
			for(int i = 0; i < document.getWordCountMap().get(word); i++){
				PWord_i = Math.log(this.trainedData.getSpecificLikelihood(word, false, bernoulli));
				postProbIsUnflagged += PWord_i;
			}
		}

		
		if(postProbIsFlagged > postProbIsUnflagged){
			return true;
		}
		return false;
	}
	
	
	
	
	public static void main(String[] args) {
		
	}

}
