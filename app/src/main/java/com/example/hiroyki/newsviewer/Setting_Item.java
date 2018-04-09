package com.example.hiroyki.newsviewer;

import java.util.ArrayList;

/**
 * Created by hiroyki on 2018/02/13.
 */

public class Setting_Item {
    private ArrayList data;

    //データを格納
    public void setData(ArrayList data){
        this.data=data;
    }

    //データを返す
    public ArrayList getData(){
        return this.data;
    }
}
