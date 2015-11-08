import java.util.HashMap;

public class Email {
	
	private HashMap<String, Integer> wordCountMap;
	private boolean bIsSpam;
	
	public Email(boolean bIsSpam, HashMap<String, Integer> wordCountMap){
		this.wordCountMap = wordCountMap;
		this.bIsSpam = bIsSpam;
	}
	
	public HashMap<String, Integer> getWordCountMap() {
		return this.wordCountMap;
	}
	
	public boolean isSpam() {
		return this.bIsSpam;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
