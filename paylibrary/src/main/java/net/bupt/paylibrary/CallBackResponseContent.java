package net.bupt.paylibrary;

/**
 * 回调请求返回的数据
 */
public interface CallBackResponseContent {
    /**
     * 请求成功
     */
    void getResponseContent(String result);

    /**
     * 请求失败
     */
    void getFailContent(String result);
}
