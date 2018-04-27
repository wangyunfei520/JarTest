package net.bupt.paylibrary.utils;

import android.content.Context;

import net.bupt.paylibrary.di.modules.RequestHelper;


/**
 * Utils初始化相关
 */
public class BuptPayManager {
    private volatile static BuptPayManager instance = null;

    public static BuptPayManager getInstance() {
        if (instance == null) {
            synchronized (BuptPayManager.class) {
                if (instance == null) {
                    instance = new BuptPayManager();
                }
            }
        }
        return instance;
    }

    private static Context context;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public  void init(Context context) {
        BuptPayManager.context = context.getApplicationContext();
        RequestHelper.getInstance().init();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    private BuptPayManager() {
    }

    public void pay(Context context) {
        BuptPayUtils.go(context);
    }
}
