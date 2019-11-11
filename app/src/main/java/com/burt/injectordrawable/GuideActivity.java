package com.burt.injectordrawable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.burt.injectordrawable.bean.PieData;
import com.burt.injectordrawable.view.PieView;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        PieView pieView = findViewById(R.id.pieView);
//        PieView pieView = new PieView(this);
        String[] names = {"sohu", "baidu", "bing", "yaho", "google"};
        float[] values = {20, 20, 30, 10, 20};
        ArrayList<PieData> datas = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {

            datas.add(new PieData(names[i], values[i]));
        }
        pieView.setData(datas);
        pieView.setStartAngle(30);
    }
}
