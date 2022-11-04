# 实验3_Android界面组件  

## Android ListView的用法  
### 关键代码部分  
```java
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
```
### 截图  
## 创建自定义布局的AlertDialog  
### 关键代码部分  
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(R.layout.alert);
        builder.show();
    }
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="72dp"
        android:background="#CAE623"
        android:gravity="center"
        android:text="ANDROID APP"
        android:textColor="@color/white"
        android:textSize="34sp"
        android:textStyle="bold" />

    <EditText
        android:id="@+id/editTextTextPersonName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:hint="Username"
        android:inputType="textPersonName" />

    <EditText
        android:id="@+id/editTextTextPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:hint="Password"
        android:inputType="textPassword" />
</LinearLayout>
```
### 截图  
## 使用XML定义菜单  
### 关键代码部分  
```java
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
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:id="@+id/font_size" android:title="字体大小" >
        <menu >
            <group >
                <item android:id="@+id/font_10" android:title="10px" />
                <item android:id="@+id/font_16" android:title="16px" />
                <item android:id="@+id/font_20" android:title="20px" />
            </group>
        </menu>
    </item>

    <item android:id="@+id/normal_menu" android:title="普通菜单项" />

    <item android:id="@+id/font_color" android:title="字体颜色" >
        <menu >
            <group >
                <item android:id="@+id/font_red" android:title="红色" />
                <item android:id="@+id/font_black" android:title="黑色" />
            </group>
        </menu>
    </item>
</menu>
```
### 截图  

## 创建上下文操作模式(ActionMode)的上下文菜单  
### 关键代码部分  
```java
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
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/menu_add"
        app:showAsAction="always"
        android:title="item_all"
        android:icon="@drawable/choose"/>

    <item
        android:id="@+id/menu_delete"
        app:showAsAction="always"
        android:title="item_delete"
        android:icon="@drawable/dustbin"/>

</menu>
```
### 截图  
