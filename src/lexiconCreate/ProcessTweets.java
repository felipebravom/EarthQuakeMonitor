package lexiconCreate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import uk.ac.wlv.sentistrength.SentiStrength;
import cmu.arktweetnlp.Twokenize;

public class ProcessTweets {

	public ProcessTweets() {
		// TODO Auto-generated constructor stub
	}
	
	static public void main(String args[]){
		
		
		try {

			
			ZipFile zf=new ZipFile("data/clean_spanish_tweets.csv.zip");
			
			ZipEntry ze=zf.getEntry("clean_spanish_tweets.csv");
			
			 BufferedReader bf = new BufferedReader(
					 new InputStreamReader(zf.getInputStream(ze)));
			 
			 String line;
			 
			 
			 SentiStrength sentiStrength = new SentiStrength();
			 String sentiParams[] = {"sentidata", "extra/SentiStrength/spanish/", "trinary"};
			 sentiStrength.initialise(sentiParams);	
			 
			 List<Entry> entries=new ArrayList<Entry>();
			 int i=0;
			 
			 while ((line = bf.readLine()) != null && i <30 ) {	
				 Entry e=new Entry(line);
				 e.evaluateSentiStrength(sentiStrength);
					 
				 entries.add(e);			 
				 i++;
			 }
			 
			 Sent140Evaluator s140=new Sent140Evaluator(entries);
			 s140.evaluateSentimentApiEntrySet();
			 
			 for(Entry e:entries){
				 System.out.println(e.toString());
			 }
			 
			 
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
