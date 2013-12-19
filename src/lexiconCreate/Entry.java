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

		double earthPos=0;
		double earthNeg=0;
		double earthSub=0;
		double earthNeu=0;

		// Adds the scores of the words according to the Lexicon
		for(String word:this.words){
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


		this.features.put("earthPos", earthPos);
		this.features.put("earthNeg", earthNeg);


		this.features.put("earthSub", earthSub);

		this.features.put("earthNeu", earthNeu);


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
		}

		this.features.put("elhPos",elhPos);
		this.features.put("elhNeg", elhNeg);


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


		double earthSub=(Double)this.features.get("earthSub");
		double earthNeu=(Double)this.features.get("earthNeu");
		double earthPos=(Double)this.features.get("earthPos");
		double earthNeg=(Double)this.features.get("earthNeg");
		double elhPos=(Integer)this.features.get("elhPos");
		double elhNeg=(Integer)this.features.get("elhNeg");
		
		
		
		
		double subScore=InterWsub+earthSubWsub*earthSub+earthNeuWsub*earthNeu+earthPosWsub*earthPos
				+earthNegWsub*earthNeg+elhPosWsub*elhPos+elhNegWsub*elhNeg;
		
			
		

		double polScore=InterceptWpol+earthPosWpol*earthPos+earthNegWpol*earthNeg+
				elhPosWpol*elhPos+elhNegWpol*elhNeg;
		
		
		String label;
		if(subScore<=0){
			label="neutral";
		}
		else{
			if(polScore<0){
				label="negative";
			}
			else{
				label="positive";
			}
		}
		
		features.put("predicted", label);
		features.put("subScore", subScore);
		features.put("polScore", polScore);
		




	}



}
