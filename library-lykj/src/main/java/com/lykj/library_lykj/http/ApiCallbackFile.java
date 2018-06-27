package com.lykj.library_lykj.http;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface ApiCallbackFile {
    void onSuccess(Object resultData);
    void onError(String errors);
//    public void onGetCookie(String cookie){}
//    public void onBefore(BaseRequest request){}
//    public void onCacheSuccess(String s, Call call){}
//    public void onCacheError(Call call, Exception e){}
//    public void onAfter(String s, Exception e){}
//    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed){}
    void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed);
//    public void parseError(Call call, Exception e){}
}
