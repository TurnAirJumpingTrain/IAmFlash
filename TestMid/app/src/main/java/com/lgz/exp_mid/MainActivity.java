package com.lgz.exp_mid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {

    private DatabaseHelper dbHelper;

    private FloatingActionButton fab;
    private ListView lv;
    private ListView lv_plan;
    private LinearLayout lv_layout;
    private LinearLayout lv_plan_layout;

    private Context context = this;
    private NoteAdapter adapter;
    private List<NotePad> notePadList = new ArrayList<NotePad>();
    private TextView mEmptyView;

    private Toolbar myToolbar;

    private PopupWindow popupWindow; // 左侧弹出菜单
    private PopupWindow popupCover; // 菜单蒙版
    private LayoutInflater layoutInflater;
    private RelativeLayout main;
    private ViewGroup customView;
    private ViewGroup coverView;
    private WindowManager wm;
    private DisplayMetrics metrics;
    private TagAdapter tagAdapter;

    private TextView setting_text;
    private ImageView setting_image;
    private ListView lv_tag;
    private TextView add_tag;

    private BroadcastReceiver myReceiver;


    private SharedPreferences sharedPreferences;
    private Button content_button;

    private AlarmManager alarmManager;

    String[] list_String = {"before one month", "before three months", "before six months", "before one year"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        initView();

        if (super.isNightMode())
            myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_menu_white_24dp));
        else myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_menu_black_24dp)); // 三道杠

        myToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpWindow();
            }
        });

