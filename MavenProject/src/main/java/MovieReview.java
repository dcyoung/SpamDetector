import java.util.HashMap;

public class MovieReview {
	
	private HashMap<String, Integer> wordCountMap;
	private boolean bIsPositive;
	
	public MovieReview(boolean bIsPositive, HashMap<String, Integer> wordCountMap){
		this.wordCountMap = wordCountMap;
		this.bIsPositive = bIsPositive;
	}
	
	public HashMap<String, Integer> getWordCountMap() {
		return this.wordCountMap;
	}
	
	public boolean isPositive() {
		return this.bIsPositive;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
