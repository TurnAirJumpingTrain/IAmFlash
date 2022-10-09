# 实验1_Android开发基础
## 实验内容
### 注册GitHub账号，安装Git工具
• 课程官方GitHub：https://github.com/fjnu-cse  
• Git安装：https://git-scm.com/
### 创建Android工程并同步至GitHub
• 参考1：命令行方式，推荐
http://blog.csdn.net/fjnu_se/article/details/66472625  
• 参考2：Android Studio同步（不推荐）
http://blog.csdn.net/fjnu_se/article/details/56683934  
• Android工程的忽略文件
• https://github.com/github/gitignore/blob/master/Android.gitignore  
## 实验关键代码  
    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.636" />

    <EditText
        android:id="@+id/editTextDate"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:ems="10"
        android:inputType="date"
        android:minHeight="48dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.38"
        tools:ignore="SpeakableTextPresentCheck" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="我是闪电"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.475"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.219" />
## 实验结果
![pic1](https://user-images.githubusercontent.com/113674466/194739499-69eb6f45-1cb7-4c27-b5b0-20839e990510.PNG)
