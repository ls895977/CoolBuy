package com.lykj.library_lykj.http;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface ApiCallback{
    void onSuccess(Object resultData, ApiRequest.RequestFlag flag);
    void onError(String errors, ApiRequest.RequestFlag flag);
//    public void onGetCookie(String cookie){}
//    public void onBefore(BaseRequest request){}
//    public void onCacheSuccess(String s, Call call){}
//    public void onCacheError(Call call, Exception e){}
//    public void onAfter(String s, Exception e){}
//    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed){}
//    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed){}
//    public void parseError(Call call, Exception e){}
}
