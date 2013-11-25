package lexiconCreate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;


public class TweetDatasetClean {
	
	public static String cleanString(String tweet){
		String res=tweet.replaceAll("u00e1", "á").replaceAll("u00e9", "é").replaceAll("u00ed", "í").
		replaceAll("u00f3", "ó").replaceAll("u00fa", "ú").replaceAll("u00c1", "Á").
		replaceAll("u00c9", "É").replaceAll("u00cd", "Í").replaceAll("u00d3", "Ó").
		replaceAll("u00da", "Ú").replaceAll("u00f1", "ñ").replaceAll("u00d1", "Ñ").
		replaceAll("u00bf", "¿").replaceAll("u00dc", "ü").toLowerCase();
	
		
	
		return res;
	}

	public TweetDatasetClean() {
		// TODO Auto-generated constructor stub
	
		
		
	}
	
	
	static public void main(String args[]) {
		prueba();

		    
	}
	
	static public void prueba(){			

		try {
			DetectorFactory.loadProfile("extra/profiles/");
		} catch (LangDetectException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Detector detector;
		try {
			detector = DetectorFactory.create();
		} catch (LangDetectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	
		
		
		try {
			
			
			PrintWriter pw=new PrintWriter("salida.csv");
	        
			
			
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream("data/tweets.csv.zip"));
			ZipInputStream zis=new ZipInputStream(bis);
			
			ZipFile zf=new ZipFile("data/tweets.csv.zip");
			
			ZipEntry ze=zf.getEntry("tweets.csv");
			
			 BufferedReader bf = new BufferedReader(
					 new InputStreamReader(zf.getInputStream(ze)));
			 
			 String line;
			 
			 while ((line = bf.readLine()) != null) {
				 String data[]=line.split("\",");
				 String tweet=cleanString(data[0].replace("\"", ""));
				 
				 
				 try {
					detector = DetectorFactory.create();
					 detector.append(tweet);
				     if(detector.detect().equals("es"))
				    	 pw.println(tweet);
				    	 
				     
				} catch (LangDetectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}

			 }
			 
			 bf.close();
			 zf.close();
			 pw.close();
			
			
		
			     
			
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//= new ZipInputStream(new BufferedInputStream(fis));
		
		
		
	}

}
