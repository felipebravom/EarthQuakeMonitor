package lexiconCreate;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cmu.arktweetnlp.Twokenize;

public class CreateLexicon {
	
	
	static public void main(String args[]){
		
		
		try {
			ZipFile zf = new ZipFile("data/processed_tweets.csv.zip");
			ZipEntry ze=zf.getEntry("processed_tweets.csv");
			
			 BufferedReader bf = new BufferedReader(
					 new InputStreamReader(zf.getInputStream(ze)));
			 
			 Map<String,Map<String,Double>> wordMap=new HashMap<String,Map<String,Double>>();
			 Twokenize tokenizer = new Twokenize();
				
			 double poscount=0.0d;
			 double negcount=0.0d;
			 double neucount=0.0d;
			 
			 
			 String line;
			 while( (line=bf.readLine())!=null){
				 String parts[]=line.split("\t");
				 String content=parts[0];
				 String sspol=parts[3];
				 String s140=parts[4];
				 
				// System.out.println(content+" "+sspol+" "+s140);
				 // Solo considero tweets con igual polarida segun ambos metodos
				 if(sspol.equals(s140)){
					 List<String> words=tokenizer.tokenize(content);
					 
					 
					 
					 
					 
					 for(String word:words){
						 
						 if(sspol.equals("neutral"))
							 neucount++;
						 else if(sspol.equals("positive"))
							 poscount++;
						 else
							 negcount++;
						 
						 
						 
						 if(!wordMap.containsKey(word)){
							 double pos=0.0d;
							 double neu=0.0d;
							 double neg=0.0d;
							 
							 if(sspol.equals("neutral"))
								 neu++;
							 else if(sspol.equals("positive"))
								 pos++;
							 else
								 neg++;
							 Map<String,Double> polMap=new HashMap<String,Double>();
							 polMap.put("pos", pos);
							 polMap.put("neu", neu);
							 polMap.put("neg", neg);
							 
							 wordMap.put(word, polMap);						 
							 
							 
						 }
						 else{
							 Map<String,Double> polMap=wordMap.get(word);
							 if(sspol.equals("neutral")){
								 polMap.put("neu", polMap.get("neu")+1);
							 }								 
							 else if(sspol.equals("positive")){
								 polMap.put("pos", polMap.get("pos")+1);
							 }								
							 else{
								 polMap.put("neg", polMap.get("neg")+1);
							 }
											 
						 }					 
						 
					 }
					 
					 
					 
				 }
			 }
			 
			 
			 

			 PrintWriter pw=new PrintWriter("lexicon.csv");
			 
			 pw.println("word\tpos\tneg\tneu\tposProb\tnegProb\tneuProb\tsubProb\tpolScore\tsubScore");
			 
			 String words[]=wordMap.keySet().toArray(new String[0]);
			 Arrays.sort(words);
			 
			 double polScoreMin=0;
			 double polScoreMax=0;
			 
			 double subScoreMin=0;
			 double subScoreMax=0;
			 
			 for(String word:words){
				 Map<String,Double> polMap=wordMap.get(word);				 
				 double pos=polMap.get("pos");
				 double neu=polMap.get("neu");
				 double neg=polMap.get("neg");
				 
				 double posProb=pos/poscount;				 
				 double negProb=neg/negcount;
				 
				 polMap.put("posProb",posProb);
				 polMap.put("negProb",negProb);
				 
				 double neuProb=neu/neucount;
				 
				 polMap.put("neuProb", neuProb);
				 
				 double subProb=(pos+neg)/(poscount+negcount); // probabiliad de la palabra de ser sujetiva
				 
				 polMap.put("subProb", subProb);
				 
				 
				 double polScore=posProb-negProb;
				 
				 polMap.put("polScore", polScore);
				 
				 
				 if(polScore>polScoreMax){
					 polScoreMax=polScore;
				 }
				 if(polScore<polScoreMin){
					 polScoreMin=polScore;
				 }
				 
				 double subScore=subProb-neuProb;
				 
				 polMap.put("subScore",subScore);
				
				 if(subScore>subScoreMax){
					 subScoreMax=subScore;
				 }
				 if(subScore<subScoreMin){
					 subScoreMin=subScore;
				 }
				 
				 
				 pw.println(word+"\t"+pos+"\t"+neg+"\t"+neu+"\t"+posProb+"\t"+negProb+
						 "\t"+neuProb+"\t"+subProb+"\t"+polScore+"\t"+subScore);
				 
				 
				 
			 }
			 
			 pw.close();
			 
			 pw=new PrintWriter("lexiconScore.csv");
			 
			 pw.println("word\tpolScore\tsubScore");
			 
			 for(String word:words){
				 Map<String,Double> polMap=wordMap.get(word);	
				 double polScore=polMap.get("polScore");
				 if(polScore<0){
					 polScore=(polScore/polScoreMin)*(-5); // Lo escalamos entre 0 y 5
				 }
				 if(polScore>0){
					 polScore=(polScore/polScoreMax)*(5);  // los negativas entre 0 y -5
				 }
				 double subScore=polMap.get("subScore");
				 if(subScore<0){
					 subScore=(subScore/subScoreMin)*(-5);
				 }
				 if(subScore>0){
					 subScore=(subScore/subScoreMax)*(5);
				 }
				 
				 
				 pw.println(word+"\t"+polScore+"\t"+subScore);
				 
			 
			 
			 
			 
			 }
			 
			 
			 
			 
			 
			 pw.close();
			 
			 
			 
			
		
		
		
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
