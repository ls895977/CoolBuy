package com.lykj.library_lykj.http;

import android.support.annotation.NonNull;

import com.lykj.library_lykj.common.Constant;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiHttp extends ApiRequest{
    private HttpHeaders headers = new HttpHeaders();
    private HttpParams params = new HttpParams();
    private ApiCallback callback;
    private ApiCallbackFile mCallbackFile;
    private ApiRequestResult result;

    private HashMap<String,Object> addUrl = new HashMap<>();
    public void putUrl(String key, Object val) {
        addUrl.put(key, val);
    }
    public void putHeader(String key, String val){
        headers.put(key, val);
    }

    public void put(String key, String val){
        params.put(key,val);
    }

    /**
     *   Activity销毁时，取消网络请求
     *   OkGo.getInstance().cancelTag(tag);
     *
     * @param url 地址
     * @param flag 取消网络请求
     * @param cb 回调
     */
    public void postToList(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;

        Post(url, flag, headers, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
                int status;
                String message;
                try {
                    JSONObject object = new JSONObject(s);
                    status = object.optInt("code");
                    message = object.optString("msg");
                } catch (JSONException e) {
                    callback.onError("-----json数据解析失败--", flag);
                    e.printStackTrace();
                    return;
                }

                result = ResultToListMap(s, status, message);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();

                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void getToList(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        url = transformUrl(url);
        Get(url, flag, headers, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
//                Logger.e("-----s--->"+s);

                int status;
                String message;
                try {
                    JSONObject object = new JSONObject(s);
                    status = object.optInt("code");
                    message = object.optString("msg");
                } catch (JSONException e) {
                    callback.onError("-----json数据解析失败--", flag);
                    e.printStackTrace();
                    return;
                }

                result = ResultToListMap(s, status, message);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();

                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void postToMap(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;

        Post(url, flag, headers, params, new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
                int status;
                String message;
                try {
                    JSONObject object = new JSONObject(s);
                    status = object.optInt("code");
                    message = object.optString("msg");
                } catch (JSONException e) {
                    callback.onError("-----json数据解析失败--", flag);
                    e.printStackTrace();
                    return;
                }

                result = ResultToMap(s, status, message);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void getToMap(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        url = transformUrl(url);

        Get(url, flag, headers, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
                int status;
                String message;
                try {
                    JSONObject object = new JSONObject(s);
                    status = object.optInt("code");
                    message = object.optString("msg");
                } catch (JSONException e) {
                    callback.onError("-----json数据解析失败--", flag);
                    e.printStackTrace();
                    return;
                }

                result = ResultToMap(s, status, message);


            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void postToString(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        Logger.e("-----params--->"+params.toString());

        Post(url, flag, headers, params, new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();

                String s = response.body();
//                Logger.e("------onSuccess-->"+s);
                callback.onSuccess(s, flag);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void getToString(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        url = transformUrl(url);
        Logger.e("------params--"+url);
        Get(url, flag, headers, params, new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                addUrl.clear();
                String s = response.body();
                Logger.e("-----onSuccess-----"+s);
                Logger.json(s);
                callback.onSuccess(s, flag);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                addUrl.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void putToString(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        url = transformUrl(url);
        Logger.e("------params--"+url);

        Put(url, flag, headers, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                addUrl.clear();
                String s = response.body();
                callback.onSuccess(s, flag);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                addUrl.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void deleteToString(String url, RequestFlag flag, ApiCallback cb){
        this.callback = cb;
        url = transformUrl(url);
        Logger.e("-----params---"+url);

        Delete(url, flag, headers, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                addUrl.clear();
                String s = response.body();
//                Logger.e("------onSuccess-->"+s);
                callback.onSuccess(s, flag);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                addUrl.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    @NonNull
    private String transformUrl(String url) {
        url += "?";
        for (Object o : addUrl.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            url += k + "=" + v + "&";
        }
        url=url.substring(0, url.length()-1);
        return url;
    }

    /**
     * 上传单个文件
     * @param url   地址
     * @param flag   表示用于请求的取消
     * @param key   表单上传时的key
     * @param file  需要上传的文件, 即表单上传时的value
     * @param cb    回调，上传成功或失败后返回的数据
     */
    public void postFile(String url, RequestFlag flag, String key, File file, ApiCallback cb){
        this.callback = cb;

        url = transformUrl(url);
        Logger.e("----params--->"+url);
        PostFile(url, flag, params, headers, key, file, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
                callback.onSuccess(s, flag);
            }
            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void postFilesWithToken(String url, RequestFlag flag, String key, List<File> files, ApiCallback cb){
        this.callback = cb;
        Logger.e("----params--->"+params.toString());

        PostFiles(url, flag, params, headers, key, files, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                headers.clear();
                params.clear();
                String s = response.body();
                callback.onSuccess(s, flag);
                Logger.e("--------callback-----_>"+s);
            }
            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                headers.clear();
                params.clear();
                try {
                    int code = response.code();
                    if (code != 200)
                        callback.onError("服务器连接失败!", flag);
                    Logger.e("----onError--body->", response.body());
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    callback.onError("网络错误！", flag);
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

    public void downFile(String url, RequestFlag flag, ApiCallbackFile cb){
        this.mCallbackFile = cb;

//        url += "?token="+ACache.get(context).getAsString("token");

//      FileCallback(String destFileDir, String destFileName):
//      可以额外指定文件的下载目录和下载完成后的文件名
        DownLoadFile(Get, url, flag, new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                mCallbackFile.onSuccess(response.body());

            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
               // Logger.e("------onSuccess-------"+progress);
                mCallbackFile.downloadProgress(progress.currentSize,progress.totalSize,progress.fraction,progress.speed);
            }


            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                try {
                    int code = response.code();
                    if (code != 200)
                        mCallbackFile.onError("服务器连接失败!");
                    Logger.e("----onError--code->", response.code()+"");
                } catch (NullPointerException e1) {
                    mCallbackFile.onError("网络错误！");
                    Logger.e("----onError--->","response.code()  返回null ");
                }
            }
        });
    }

}
