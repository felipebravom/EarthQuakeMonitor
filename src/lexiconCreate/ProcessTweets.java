package lexiconCreate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

		//	PrintWriter out = new PrintWriter("processed.csv");
			
			
			
			ZipFile zf=new ZipFile("data/clean_spanish_tweets.csv.zip");
			
			ZipEntry ze=zf.getEntry("clean_spanish_tweets.csv");
			
			 BufferedReader bf = new BufferedReader(
					 new InputStreamReader(zf.getInputStream(ze)));
			 
			 String line;
			 
			 
			 SentiStrength sentiStrength = new SentiStrength();
			 String sentiParams[] = {"sentidata", "extra/SentiStrength/spanish/", "trinary"};
			 sentiStrength.initialise(sentiParams);	
			
			 PrintWriter out = new PrintWriter(new FileOutputStream( new File("processed.csv"), true)); 				
			 List<Entry> entries=new ArrayList<Entry>();
			 int i=0;
			 
			 while ((line = bf.readLine()) != null ) {
				 
				 Entry e=new Entry(line.replaceAll("\t", " "));
				 e.evaluateSentiStrength(sentiStrength);					 
				 entries.add(e);			 
				 
				 if(i==9000){
					 Sent140Evaluator s140=new Sent140Evaluator(entries);
					 s140.evaluateSentimentApiEntrySet();
					 for(Entry ent:entries){
						 out.println(ent.toString());
					 }
					 out.close();
					 out = new PrintWriter(new FileOutputStream( new File("processed.csv"), true)); 
					 entries.clear();
					 i=0;
					 try {
						Thread.sleep(1000*1);
						System.out.println("Deperte");
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						continue;
					}		 
					 
				 }
				 
				 
				 i++;
			 }
			 
			 
			 
			 
			 
			 
			 
			 zf.close();
			 
			 
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
