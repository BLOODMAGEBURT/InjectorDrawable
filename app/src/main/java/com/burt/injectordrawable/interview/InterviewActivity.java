package com.burt.injectordrawable.interview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.burt.injectordrawable.MainActivity;
import com.burt.injectordrawable.R;

public class InterviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

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
}
