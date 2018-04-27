package com.wyf.jartest;

import android.app.Application;

import net.bupt.paylibrary.utils.BuptPayManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BuptPayManager.getInstance().init(this);
    }
}
