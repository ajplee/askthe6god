import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.indico.Indico;
import io.indico.api.text.TextTag;
import io.indico.api.utils.IndicoException;
import io.indico.api.results.BatchIndicoResult;

public class ApplicationMain{
	// single example
	List<Double> sentinal = new ArrayList<>();
	List<List<String>> textTags = new ArrayList<>();
	List<List<String>> keyWords = new ArrayList<>();
	static Indico indico = new Indico("682032997381c15cbce8b08bc02f0343");

	public static void main(String[] args){

		Scanner input = new Scanner(System.in);
		makeTheTags();
	}

	static void makeTheTags(){
		BufferedReader br;
		PrintWriter pw = null;
		String[] example = new String[21];
		int a = 0;
		String str;
		String song = "from time";
		try {
			pw= new PrintWriter(song + ".csv");
			br = new BufferedReader(new FileReader("C:\\Users\\sam\\Documents\\drake\\" + song + ".txt"));
			while((str = br.readLine()) != null && a < 25)
				example[a++] = str;
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	
		Object[] sentiment = null;
		List<Map<TextTag, Double>> tTags;
		List<String> tags = new ArrayList<String>();
		String ttag;
		List<Map<String, Double>> keyWords;
		List<String> words = new ArrayList<String>();

		BatchIndicoResult multiple = null;
		try {
		    multiple = indico.sentimentHQ.predict(example);
			sentiment = multiple.getSentimentHQ().toArray();
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("top_n", 5);
			multiple = indico.textTags.predict(example, params);
			tTags = multiple.getTextTags();
			for(Map<TextTag, Double> m: tTags){
				for(TextTag t: m.keySet())
					tags.add(t.toString());
			}
			
			multiple = indico.keywords.predict(example);
			keyWords = multiple.getKeywords();
			for(Map<String, Double> m: keyWords)
				words.addAll(m.keySet());
			
			
		} catch (IOException | IndicoException e) {
			e.printStackTrace();
		}
		
		pw.close();
	}
	
}
