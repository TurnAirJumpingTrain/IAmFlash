# 期中实验_NotePad记事本应用
## 基础功能实现
### 1.添加时间戳
在NoteEditor类中实现dateToStr方法，获取并格式化编辑和新增笔记时的系统时间
#### 关键代码
```java
    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
```
#### 截图
![1](https://user-images.githubusercontent.com/113674466/202892318-027f1eeb-54ba-43ef-a60c-8dd486b9fb0b.png)
### 2.查询功能
通过textview接收关键词信息，定义关键词过滤规则并实时刷新界面
#### 关键代码
```java
          @Override
          protected FilterResults performFiltering(CharSequence charSequence) {
              FilterResults result = new FilterResults();
              List<Note> list;
              if (TextUtils.isEmpty(charSequence)) {
                  list = backList;
              } else {
                  list = new ArrayList<>();
                  for (Note notePad : backList) {
                      if (notePad.getContent().contains(charSequence)) {
                          list.add(notePad);
                      }
  
                  }
              }
              result.values = list; 
              result.count = list.size();
              return result;
          }
```
```java
    public void refreshListView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreference(this);
        DatabaseUtils op = new DatabaseUtils(context);
        op.open();
        if (notePadList.size() > 0) notePadList.clear();
        notePadList.addAll(op.getAllNotes());
        if (sharedPreferences.getBoolean("reverseSort", false)) sortNotes(notePadList, 2);
        else sortNotes(notePadList, 1);
        op.close();
        adapter.notifyDataSetChanged();
    }
```
#### 截图
![select1](https://user-images.githubusercontent.com/113674466/202892313-663d94ae-70f3-4419-b4f9-b7df4e975c71.png)
![select2](https://user-images.githubusercontent.com/113674466/202892314-c1f0113d-56c2-485c-b882-87b62e72d4ed.png)
## 附加功能实现
### 1.笔记分类
在NotePad中增加tag属性并展示在主界面中，设置监听器，触发时对已有数据项进行筛选并对界面刷新  
可以新增标签  
可以通过上方的“查看所有笔记”按钮返回
#### 关键代码
加载标签
```java
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        for(int i = 0; i < numList.size(); i++) Log.d("tag", numList.get(i).toString());
        Log.d("tag", "getView: " + numList.size());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        context.setTheme((sharedPreferences.getBoolean("nightMode", false)? R.style.NightTheme: R.style.DayTheme));
        View v = View.inflate(context, R.layout.tag_layout, null);
        TextView blank_tag = v.findViewById(R.id.blank_tag);
        TextView text_tag = v.findViewById(R.id.text_tag);
        ImageView delete_tag = v.findViewById(R.id.delete_tag);
        blank_tag.setText(numList.get(position).toString());
        text_tag.setText(tagList.get(position));
        return v;
    }
```
新增标签
```java
                add_tag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sharedPreferences.getString("tagListString","").split("_").length < 10) {
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
                                            else Toast.makeText(context, "重复标签", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                        else{
                            Toast.makeText(context, "标签超过10个", Toast.LENGTH_SHORT).show();
                        }
                    }
```
#### 截图
展示分类  
![8](https://user-images.githubusercontent.com/113674466/202892517-72183ecd-cfe5-4ec3-9ef8-e32c48023b2f.png)  
按分类查询笔记  
![tag](https://user-images.githubusercontent.com/113674466/202892317-5bf95a94-7240-456e-b9c3-413e8102bca8.png)  
新建分类  
![9](https://user-images.githubusercontent.com/113674466/202892549-58adedb4-4dea-4832-afc8-4429f80feb56.png)  
新建笔记时选择分类  
![11](https://user-images.githubusercontent.com/113674466/202892718-28bea858-c785-43f1-ad67-1d4f250dc396.png)

### 2.更换夜间模式
在style.xml中写好夜间模式和正常模式下的颜色搭配，通过设置监听器等方式切换  
正常模式
```xml
    <style name="DayTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="toolbarTheme">@style/ThemeOverlay.AppCompat.ActionBar</item>
        <item name="colorPrimary">@color/hg</item>
        <item name="android:textColorPrimary">@color/black</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="tvBackground">@color/sg</item>
        <item name="lvBackground">@color/dg</item>
        <item name="menu_search">@drawable/ic_search_black_24dp</item>
        <item name="menu_delete_all">@drawable/ic_delete_forever_black_24dp</item>
        <item name="menu_delete">@drawable/ic_delete_black_24dp</item>
        <item name="titleColor">@color/black</item>
        <item name="tvMainColor">@color/black</item>
        <item name="tvSubColor">@color/grey9</item>
        <item name="settingIcon">@drawable/ic_settings_black_24dp</item>
        <item name="spinnerArrow">@drawable/ic_arrow_drop_down_black_24dp</item>
        <item name="renewIcon">@drawable/ic_autorenew_black_24dp</item>
        <item name="removeTag">@drawable/ic_remove_black_24dp</item>
        <item name="addTag">@drawable/ic_add_black_24dp</item>
        <item name="addPlanTag">@drawable/ic_flag_black_24dp</item>
        <item name="addNewTag">@color/dg</item>
        <item name="colorControlActivated">@color/black</item>
        <item name="colorSwitchThumbNormal">@color/black</item>
    </style>
```
夜间模式
```xml
    <style name="NightTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="toolbarTheme">@style/ThemeOverlay.AppCompat.Dark.ActionBar</item>
        <item name="colorPrimary">@color/black</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="tvBackground">@color/black</item>
        <item name="lvBackground">@color/near_black</item>
        <item name="menu_search">@drawable/ic_search_white_24dp</item>
        <item name="menu_delete_all">@drawable/ic_delete_forever_white_24dp</item>
        <item name="menu_delete">@drawable/ic_delete_white_24dp</item>
        <item name="titleColor">@color/white</item>
        <item name="tvMainColor">@color/near_white</item>
        <item name="tvSubColor">@color/greyC</item>
        <item name="settingIcon">@drawable/ic_settings_white_24dp</item>
        <item name="spinnerArrow">@drawable/ic_arrow_drop_down_white_24dp</item>
        <item name="renewIcon">@drawable/ic_autorenew_white_24dp</item>
        <item name="removeTag">@drawable/ic_remove_white_24dp</item>
        <item name="addTag">@drawable/ic_add_white_24dp</item>
        <item name="addPlanTag">@drawable/ic_flag_white_24dp</item>
        <item name="addNewTag">@color/near_black</item>
        <item name="colorControlActivated">@color/white</item>
        <item name="colorSwitchThumbNormal">@color/white</item>
    </style>
```
#### 截图
![6](https://user-images.githubusercontent.com/113674466/202892308-620c0fe3-f3e9-4586-aa51-de668c3e8fe8.png)
![10](https://user-images.githubusercontent.com/113674466/202892641-f0241577-610e-47c8-ac3a-165df8afecb6.png)
### 3.按时间顺序排序
从数据库拿到NotePadList后先进行按时间大小的排序，再进行展示
#### 关键代码
格式化时间
```java
    public long dateStrToSec(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long secTime = format.parse(date).getTime();
        return secTime;
    }
```
按时间进行排序
```java
    public void sortNotes(List<NotePad> notePadList, final int mode) {
        Collections.sort(notePadList, new Comparator<NotePad>() {
            @Override
            public int compare(NotePad o1, NotePad o2) {
                try {
                    //时间逆序
                    if (mode == 1) {
                        return npLong(dateStrToSec(o2.getTime()) - dateStrToSec(o1.getTime()));
                    }
                    //时间顺序
                    else if (mode == 2) {
                        return npLong(dateStrToSec(o1.getTime()) - dateStrToSec(o2.getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
    }
```
#### 截图
切换界面  

效果图  
![aftertime](https://user-images.githubusercontent.com/113674466/202892311-ebe8d4b7-669c-44da-a4ae-dd311e047695.png)
![beforetime](https://user-images.githubusercontent.com/113674466/202892312-4ec6f429-e90c-4dbf-823a-060471658ff4.png)
