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
			 
			 Map<String,Map<String,Integer>> wordMap=new HashMap<String,Map<String,Integer>>();
			 Twokenize tokenizer = new Twokenize();
				
			 int poscount=0;
			 int negcount=0;
			 int neucount=0;
			 
			 
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
							 int pos=0;
							 int neu=0;
							 int neg=0;
							 
							 if(sspol.equals("neutral"))
								 neu++;
							 else if(sspol.equals("positive"))
								 pos++;
							 else
								 neg++;
							 Map<String,Integer> polMap=new HashMap<String,Integer>();
							 polMap.put("pos", pos);
							 polMap.put("neu", neu);
							 polMap.put("neg", neg);
							 
							 wordMap.put(word, polMap);						 
							 
							 
						 }
						 else{
							 Map<String,Integer> polMap=wordMap.get(word);
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
			 
			 pw.println("word\tposCount\tneuCount\tnegCount");
			 
			 String words[]=wordMap.keySet().toArray(new String[0]);
			 Arrays.sort(words);
			 
			 for(String word:words){
				 Map<String,Integer> polMap=wordMap.get(word);				 
				 int pos=polMap.get("pos");
				 int neu=polMap.get("neu");
				 int neg=polMap.get("neg");
				 
				 double posProb=pos/poscount;
				 double negProb=neg/negcount;
				 double neuProb=neu/neucount;
				 
				 
				
				 pw.println(word+"\t"+pos+"\t"+neu+"\t"+neg+"\t"+posProb+"\t"+negProb+"\t"+neuProb);
				 
				 
			 }
			 
			 pw.close();
			 
			 
			 
			
		
		
		
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
