import java.util.HashMap;

public class TextDocument {
	
	private HashMap<String, Integer> wordCountMap;
	private boolean bIsFlagged;
	
	public TextDocument(boolean bIsFlagged, HashMap<String, Integer> wordCountMap){
		this.wordCountMap = wordCountMap;
		this.bIsFlagged = bIsFlagged;
	}
	
	public HashMap<String, Integer> getWordCountMap() {
		return this.wordCountMap;
	}
	
	public boolean isFlagged() {
		return this.bIsFlagged;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
