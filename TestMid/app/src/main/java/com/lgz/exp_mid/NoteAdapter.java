package com.lgz.exp_mid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;

    private List<NotePad> backList;//用来备份原始数据
    private List<NotePad> notePadList;//这个数据是会改变的，所以要有个变量来备份一下原始数据
    private MyFilter mFilter;
    private List<String> tagList;

    public NoteAdapter(Context mContext, List<NotePad> notePadList, List<String> tagList) {
        this.mContext = mContext;
        this.notePadList = notePadList;
        backList = notePadList;
        this.tagList = tagList;
    }

    @Override
    public int getCount() {
        return notePadList.size();
    }

    @Override
    public Object getItem(int position) {
        return notePadList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);
        TextView tv_tag = (TextView)v.findViewById(R.id.tv_tag);
        //Set text for TextView
        String allText = notePadList.get(position).getContent();
        tv_content.setText(allText.split("\n")[0]);
        tv_time.setText(notePadList.get(position).getTime());
        tv_tag.setText(tagList.get(notePadList.get(position).getTag()-1));
        //Save note id to tag
        v.setTag(notePadList.get(position).getId());
        return v;
    }

    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        //performFiltering(CharSequence charSequence)中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<NotePad> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，显示所有的数据
                list = backList;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (NotePad notePad : backList) {
                    if (notePad.getContent().contains(charSequence)) {
                        list.add(notePad);
                    }

                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notePadList = (List<NotePad>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }

}
