package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.textView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.isCheckable()){
            item.setChecked(true);
        }

        switch (item.getItemId()){
            case R.id.font_10: txt.setTextSize(10 * 2); break;
            case R.id.font_16: txt.setTextSize(16 * 2); break;
            case R.id.font_20: txt.setTextSize(20 * 2); break;
            case R.id.font_red: txt.setTextColor(Color.RED);break;
            case R.id.font_black: txt.setTextColor(Color.BLACK);break;
            case R.id.normal_menu: Toast.makeText(MenuActivity.this,"普通菜单你也点啊",Toast.LENGTH_SHORT).show();break;
        }
        return super.onOptionsItemSelected(item);
    }
}
