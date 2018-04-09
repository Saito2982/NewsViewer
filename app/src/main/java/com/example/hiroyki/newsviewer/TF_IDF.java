package com.example.hiroyki.newsviewer;


import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohamed Guendouz
 */
public class TF_IDF {
    private List<Token> tokens;
    private List<Token> token;
    private double _sum = 0.0;
    private double _ave = 0.0;
    public static ArrayList user_View = new ArrayList<String>();

    public TF_IDF(ArrayList user_View, String str) {
        // このトークナイザーで解析する
        Tokenizer tokenizer = Tokenizer.builder().build();

        List<List<Token>> tokensList = new ArrayList<List<Token>>();

        // トークンに分割
        for (int i = 0; i < user_View.size(); i=i+1){
            tokens = tokenizer.tokenize(str);
            tokensList.add(tokens);
        }

        token = tokenizer.tokenize(String.valueOf(user_View.get(0)));

        // インスタンス生成
        MoTfIdf mti = new MoTfIdf(tokensList, token);

        // TF-IDF値の導出
        List<MoTfIdf.TermAndTfIdf> tati = mti.calc();

        // 表示
        for(MoTfIdf.TermAndTfIdf tmp : tati) {
            Log.v("kuromoji","****************************");
            Log.v("kuromoji","Term: " + tmp.getTerm());
            Log.v("kuromoji","TF: " + tmp.getTf());
            Log.v("kuromoji","IDF: " + tmp.getIdf());
            Log.v("kuromoji","TF-IDF: " + tmp.getTfIdf());
            _sum += tmp.getTfIdf();
        }
        Log.v("kuromoji", "sum:" + _sum);
        _ave = _sum / tati.size();
        Log.v("kuromoji", "ave:" + _ave);
    }
    public double getTfidf() {
        return _ave;
    }

}