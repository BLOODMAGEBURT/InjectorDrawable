package com.burt.injectordrawable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {

    private InjectDrawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        ImageView injectorView = findViewById(R.id.injectorView);


        drawable = InjectDrawable.create(injectorView);
        injectorView.setImageDrawable(drawable);
        SeekBar bar = findViewById(R.id.durationBar);

        SeekBar dividerBar = findViewById(R.id.dividerBar);

        Switch resultSwitch = findViewById(R.id.resultSwitch);

        drawable.setDividerProgress(0.4f);

        resultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    drawable.updateState(InjectDrawable.STATE_RUNNING);
                } else {
                    drawable.updateState(InjectDrawable.STATE_STILL);
                }

            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawable.setProgress(progress / 100F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dividerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawable.setDividerProgress(progress / 100F);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button toGuide = findViewById(R.id.toGuide);
        toGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GuideActivity.class));
            }
        });
    }
}
