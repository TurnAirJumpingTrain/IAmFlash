package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapterActivity extends AppCompatActivity {

    private String[] names=new String[]{"Lion","Tiger","Monkey","Dog","Cat","Elephant"};
    private int[] imgs=new int[]{R.drawable.lion,R.drawable.tiger,R.drawable.monkey,R.drawable.dog,R.drawable.cat,R.drawable.elephant};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        List<Map<String,Object>> listItems=new ArrayList<>();
        for(int i = 0 ;i < names.length;i++){
            Map<String,Object> listItem=new HashMap<>();
            listItem.put("header",imgs[i]);
            listItem.put("personName",names[i]);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.simple_items,new String[]{"header","personName"},new )
    }
}