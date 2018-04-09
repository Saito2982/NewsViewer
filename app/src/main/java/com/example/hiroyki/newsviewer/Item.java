package com.example.hiroyki.newsviewer;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


// Item.java
public class Item {
    private String realTitele;
    private String tmpString;
    // 記事のタイトル
    private CharSequence mTitle;
    // 記事の本文
    private CharSequence mDescription;
    private String  mLink;
    private String mDate;

    private String regex="<img src=\"(.*?)\"";
    private Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public Item() {
        mTitle = "";
        mDescription = "";
        tmpString = "";
        mLink = "";
        mDate = "";
    }

    public void setRealTitle(String RealTitle) { realTitele = RealTitle;}

    public String getRealTitele() { return realTitele; }

    //記事の概要
    public void setDescription(CharSequence description) {
        mDescription = description;
    }

    public CharSequence getDescription() {
        return mDescription;
    }


    public void setLink(String link) {
        mLink = link;

        //tmpString = "http://i1.wp.com/d-navi004.com/wp-content/uploads/2014/11/MetroUI-Folder-OS-OS-Android-icon.png";
    }

    public String getLink(){return mLink;}

    //サムネイル設定
    public void setThumbnail(String thumbnail) {
        Matcher m = p.matcher(thumbnail);
        m.find();
        Log.v("Matcher m" , m.group(1));
        tmpString = m.group(1);
    }

    public String getThumbnail() {
        return tmpString;
    }

    //記事タイトル
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    //記事時間取得
    public  void setDate(String date){
        mDate = date;
        Log.v("TAG", String.valueOf(date));
    }

    public String getDate() { return mDate;}
}