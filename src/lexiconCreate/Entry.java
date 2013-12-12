package lexiconCreate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmu.arktweetnlp.Twokenize;
import uk.ac.wlv.sentistrength.SentiStrength;

public class Entry {
	private String content;
	private List<String> words;
	
	private Map<String, Object> features;

	public Entry(String content) {
		this.content = content;
		this.features = new HashMap<String, Object>();

		// TODO Auto-generated constructor stub
	}
	
	public void tokenize(){
		String tweet = this.content.replaceAll("\\+", " ").replaceAll("([aeiou])\\1+","$1");;
		
	 //	Twokenize t = new Twokenize();
		List<String> words = Twokenize.tokenizeRawTweetText(tweet);
		
		List<String> cleanWords=new ArrayList<String>();


		for(String word:words){

			String cleanWord=word; 


		
			
			if(word.matches("http.*|www\\..*")){
				cleanWord="URL";
			}
			else if(word.matches("@.*")){
				cleanWord="USER";
			}	


			cleanWords.add(cleanWord);


		}
						
		this.words=cleanWords;
		
	}

	public void setFeature(String name, String value) {
		this.features.put(name, value);
	}

	public String getContent() {
		return this.content;
	}

	public Map<String, Object> getFeatures() {
		return this.features;
	}

	public String toString() {
		String value = content;
		value += "\t"+this.features.get("SSpos")+"\t"+this.features.get("SSneg")+
				"\t"+this.features.get("SSneu")+"\t"+this.features.get("s140");

		return value;
	}

	public void evaluateSentiStrength(SentiStrength sentiStrength) {
		
		String words[] = this.words.toArray(new String[0]);

		String sentence = "";

		for (int i = 0; i < words.length; i++) {
			sentence += words[i];
			if (i < words.length - 1) {
				sentence += "+";
			}
		}

		String result = sentiStrength.computeSentimentScores(sentence);

	

		String[] values = result.split(" ");
		
		this.setFeature("SSpos", values[0]);
		this.setFeature("SSneg", values[1]);
		
		int neu = Integer.parseInt(values[2]);
		String neuString;
		if(neu>0)
			neuString="positive";
		else if(neu==0)
			neuString="neutral";
		else
			neuString="negative";

		
		this.setFeature("SSneu", neuString );

		


	}
	
	
	public void evaluateEarthQuakeLex(EarthQuakeLexEvaluator eqLex){
		
		double earthPol=0;
		double earthSub=0;
		
		// Adds the scores of the words according to the Lexicon
		for(String word:this.words){
			if(eqLex.getDict().containsKey(word)){
				Map<String,Double> emoMap=eqLex.getWord(word);
				
				earthPol += emoMap.get("pol");
				earthSub += emoMap.get("subj");
				
			}
			
			//earthPol
			
			
			this.features.put("earthPol", earthPol);
			this.features.put("earthSub", earthSub);

			
		}
		
	}
	
	// Evaluates the content according the Elh Spanish Lexicon formed by positive and negative words	
	public void evaluateElhPolar(LexiconEvaluator lex){
		
		int elhPos=0;
		int elhNeg=0;
		
		// Adds the scores of the words according to the Lexicon
		for(String word:this.words){
			if(lex.getDict().containsKey(word)){
				String polarity=lex.getDict().get(word);
				if(polarity.equals("positive"))
					elhPos++;
				else
					elhNeg++;
				
			}
			
			//earthPol
			
			
			this.features.put("elhPos",elhPos);
			this.features.put("elhNeg", elhNeg);

			
		}
		
	}
	
	
	
}
