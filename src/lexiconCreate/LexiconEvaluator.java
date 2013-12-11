/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexiconCreate;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fbravo
 */
public class LexiconEvaluator {

    protected String path;
    protected Map<String, String> dict;

    public LexiconEvaluator(String file) {
        this.dict = new HashMap<String, String>();
        this.path = file;

    }

    public void processDict() {


        try {
            // first, we open the file
            Scanner sc = new Scanner(new File(this.path));
            sc.useDelimiter("\n");
            for (String line = sc.next(); sc.hasNext(); line = sc.next()) {
                String pair[] = line.split("\t");
                this.dict.put(pair[0], pair[1]);


            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LexiconEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Retorno la polaridad de la palabra
    public String retrieveValue(String word) {
        if (!this.dict.containsKey(word)) {
            return "not_found";
        } else {
            return this.dict.get(word);
        }


    }
    
    public Map<String,String> getDict(){
    	return this.dict;
    }


}
