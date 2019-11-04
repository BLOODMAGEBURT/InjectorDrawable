package com.burt.injectordrawable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        ImageView injectorView = findViewById(R.id.injectorView);
//       ArrowDrawable drawable = ArrowDrawable.create(injectorView);
//       ArrowDrawable drawable = ArrowDrawable.create(injectorView,200,200);

       injectorView.setImageDrawable(InjectDrawable.create(injectorView));
//       drawable.reset();
    }
}
