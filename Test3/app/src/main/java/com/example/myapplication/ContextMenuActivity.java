package com.example.myapplication;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextMenuActivity extends AppCompatActivity {

    private String[] head=new String[]{"ONE","TWO","THREE","FOUR","FIVE","SIX","SEVEN","EIGHT","NINE","TEN"};

    private String[] name=new String[]{"宋哲凯一号","宋哲凯二号","宋哲凯三号","宋哲凯四号","宋哲凯五号","宋哲凯六号","宋哲凯七号","宋哲凯八号","宋哲凯九号","宋哲凯十号"};

    private ListView listView;

    private List<Integer> positionList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        listView=findViewById(R.id.mylist);
        registerForContextMenu(listView);
//        装配listview
        List<Map<String,Object>> listItems=new ArrayList<>();
        for (int i=0;i<head.length;i++){
            Map<String,Object> listitem=new HashMap<>();
            listitem.put("head",head[i]);
            listitem.put("name",name[i]);
            listItems.add(listitem);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.simple_items2,new String[]{"head","name"},new int[]{R.id.head,R.id.name});
        listView.setAdapter(simpleAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int num=0;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long chosenId, boolean checked) {
                int count = listView.getCount();
                if (checked == true) {
                    positionList.add(position);
                    for (int k=0;k<count;k++){
                        View childAt = listView.getChildAt(k);
                        childAt.setBackgroundResource(R.color.white);
                    }
                    for (Integer integer : positionList) {
                        View childAt = listView.getChildAt(integer);
                        childAt.setBackgroundResource(R.color.red);
                    }
                    num++;
                } else {
                    positionList.remove((Integer) position);
                    for (int k=0;k<count;k++){
                        View childAt = listView.getChildAt(k);
                        childAt.setBackgroundResource(R.color.white);
                    }
                    for (Integer integer : positionList) {
                        View childAt = listView.getChildAt(integer);
                        childAt.setBackgroundResource(R.color.red);
                    }
                    num--;
                }
                // 用TextView显示
                mode.setTitle("  " + num + " Selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.contextmenu_main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                num=0;
            }
        });
    }
}
