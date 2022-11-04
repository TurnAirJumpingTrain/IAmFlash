package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.simple_items,new String[]{"header","personName"},new int[]{R.id.imageView,R.id.name});
        ListView list=findViewById(R.id.mylist);
        list.setAdapter(simpleAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast toast=Toast.makeText(SimpleAdapterActivity.this,names[i],Toast.LENGTH_SHORT);
                toast.show();
                for (int num=0;num<adapterView.getCount();num++){
                    View viewChildAt = adapterView.getChildAt(num);
                    viewChildAt.setBackgroundResource(R.color.white);
                }
                view.setBackgroundResource(R.color.purple_200);
            }
        });
    }
}