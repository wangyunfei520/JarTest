package net.bupt.paylibrary.utils;

import android.content.Context;

/**
 * 利用反射根据资源名字获取资源ID加载布局+设置图片
 */
public class MResource {
    public static int getIdByName(Context context, String className, String resName) {
        String packageName = context.getPackageName();
        int id = 0;
        try {
            Class r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (Class cls : classes) {
                if (cls.getName().split("\\$")[1].equals(className)) {
                    desireClass = cls;
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(resName).getInt(desireClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
