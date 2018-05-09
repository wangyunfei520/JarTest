package com.wyf.jartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.bupt.paylibrary.utils.BuptPayManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BuptPayManager.getInstance().pay(this,"123321","123",
                "test222","0.01");
    }
}
