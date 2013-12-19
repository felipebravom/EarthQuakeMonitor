package lexiconCreate;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import uk.ac.wlv.sentistrength.SentiStrength;

import cmu.arktweetnlp.Twokenize;

// Crea datos de entrenamiento con los distintos lexicos y SentiStrength

public class CreateTrainData {


	static public void main(String args[]){
		
		SentiStrength sentiStrength = new SentiStrength();
		String sentiParams[] = {"sentidata", "extra/SentiStrength/spanish/", "trinary"};
		sentiStrength.initialise(sentiParams);	

		EarthQuakeLexEvaluator eqLex=new EarthQuakeLexEvaluator("extra/earthQuakeLex.csv");
		eqLex.processDict();

		LexiconEvaluator elhPol = new LexiconEvaluator("extra/ElhPolar_es.csv");
		elhPol.processDict();



		try {
			ZipFile zf = new ZipFile("data/processed_tweets.csv.zip");
			ZipEntry ze=zf.getEntry("processed_tweets.csv");

			BufferedReader bf = new BufferedReader(
					new InputStreamReader(zf.getInputStream(ze)));

			Map<String,Map<String,Double>> wordMap=new HashMap<String,Map<String,Double>>();
			Twokenize tokenizer = new Twokenize();

			List<Entry> entries=new ArrayList<Entry>();
			
			PrintWriter pw=new PrintWriter("dataset.csv");
			pw.println("content\tSSPOS\tSSNEG\tearthPos\tearthNeg\tearthSub\tearthNeu\telhPos\telhNeg\tlabel\tpredicted\tsubScore\tpolScore");

			String line;
			while( (line=bf.readLine())!=null){
				String parts[]=line.split("\t");
				String content=parts[0];
				String sspol=parts[3];
				String s140=parts[4];

				// System.out.println(content+" "+sspol+" "+s140);
				// Solo considero tweets con igual polarida segun ambos metodos
				if(sspol.equals(s140)){
					
					//System.out.println(content);
					
					
					Entry e=new Entry(content);
					e.tokenize();
					
					e.evaluateSentiStrength(sentiStrength);
					e.evaluateEarthQuakeLex(eqLex);
					e.evaluateElhPolar(elhPol);
					
					e.evaluateSentimentLinearModel();
					
					e.getFeatures().put("label", sspol);
					
					
					pw.println(e.getContent()+"\t"+e.getFeatures().get("SSpos")+"\t"+e.getFeatures().get("SSneg")+"\t"+
							e.getFeatures().get("earthPos")+"\t"+e.getFeatures().get("earthNeg")+"\t"+
							e.getFeatures().get("earthSub")+"\t"+e.getFeatures().get("earthNeu")+
							"\t"+e.getFeatures().get("elhPos")
							+"\t"+e.getFeatures().get("elhNeg")+"\t"+e.getFeatures().get("label")
							+"\t"+e.getFeatures().get("predicted")+"\t"+e.getFeatures().get("subScore")
							+"\t"+e.getFeatures().get("polScore")
							
							);
					
					

					
				//	System.out.println("agregue");
					
				}
			}
			
			
			
		


			
			pw.close();
		







		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}



}
