package lexiconCreate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

// This class process a Lexicon with different variables

public class EarthQuakeLexEvaluator {
	protected String path;
	protected Map<String, Map<String,Double>> dict;


	public EarthQuakeLexEvaluator(String path) {
		this.path=path;
		this.dict=new HashMap<String, Map<String,Double>>();

	}

	public Map<String, Map<String,Double>> getDict(){
		return this.dict;
	}

	public Map<String,Double> getWord(String word){
		if(this.dict.containsKey(word))
			return dict.get(word);
		else
			return null;

	}

	public void processDict(){
		Scanner sc;
		try {
			sc = new Scanner(new File(this.path));
			sc.useDelimiter("\n");
			// Skip firt line
			sc.next();
			
			for (String line = sc.next(); sc.hasNext(); line = sc.next()) {
				String content[] = line.split("\t");
				String word=content[0];
				double pol=Double.parseDouble(content[1]);				
				double sub=Double.parseDouble(content[2]);

				Map<String,Double> emotionMap=new HashMap<String,Double>();
				
				// Insert polarity and subjeticity values
				emotionMap.put("pol", pol);
				emotionMap.put("subj", sub);
				
				
				
				
				this.dict.put(word, emotionMap);



			}

			sc.close();	



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public void main(String args[]){
		EarthQuakeLexEvaluator eval=new EarthQuakeLexEvaluator("extra/earthQuakeLex.csv");
		eval.processDict();

		Map<String,Double> pal=eval.getWord("csm");

		if(pal!=null){
			for(String emo:pal.keySet()){
				System.out.println(emo+" "+pal.get(emo));
			}
		}



	}

}
