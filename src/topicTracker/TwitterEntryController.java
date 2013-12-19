package topicTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lexiconCreate.EarthQuakeLexEvaluator;
import lexiconCreate.LexiconEvaluator;

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
		content=content.replaceAll("([aeiou])\\1+","$1"); // remove repeated vowels
		Twokenize tokenizer = new Twokenize();

		

		List<String> tokens=new ArrayList<String>();


		for(String word:tokenizer.tokenizeRawTweetText(content)){

			String cleanWord=word; 


			// Replace URLs for a special token URL
			if(word.matches("http.*|www\\..*")){
				cleanWord="URL";
			}
			
			// Replace user mentions to a special token USER
			else if(word.matches("@.*")){
				cleanWord="USER";
			}	

			tokens.add(cleanWord);


		}

		this.twitterEntry.setTokens(tokens);		

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




	public void evaluateEarthQuakeLex(EarthQuakeLexEvaluator eqLex){

		double earthPos=0;
		double earthNeg=0;
		double earthSub=0;
		double earthNeu=0;

		// Adds the scores of the words according to the Lexicon
		for(String word:this.twitterEntry.getTokens()){
			if(eqLex.getDict().containsKey(word)){
				Map<String,Double> emoMap=eqLex.getWord(word);

				double polarity=emoMap.get("pol");
				if(polarity>0){
					earthPos += polarity; 					
				}
				else if(polarity<0){
					earthNeg += polarity;
				}

				double subjectivity=emoMap.get("subj");

				if(subjectivity>0){
					earthSub += emoMap.get("subj");								
				}
				else if(subjectivity<0)
					earthNeu += emoMap.get("subj");

			}

		}


		this.twitterEntry.getFeatures().put("earthPos", earthPos);
		this.twitterEntry.getFeatures().put("earthNeg", earthNeg);


		this.twitterEntry.getFeatures().put("earthSub", earthSub);

		this.twitterEntry.getFeatures().put("earthNeu", earthNeu);


	}

	// Evaluates the content according the Elh Spanish Lexicon formed by positive and negative words	
	public void evaluateElhPolar(LexiconEvaluator lex){

		int elhPos=0;
		int elhNeg=0;

		// Adds the scores of the words according to the Lexicon
		for(String word:this.twitterEntry.getTokens()){
			if(lex.getDict().containsKey(word)){
				String polarity=lex.getDict().get(word);
				if(polarity.equals("positive"))
					elhPos++;
				else
					elhNeg++;

			}			
		}

		this.twitterEntry.getFeatures().put("elhPos",elhPos);
		this.twitterEntry.getFeatures().put("elhNeg", elhNeg);


	}

	// Evaluates the linear classifer
	public void evaluateSentimentLinearModel(){

		// weights for the Subjectivity Logistic Regression
		double InterWsub=-3.056370435;
		double earthSubWsub=0.007649282;
		double earthNeuWsub=0.157909987;
		double earthPosWsub=0.096099674;
		double earthNegWsub= -0.040263249;
		double elhPosWsub=0.295295301;
		double elhNegWsub=1.525096124;


		// weights for the Polarity Logistic Regression
		double InterceptWpol=1.16095248;
		double earthPosWpol=0.08285113;
		double earthNegWpol=0.08058357;
		double elhPosWpol=0.52087827;
		double elhNegWpol=-2.30640353;


		double earthSub=(Double)this.twitterEntry.getFeatures().get("earthSub");
		double earthNeu=(Double)this.twitterEntry.getFeatures().get("earthNeu");
		double earthPos=(Double)this.twitterEntry.getFeatures().get("earthPos");
		double earthNeg=(Double)this.twitterEntry.getFeatures().get("earthNeg");
		double elhPos=(Integer)this.twitterEntry.getFeatures().get("elhPos");
		double elhNeg=(Integer)this.twitterEntry.getFeatures().get("elhNeg");




		double subScore=InterWsub+earthSubWsub*earthSub+earthNeuWsub*earthNeu+earthPosWsub*earthPos
				+earthNegWsub*earthNeg+elhPosWsub*elhPos+elhNegWsub*elhNeg;




		double polScore=InterceptWpol+earthPosWpol*earthPos+earthNegWpol*earthNeg+
				elhPosWpol*elhPos+elhNegWpol*elhNeg;

		
		// Label corresponds to the polarity class, 1 for positive, 0 for neutral and -1 for negative

		int label;
		if(subScore<=0){
			label=0;
		}
		else{
			if(polScore<0){
				label=-1;
			}
			else{
				label=1;
			}
		}

		this.twitterEntry.getFeatures().put("subScore", subScore);
		this.twitterEntry.getFeatures().put("polScore", polScore);
		
		
		this.twitterEntry.getFeatures().put("label", label);






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