//        a
        content_button=findViewById(R.id.content_button);
        content_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtils databaseUtils =new DatabaseUtils(context);
                databaseUtils.open();
                List<NotePad> allNotePads = databaseUtils.getAllNotes();
                allNotePads.sort((notePad1, notePad2) -> notePad1.getTime().compareTo(notePad2.getTime()));
                List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_"));
                adapter = new NoteAdapter(getApplicationContext(), allNotePads,tagList);
                refreshListView();
                lv.setAdapter(adapter);
                databaseUtils.close();
            }
        });
    }

    private void showPopUpWindow() {
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        popupCover = new PopupWindow(coverView, width, height, false);
        popupWindow = new PopupWindow(customView, (int) (width * 0.7), (height), true);
        if (isNightMode()) popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupCover.setAnimationStyle(R.style.AnimationCover);


        //display the popup window
        findViewById(R.id.main_layout).post(new Runnable() {//等待main_layout加载完，再show popupwindow
            @Override
            public void run() {
                popupCover.showAtLocation(main, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.showAtLocation(main, Gravity.NO_GRAVITY, 0, 0);

                setting_text = customView.findViewById(R.id.setting_settings_text);
                setting_image = customView.findViewById(R.id.setting_settings_image);
                lv_tag = customView.findViewById(R.id.lv_tag);
                add_tag = customView.findViewById(R.id.add_tag);

                add_tag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sharedPreferences.getString("tagListString","").split("_").length < 8) {
                            final EditText et = new EditText(context);
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("输入标签名")
                                    .setView(et)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_")); //获取tags

                                            String name = et.getText().toString();
                                            if (!tagList.contains(name)) {
                                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                                String oldTagListString = sharedPreferences.getString("tagListString", null);
                                                String newTagListString = oldTagListString + "_" + name;
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("tagListString", newTagListString);
                                                editor.commit();
                                                refreshTagList();
                                            }
                                            else Toast.makeText(context, "重复标签!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                        else{
                            Toast.makeText(context, "自定义的标签够多了！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_")); //获取tags
                tagAdapter = new TagAdapter(context, tagList, numOfTagNotes(tagList));
                lv_tag.setAdapter(tagAdapter);

                lv_tag.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_")); //获取tags
                        int tag = position + 1;
                        List<NotePad> temp = new ArrayList<>();
                        for (int i = 0; i < notePadList.size(); i++) {
                            if (notePadList.get(i).getTag() == tag) {
                                NotePad notePad = notePadList.get(i);
                                temp.add(notePad);
                            }
                        }

                        NoteAdapter tempAdapter = new NoteAdapter(context, temp,tagList);
                        lv.setAdapter(tempAdapter);
                        myToolbar.setTitle(tagList.get(position));
                        popupWindow.dismiss();
                        Log.d(TAG, position + "");
                    }
                });

                lv_tag.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (position > 4) {
                            resetTagsX(parent);
                            float length = getResources().getDimensionPixelSize(R.dimen.distance);
                            TextView blank = view.findViewById(R.id.blank_tag);
                            blank.animate().translationX(length).setDuration(300).start();
                            TextView text = view.findViewById(R.id.text_tag);
                            text.animate().translationX(length).setDuration(300).start();
                            ImageView del = view.findViewById(R.id.delete_tag);
                            del.setVisibility(View.VISIBLE);
                            del.animate().translationX(length).setDuration(300).start();

                            del.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setMessage("All related notes will be tagged as \"no tag\" !")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int tag = position + 1;
                                                    for (int i = 0; i < notePadList.size(); i++) {
                                                        //被删除tag的对应notes tag = 1
                                                        NotePad temp = notePadList.get(i);
                                                        if (temp.getTag() == tag) {
                                                            temp.setTag(1);
                                                            DatabaseUtils op = new DatabaseUtils(context);
                                                            op.open();
                                                            op.updateNote(temp);
                                                            op.close();
                                                        }
                                                    }
                                                    List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_")); //获取tags
                                                    if(tag + 1 < tagList.size()) {
                                                        for (int j = tag + 1; j < tagList.size() + 1; j++) {
                                                            //大于被删除的tag的所有tag减一
                                                            for (int i = 0; i < notePadList.size(); i++) {
                                                                NotePad temp = notePadList.get(i);
                                                                if (temp.getTag() == j) {
                                                                    temp.setTag(j - 1);
                                                                    DatabaseUtils op = new DatabaseUtils(context);
                                                                    op.open();
                                                                    op.updateNote(temp);
                                                                    op.close();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    //edit the preference
                                                    List<String> newTagList = new ArrayList<>();
                                                    newTagList.addAll(tagList);
                                                    newTagList.remove(position);
                                                    String newTagListString = TextUtils.join("_", newTagList);
                                                    Log.d(TAG, "onClick: " + newTagListString);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("tagListString", newTagListString);
                                                    editor.commit();

                                                    refreshTagList();
                                                }
                                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                                }
                            });

                            return true;
                        }
                        return false;
                    }
                });


                setting_text.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
                        overridePendingTransition(R.anim.in_lefttoright, R.anim.no);

                    }
                });
                setting_image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
                        overridePendingTransition(R.anim.in_lefttoright, R.anim.no);

                    }
                });


                coverView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popupCover.dismiss();
                    }
                });
            }
        });

    }

    private void refreshTagList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_")); //获取tags
        tagAdapter = new TagAdapter(context, tagList, numOfTagNotes(tagList));
        lv_tag.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
    }

    private void resetTagsX(AdapterView<?> parent) {
        for (int i = 5; i < parent.getCount(); i++) {
            View view = parent.getChildAt(i);
            if (view.findViewById(R.id.delete_tag).getVisibility() == View.VISIBLE) {
                float length = 0;
                TextView blank = view.findViewById(R.id.blank_tag);
                blank.animate().translationX(length).setDuration(300).start();
                TextView text = view.findViewById(R.id.text_tag);
                text.animate().translationX(length).setDuration(300).start();
                ImageView del = view.findViewById(R.id.delete_tag);
                del.setVisibility(GONE);
                del.animate().translationX(length).setDuration(300).start();
            }
        }
    }

    @Override
    protected void needRefresh() {
        setNightMode();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("opMode", 10);
        startActivity(intent);
        overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
        if (popupWindow.isShowing()) popupWindow.dismiss();
        finish();
    }

    public void initView() {

        initPrefs();

        fab = findViewById(R.id.fab);

        lv = findViewById(R.id.lv);
        lv_plan = findViewById(R.id.lv_plan);
        lv_layout = findViewById(R.id.lv_layout);
        lv_plan_layout = findViewById(R.id.lv_plan_layout);
        content_button = findViewById(R.id.content_button);
        myToolbar = findViewById(R.id.my_toolbar);
        refreshLvVisibility();

        mEmptyView = findViewById(R.id.emptyView); // search page

        List<String> tagList = Arrays.asList(sharedPreferences.getString("tagListString", null).split("_"));
        adapter = new NoteAdapter(getApplicationContext(), notePadList,tagList);

        refreshListView();
        lv.setAdapter(adapter);
        lv.setEmptyView(mEmptyView); // connect empty textview with listview
        boolean temp = sharedPreferences.getBoolean("content_button", false);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteEditor.class);
                intent.putExtra("mode", 4);     // MODE of 'new note'
                startActivityForResult(intent, 1);      //collect data from edit
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);

            }
        });

        lv.setOnItemClickListener(this);
        lv_plan.setOnItemClickListener(this);

        lv.setOnItemLongClickListener(this);
        lv_plan.setOnItemLongClickListener(this);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置toolbar取代actionbar
        initPopupView();
    }

    private void refreshLvVisibility() {        //决定应该现实notes还是plans
        boolean temp = sharedPreferences.getBoolean("content_switch", false);
        lv_layout.setVisibility(View.VISIBLE);
        lv_plan_layout.setVisibility(GONE);
        myToolbar.setTitle("所有笔记");
    }

    public void initPopupView() {
        //instantiate the popup.xml layout file
        layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = (ViewGroup) layoutInflater.inflate(R.layout.setting_layout, null);
        coverView = (ViewGroup) layoutInflater.inflate(R.layout.setting_cover, null);

        main = findViewById(R.id.main_layout);
        //instantiate popup window
        wm = getWindowManager();
        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

    }

    private void initPrefs() {
        //initialize all useful SharedPreferences for the first time the app runs

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("nightMode")) {
            editor.putBoolean("nightMode", false);
            editor.commit();
        }
        if (!sharedPreferences.contains("reverseSort")) {
            editor.putBoolean("reverseSort", false);
            editor.commit();
        }
        if (!sharedPreferences.contains("fabColor")) {
            editor.putInt("fabColor", -500041);
            editor.commit();
        }
        if (!sharedPreferences.contains("tagListString")) {
            String s = "no tag_life_study_work_play";
            editor.putString("tagListString", s);
            editor.commit();
        }
        if(!sharedPreferences.contains("fabPlanColor")){
            editor.putInt("fabPlanColor", -500041);
            editor.commit();
        }
        if(!sharedPreferences.contains("noteTitle")){
            editor.putBoolean("noteTitle", true);
            editor.commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //search setting
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View view = findViewById(R.id.menu_clear);

                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Delete all notes");
                            builder.setIcon(R.drawable.ic_error_outline_black_24dp);
                            builder.setItems(list_String, new DialogInterface.OnClickListener() {//列表对话框；
                                @Override
                                public void onClick(DialogInterface dialog, final int which) {//根据这里which值，即可以指定是点击哪一个Item；
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setMessage("Do you want to delete all notes" + " " + list_String[which] + "? ")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int a) {
                                                    Log.d(TAG, "onClick: " + which);
                                                    removeSelectItems(which, 1);
                                                    refreshListView();
                                                }

                                                //根据模式与时长删除对顶的计划s/笔记s
                                                private void removeSelectItems(int which, int mode) {
                                                    int monthNum = 0;
                                                    switch (which){
                                                        case 0:
                                                            monthNum = 1;
                                                            break;
                                                        case 1:
                                                            monthNum = 3;
                                                            break;
                                                        case 2:
                                                            monthNum = 6;
                                                            break;
                                                        case 3:
                                                            monthNum = 12;
                                                            break;
                                                    }
                                                    Calendar rightNow = Calendar.getInstance();
                                                    rightNow.add(Calendar.MONTH,-monthNum);//日期加3个月
                                                    Date selectDate = rightNow.getTime();
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    String selectDateStr = simpleDateFormat.format(selectDate);
                                                    Log.d(TAG, "removeSelectItems: " + selectDateStr);
                                                    switch(mode){
                                                        case 1: //notes
                                                            dbHelper = new DatabaseHelper(context);
                                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                            Cursor cursor = db.rawQuery("select * from notes" ,null);
                                                            while(cursor.moveToNext()){
                                                                if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME)).compareTo(selectDateStr) < 0){
                                                                    db.delete("notes", DatabaseHelper.ID + "=?", new String[]{Long.toString(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ID)))});
                                                                }
                                                            }
                                                            db.execSQL("update sqlite_sequence set seq=0 where name='notes'"); //reset id to 1
                                                            refreshListView();
                                                            break;
                                                    }
                                                }
                                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });
                }
            }
        });


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Delete All Notes ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new DatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("notes", null, null);//delete data in table NOTES
                        db.execSQL("update sqlite_sequence set seq=0 where name='notes'"); //reset id to 1
                        refreshListView();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
        return super.onOptionsItemSelected(item);
    }

    //刷新listview
    public void refreshListView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //initialize CRUD
        DatabaseUtils op = new DatabaseUtils(context);
        op.open();

        // set adapter
        if (notePadList.size() > 0) notePadList.clear();
        notePadList.addAll(op.getAllNotes());
        if (sharedPreferences.getBoolean("reverseSort", false)) sortNotes(notePadList, 2);
        else sortNotes(notePadList, 1);
        op.close();
        adapter.notifyDataSetChanged();

    }

    //click item in listView
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                NotePad curNotePad = (NotePad) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, NoteEditor.class);
                intent.putExtra("content", curNotePad.getContent());
                intent.putExtra("id", curNotePad.getId());
                intent.putExtra("time", curNotePad.getTime());
                intent.putExtra("mode", 3);     // MODE of 'click to edit'
                intent.putExtra("tag", curNotePad.getTag());
                startActivityForResult(intent, 1);      //collect data from edit
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
                break;
        }
    }

    // react to startActivityForResult and collect data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        int returnMode;
        long note_Id;
        returnMode = data.getExtras().getInt("mode", -1);
        note_Id = data.getExtras().getLong("id", 0);
        if (returnMode == 1) {  //update current note

            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            NotePad newNotePad = new NotePad(content, time, tag);
            newNotePad.setId(note_Id);
            DatabaseUtils op = new DatabaseUtils(context);
            op.open();
            op.updateNote(newNotePad);
            op.close();

        } else if (returnMode == 2) {  //delete current note
            NotePad curNotePad = new NotePad();
            curNotePad.setId(note_Id);
            DatabaseUtils op = new DatabaseUtils(context);
            op.open();
            op.removeNote(curNotePad);
            op.close();
        } else if (returnMode == 0) {  // create new note
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            NotePad newNotePad = new NotePad(content, time, tag);
            DatabaseUtils op = new DatabaseUtils(context);
            op.open();
            op.addNote(newNotePad);
            op.close();

        } else {
        }
        refreshListView();
    }

    //longclick item in listView
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv:
                final NotePad notePad = notePadList.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseUtils op = new DatabaseUtils(context);
                                op.open();
                                op.removeNote(notePad);
                                op.close();
                                refreshListView();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return true;
    }

    //按模式时间排序笔记
    public void sortNotes(List<NotePad> notePadList, final int mode) {
        Collections.sort(notePadList, new Comparator<NotePad>() {
            @Override
            public int compare(NotePad o1, NotePad o2) {
                try {
                    if (mode == 1) {
                        return npLong(dateStrToSec(o2.getTime()) - dateStrToSec(o1.getTime()));
                    }
                    else if (mode == 2) {//reverseSort
                        return npLong(dateStrToSec(o1.getTime()) - dateStrToSec(o2.getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
    }


    //格式转换 string -> milliseconds
    public long dateStrToSec(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long secTime = format.parse(date).getTime();
        return secTime;
    }

    //统计不同标签的笔记数
    public List<Integer> numOfTagNotes(List<String> noteStringList){
        Integer[] numbers = new Integer[noteStringList.size()];
        for(int i = 0; i < numbers.length; i++) numbers[i] = 0;
        for(int i = 0; i < notePadList.size(); i++){
            numbers[notePadList.get(i).getTag() - 1] ++;
        }
        return Arrays.asList(numbers);
    }

    //turn long into 1, 0, -1
    public int npLong(Long l) {
        if (l > 0) return 1;
        else if (l < 0) return -1;
        else return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent!=null && intent.getIntExtra("mode", 0) == 1){
            refreshLvVisibility();
        }
    }

}
