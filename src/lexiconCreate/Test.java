package lexiconCreate;

import java.util.regex.Pattern;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
		
	}
	static public void main(String args[]){
	int a=3;
	int b=43534545;
	double c=a/b;
	System.out.println(c);
	
	
	String url1="http://www.google.cl";
	String url2="https://www.lala.la";
	String url3="www.hola.com";
	String lala="www.google.cl";
	
	Pattern p=Pattern.compile("http");
	
	
	System.out.println("hola mundo".matches("http.*"));
	
	System.out.println("Hoooollaaaa a todoooos los weoneeees pacooos culiados"
            .replaceAll("([aeiou])\\1+","$1"));
	
	
	
	
	if(lala.matches("http.*|www\\..*")){
		System.out.println("hola");
	}
	
	if("asdasd".matches("@.*")){
		System.out.println("user");
	}
	
	
		
	}

}
