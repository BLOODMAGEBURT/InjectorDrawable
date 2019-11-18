package com.burt.injectordrawable.interview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.burt.injectordrawable.R;
/*
*测试Service使用的Activity
* */
public class MyServiceActivity extends AppCompatActivity implements ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);

        init();

    }

    private void init() {
        Intent intent = new Intent(this, MyService.class);


        // 传入intent和ServiceConnection
        bindService(intent, this, BIND_AUTO_CREATE);

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        MyService myService = ((MyService.MyBinder)iBinder).getService();

        // 可以调用service中的public方法
        myService.downLoad();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
