package topicTracker;

import uk.ac.wlv.sentistrength.SentiStrength;

public class TestSentimentSpanish {

	public TestSentimentSpanish() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		SentiStrength sentiStrength = new SentiStrength();
		String sentiParams[] = {"sentidata", "extra/SentiStrength/spanish/", "trinary"};
		sentiStrength.initialise(sentiParams);
		String opinions="@24HorasTVN: ¿Tsunami de nubes? - Sorprendente imagen muestra una niebla que envuelve a montaña. El VIDEO: http://t.co/PWFFQwFd3n-33.6409963 -70.7106008";
		String[] words=opinions.split(" ");
		
			String sentence = "";
			for (int i = 0; i < words.length; i++) {
				sentence += words[i];
				if (i < words.length - 1) {
					sentence += "+";
				}
			}
			
			System.out.println(sentence);

			String result = sentiStrength.computeSentimentScores(sentence);
		
			String[] values = result.split(" ");


			int pos = Integer.parseInt(values[0]);
			int neg = Integer.parseInt(values[1]);
			int neu = Integer.parseInt(values[2]);


			System.out.println(pos+" "+neg+" "+neu);
						
		}
	
		

	}


