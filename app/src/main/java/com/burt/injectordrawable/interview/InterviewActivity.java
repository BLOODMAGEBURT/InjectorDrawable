package com.burt.injectordrawable.interview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.burt.injectordrawable.MainActivity;
import com.burt.injectordrawable.R;

public class InterviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        // activity传值
        activityDemo();
        // fragment传值
        fragmentDemo();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });


    }

    private void activityDemo() {
        Intent intent = new Intent(InterviewActivity.this, MainActivity.class);
        // 新建用于封装数据的 bundle对象
        Bundle bundle = new Bundle();
        bundle.putString("name", "xubobo");
        bundle.putInt("age", 18);
        // 将bundle对象 嵌入 Intent中
        intent.putExtras(bundle);

        startActivity(intent);


        Intent intent1 = getIntent();
        intent1.getStringExtra("name");
        intent1.getIntExtra("age", 0);
    }

    private void fragmentDemo() {

        Bundle bundle = new Bundle();
        bundle.putString("key", "value is value");


        Fragment fragment = new BlankFragment();
        // 将fragment 与  bundle绑定
        fragment.setArguments(bundle);


    }

    // 定义一个public方法，给fragment传值

    public String getTitleDemo() {

        return "it's cool";
    }


}
