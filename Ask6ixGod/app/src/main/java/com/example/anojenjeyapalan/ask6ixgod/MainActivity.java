package com.example.anojenjeyapalan.ask6ixgod;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import io.indico.Indico;
import io.indico.enums.TextTag;
import io.indico.network.IndicoCallback;
import io.indico.results.IndicoResult;
import io.indico.utils.IndicoException;


public class MainActivity extends AppCompatActivity {

    private double sentimentValue;
    private EditText editText;
    private ImageButton button;
    private String inputString;
    private List<String> keyWords;
    private Map<String, Object> params;
    private List<String> textTags;
    //private List<MediaPlayer>
            //MediaPlayer mp;

    private List<String> lyric;
    private List<Double> sentiment;
    private List<String> key1;
    private List<String> key2;
    private List<String> key3;
    private List<String> tTag1;
    private List<String> tTag2;
    private List<String> tTag3;
    private List<String> tTag4;
    private List<String> tTag5;
    private List<String> song;
    private TextView lyricText;
    private TextView songText;
    private int [] score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(MainActivity.this, "bnK8SwHRBrGPDtMb5BpQtEtOlzAByBvB3WWjtGmX", "JYxhtAjB3k5IZ0K2yNPbrxlE71nij5UtuYfE4Cd0");

       

        sentiment = new ArrayList<Double>();
        lyric = new ArrayList<String>();
        key1 = new ArrayList<String>();
        key2 = new ArrayList<String>();
        key3 = new ArrayList<String>();
        tTag1 = new ArrayList<String>();
        tTag2 = new ArrayList<String>();
        tTag3 = new ArrayList<String>();
        tTag4 = new ArrayList<String>();
        tTag5 = new ArrayList<String>();
        song = new ArrayList<String>();

        keyWords = new ArrayList<String>();
        params = new HashMap<String, Object>();
        textTags = new ArrayList<String>();

        editText = (EditText) findViewById(R.id.editText);
        //button = (ImageButton) findViewById(R.id.button);
        lyricText = (TextView) findViewById(R.id.lyricText);
        songText = (TextView) findViewById(R.id.songText);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("dataBase");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {

//                    for (int i = 0; i < list.size(); i++) {
  //                      sentiment.add(list.get(i).getDouble("0"));
    //                }
                    for(ParseObject po: list){
                        sentiment.add(po.getDouble("Sentiment"));
                        lyric.add(po.getString("Lyrics"));
                        key1.add(po.getString("Keywords1"));
                        key2.add(po.getString("Keywords2"));
                        key3.add(po.getString("Keywords3"));
                        tTag1.add(po.getString("Tag1"));
                        tTag2.add(po.getString("Tag2"));
                        tTag3.add(po.getString("Tag3"));
                        tTag4.add(po.getString("Tag4"));
                        tTag5.add(po.getString("Tag5"));
                        song.add(po.getString("Song"));
                    }
                    //Toast.makeText(MainActivity.this, sentiment.get(0) + "", Toast.LENGTH_SHORT).show();

                } else {
                    e.printStackTrace();
                    // something went wrong
                }
            }});
}


    public void buttonClicked(View view) throws IOException, IndicoException {


        //mp.start();

        textTags.clear();
        keyWords.clear();
        inputString = editText.getText().toString();
        Indico.init(this, getString(R.string.api_key), null);

        Indico.keywords.predict(inputString, new IndicoCallback<IndicoResult>() {
            @Override
            public void handle(IndicoResult result) throws IndicoException {
                keyWords.addAll(result.getKeywords().keySet());
                //output();
                //Toast.makeText(MainActivity.this, keyWords + "", Toast.LENGTH_SHORT).show();
                //output();
                match();
            }
        });
        params.put("top_n", 5);
        Indico.textTags.predict(inputString, params, new IndicoCallback<IndicoResult>() {
            @Override
            public void handle(IndicoResult result) throws IndicoException {
                Set<TextTag> l = (result.getTextTags().keySet());
                for (TextTag t : l)
                    textTags.add(t.toString());
                //Toast.makeText(MainActivity.this, textTags + "", Toast.LENGTH_SHORT).show();
            }
        });

        Indico.sentiment.predict(inputString, new IndicoCallback<IndicoResult>() {
            @Override
            public void handle(IndicoResult result) throws IndicoException {
                //Toast.makeText(MainActivity.this, result.getSentiment().toString(), Toast.LENGTH_SHORT).show();
                sentimentValue = result.getSentiment();
                //Toast.makeText(MainActivity.this, "" + sentimentValue, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getScores(List<Integer> ins) {
        score = new int[tTag1.size()];
        for (int i : score)
            score[i] = 0;
        for (int i : ins){
            if (textTags.contains(tTag1.get(i))) {
                score[i] += 10 - textTags.indexOf(tTag1.get(i));
                //System.out.println(textTags.indexOf(tTag1.get(i)));
            }
            if (textTags.contains(tTag2.get(i))) {
                score[i] += 9 - textTags.indexOf(tTag2.get(i));
                //System.out.println(textTags.size());//indexOf(tTag2.get(i)));
            }
            if (textTags.contains(tTag3.get(i)))
                score[i] += 8 - textTags.indexOf(tTag3.get(i));

            if (textTags.contains(tTag4.get(i)))
                score[i] += 7 - textTags.indexOf(tTag4.get(i));

            if (textTags.contains(tTag5.get(i)))
                score[i] += 6 - textTags.indexOf(tTag5.get(i));


            if (!keyWords.isEmpty() && keyWords.contains(key1.get(i)))
                score[i] += 10 - keyWords.indexOf(key1.get(i));

            if (keyWords.size() > 1 && keyWords.contains(key2.get(i)))
                score[i] += 9 - keyWords.indexOf(key2.get(i));

            if (keyWords.size() > 2 && keyWords.contains(key3.get(i)))
                score[i] += 8 - keyWords.indexOf(key3.get(i));
        }
    }

    private void match() {

    Random generator = new Random();
    List<Integer> inspect = new ArrayList<>();
    for(int i = 0; i < sentiment.size(); i++) {
        if(Math.abs(sentimentValue - sentiment.get(i)) < .06 ) {
            inspect.add(i);

        }
    }
        getScores(inspect);
        for(int i: inspect)
           System.out.print(score[i] + " ");

//    //SORTING ALGORITHM
//    int index = 0;
//    for (int i = 0; i < score.length; i++) {
//        index = i;
//        for (int j = i + 1; j < score.length; j++) {
//            if (score[j] > score[index]) {
//                index = j;
//            }
//        }
//        int largerNumber = score[index];
//        score[index] = score[i];
//        score[i] = largerNumber;
//    }
//
//    //CHECK FOR MULTIPLE OF THE SAME TOTAL SIMILARITY SCORE
//    int index1 = 0;
//    int k = 1;
//    int counter1 = 0;
//    while((score[0] == score[k]) && k < score.length) {
//        counter1++;
//        k++;
//    }
    int largest = inspect.get(0);
    for(int i: inspect)
        if(score[i] > score[largest] || score[i] == score[largest] && (int)(Math.random() * 3) == 2)
            largest = i;

    //OUTPUT
    //System.out.println(index1);
    //Toast.makeText(MainActivity.this, "" + lyric.get(largest), Toast.LENGTH_LONG).show();
        Typeface type = Typeface.createFromAsset(getAssets(), "drake_font.ttf");
        songText.setText("-" + song.get(largest));
        songText.setTypeface(type);
        songText.setVisibility(View.VISIBLE);

        lyricText.setText(lyric.get(largest));
        lyricText.setVisibility(View.VISIBLE);
}



}
