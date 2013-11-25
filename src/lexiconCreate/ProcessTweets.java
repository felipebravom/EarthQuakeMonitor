package lexiconCreate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
			 
			 while ((line = bf.readLine()) != null) {
				 System.out.println(line);
				 
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
