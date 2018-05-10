package net.bupt.paylibrary.utils;

import android.content.Context;

import net.bupt.paylibrary.di.modules.RequestHelper;
import net.bupt.paylibrary.entity.PayEntity;


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


    /**
     * 初始化工具类
     *
     */
    public void init() {
        RequestHelper.getInstance().init();
    }

    private BuptPayManager() {
    }

    /**
     * @param cardno      ca卡号
     * @param prodcode    产品编号
     * @param description 描述
     * @param total       价格
     */
    public void pay(Context mContext,String cardno,
                    String prodcode, String description, String total) {
        BuptPayUtils.go(mContext, new PayEntity(cardno, prodcode, description, total));
    }
}
