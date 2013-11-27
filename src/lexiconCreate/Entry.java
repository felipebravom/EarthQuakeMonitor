package lexiconCreate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmu.arktweetnlp.Twokenize;
import uk.ac.wlv.sentistrength.SentiStrength;

public class Entry {
	private String content;
	private Map<String, String> features;

	public Entry(String content) {
		this.content = content;
		this.features = new HashMap<String, String>();

		// TODO Auto-generated constructor stub
	}

	public void setFeature(String name, String value) {
		this.features.put(name, value);
	}

	public String getContent() {
		return this.content;
	}

	public Map<String, String> getFeatures() {
		return this.features;
	}

	public String toString() {
		String value = content;
		value += "\t"+this.features.get("SSpos")+"\t"+this.features.get("SSneg")+
				"\t"+this.features.get("SSneu")+"\t"+this.features.get("s140");

		return value;
	}

	public void evaluateSentiStrength(SentiStrength sentiStrength) {
		String tweet = this.content.replaceAll("\\+", " ");
		Twokenize t = new Twokenize();
		String words[] = t.tokenize(tweet).toArray(new String[0]);

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
}
