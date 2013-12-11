package lexiconCreate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub

	}
	static public void main(String args[]){
		Map<String,String> dict=new HashMap<String,String>();

		Scanner sc;
		try {
			sc = new Scanner(new File("extra/ElhPolar_esV1proc.lex"));
			sc.useDelimiter("\n");
			for (String line = sc.next(); sc.hasNext(); line = sc.next()) {
				String pair[] = line.split("\t");

				dict.put(pair[0].toLowerCase(), pair[1]);	
			}
			
			sc.close();

			
			String words[]=dict.keySet().toArray(new String[0]);
			Arrays.sort(words);
			
			
			PrintWriter pw=new PrintWriter("lex.csv");
			for(String word:words){
				pw.println(word+"\t"+dict.get(word));
				System.out.println(word+" "+dict.get(word));
			}
			
			
			pw.close();



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}

}
