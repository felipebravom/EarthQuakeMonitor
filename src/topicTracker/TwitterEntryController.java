package topicTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import uk.ac.wlv.sentistrength.SentiStrength;
import cmu.arktweetnlp.Twokenize;

// Contains a TwitterEntry object and applies different filters to it
public class TwitterEntryController {
	private TwitterEntry twitterEntry;


	public TwitterEntryController(TwitterEntry twitterEntry){
		this.twitterEntry=twitterEntry;
	}

	public TwitterEntry getTwitterEntry(){
		return this.twitterEntry;
	}


	// Tokenizes the content of the lowercased tweet 
	public void tokenize(){
		String content=this.twitterEntry.getStatus().getText().toLowerCase();
		Twokenize tokenizer = new Twokenize();

		List<String> tokens=tokenizer.tokenizeRawTweetText(content);
		this.twitterEntry.setTokens(tokens);		

	}
	
	
	// Replaces URLs to special characters, as wells as User mentions, and repeated vowels
	public void processTokens(){
		
		List<String> cleanWords=new ArrayList<String>();


		for(String word:this.twitterEntry.getTokens()){

			String cleanWord; 


			cleanWord=word.replaceAll("([aeiou])\\1+","$1");
			
			if(word.matches("http.*|www\\..*")){
				cleanWord="URL";
			}
			else if(word.matches("@.*")){
				cleanWord="USER";
			}	


			cleanWords.add(cleanWord);


		}
		
		this.twitterEntry.setTokens(cleanWords);		
		
		
		
	}

	// Detects the language of the message
	public void detectLanguage(){
		try {
			
			Detector detector=DetectorFactory.create();
			detector.append(this.twitterEntry.getStatus().getText());

			String lang=detector.detect();
			this.twitterEntry.getFeatures().put("lang", lang);

		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	// Evaluates the sentiment using SentiStrength method 
	public void evaluateSentiStrength(SentiStrength sentiStrength){
		String words[]=this.twitterEntry.getTokens().toArray(new String[0]);

		String sentence = "";

		for (int i = 0; i < words.length; i++) {
			sentence += words[i];
			if (i < words.length - 1) {
				sentence += "+";
			}
		}


		String result = sentiStrength.computeSentimentScores(sentence);


		String[] values = result.split(" ");

		int pos = Integer.parseInt(values[0]);
		int neg = Integer.parseInt(values[1]);
		int neu = Integer.parseInt(values[2]);

		Map<String,Object> features=this.twitterEntry.getFeatures();

		features.put("pos",pos);
		features.put("neg",neg);
		features.put("neu", neu);


	}





}
