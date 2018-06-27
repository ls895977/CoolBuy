package com.lykj.library_lykj.http;

import android.graphics.Bitmap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *可根据具体需求添加方法
 * https://github.com/jeasonlzy/okhttp-OkGo
 *
 */
public class ApiRequest {

    public static final int POST = 1;
    public static final int Get = 2;

    /**
     * 普通Post,无缓存请求,直接上传Json类型的文本
     *
     *  默认会携带以下请求头，请不要手动修改，okgo也不支持自己修改
     *  Content-Type: application/json;charset=utf-8
     */
    void Post(String url, Object tag, HttpHeaders headers, String httpParams, StringCallback callback){
        OkGo.<String>post(url)
//                .params(httpParams)
                .tag(tag)// 请求的 tag, 主要用于取消对应的请求
                .upJson(httpParams)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                .headers(headers)
//                .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),params.toString()))
//                .cacheKey("")// 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    void Post(String url, Object tag, HttpHeaders headers, HttpParams httpParams, StringCallback callback){
        OkGo.<String>post(url)
                .params(httpParams)
                .headers(headers)
                .tag(tag)// 请求的 tag, 主要用于取消对应的请求
//                .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),params.toString()))
                .execute(callback);
    }

    void Get(String url, Object tag, HttpHeaders headers, HttpParams httpParams, StringCallback callback){
        OkGo.<String>get(url)
                .headers(headers)
                .params(httpParams)
                .tag(tag)
                .execute(callback);
    }

    void Put(String url, Object tag, HttpHeaders headers, HttpParams httpParams, StringCallback callback){
        OkGo.<String>put(url)
                .headers(headers)
                .params(httpParams)
                .tag(tag)
                .execute(callback);
    }

    void Delete(String url, Object tag, HttpHeaders headers, HttpParams httpParams, StringCallback callback){
        OkGo.<String>delete(url)
                .headers(headers)
                .params(httpParams)
                .tag(tag)
                .execute(callback);
    }

    /**
     * 图片下载
     *
     * callback中的bitmap 即为返回的图片数据
     */
    void DownLoadBitmap(int mode, String url, Object tag, BitmapCallback callback){
        if (mode == POST)
            OkGo.<Bitmap>post(url)
                    .tag(tag)
                    .execute(callback);
        else
            OkGo.<Bitmap>get(url)
                    .tag(tag)
                    .execute(callback);
    }

    /**
     * 文件下载
     *
     FileCallback具有三个重载的构造方法,分别是:
     1.FileCallback():空参构造
     2.FileCallback(String destFileName):可以额外指定文件下载完成后的文件名
     3.FileCallback(String destFileDir, String destFileName):可以额外指定文件的下载目录和下载完成后的文件名

     文件目录如果不指定,默认下载的目录为 sdcard/download/,文件名如果不指定,则按照以下规则命名:
     1.首先检查用户是否传入了文件名,如果传入,将以用户传入的文件名命名
     2.如果没有传入,那么将会检查服务端返回的响应头是否含有Content-Disposition=attachment;
     filename=FileName.txt该种形式的响应头,如果有,则按照该响应头中指定的文件名命名文件,如FileName.txt
     3.如果上述响应头不存在,则检查下载的文件url,例如:http://image.baidu.com/abc.jpg,那么将会自动以abc.jpg命名文件
     4.如果url也把文件名解析不出来,那么最终将以nofilename命名文件
     */
    void DownLoadFile(int mode, String url, Object tag, FileCallback callback){
        if (mode == POST)
            OkGo.<File>post(url)
                    .tag(tag)
                    .execute(callback);
        else
            OkGo.<File>get(url)
                    .tag(tag)
                    .execute(callback);
    }

    /**
     * 上传图片或者文件
     * 上传文件支持文件与参数一起同时上传，也支持一个key上传多个文件，以下方式可以任选
     */
    void PostFile(String url, Object tag, HttpParams httpParams, HttpHeaders headerParams, String key, File file, StringCallback callback){
        OkGo.<String>post(url)
                .tag(tag)//
                .headers(headerParams)
//                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params(httpParams)        // 这里可以上传参数
                .params(key, file)   // 可以添加文件上传
//                .params("file2", new File("filepath2"))     // 支持多文件同时添加上传
//                .addFileParams("key", List<File> files) // 这里支持一个key传多个文件
                .execute(callback);
    }

    void PostFiles(String url, Object tag, HttpParams httpParams, HttpHeaders headerParams,String key, List<File> files, StringCallback callback){
        OkGo.<String>post(url)
                .tag(tag)
                .params(httpParams)
                .headers(headerParams)
                .addFileParams(key, files)
                .execute(callback);
    }

    /**
     * 查看url所对应的cookie
     *一般手动取出cookie的目的只是交给 webview 等等，非必要情况不要自己操作
     */
    void PostWithCookie(String url, Object tag, HttpHeaders headers, HttpParams httpParams, StringCallback callback){

        OkGo.<String>post(url)
                .tag(tag)
                .params(httpParams)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                .headers(headers)
                .execute(callback);
    }

    ApiRequestResult ResultToMap(String responseString, int status, String message) {
//        Debug.e("--调试--ResultToMap-->"+responseString);
        ApiRequestResult result = new ApiRequestResult();
        result.setJsonString(responseString);
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result.setStatus(status);

            if (result.getStatus() == 200) {

                HashMap<String, String> map = new HashMap<String, String>();
                if (jsonObject.has("data")) {
                    jsonObject = jsonObject.optJSONObject("data");

                    Iterator<?> it = jsonObject.keys();
                    String key = "";
                    String val = "";
                    while (it.hasNext()) {//遍历JSONObject
                        key = (String) it.next().toString();
                        val = jsonObject.optString(key);
//                        Debug.e("info", key + "--->" + val);
                        map.put(key, val);
                    }
                }
                result.setMapObject(map);
                return result;
            } else {
                // error.
                result.setErrors(message);
                return result;
            }

        } catch (JSONException e) {
            result.setErrors("----ApiRequest--map--json数据解析失败----");
            return result;
        }
    }

    ApiRequestResult ResultToListMap(String responseString, int status, String message) {
        ApiRequestResult result = new ApiRequestResult();
        result.setJsonString(responseString);
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result.setStatus(status);

            if (result.getStatus() == 200) {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    Iterator<?> it = jsonObject.keys();
                    String key = "";
                    String val = "";
                    while (it.hasNext()) {//遍历JSONObject
                        key = (String) it.next().toString();
                        val = jsonObject.optString(key);
                        map.put(key, val);
                    }
                    data.add(map);
                }
                result.setListMapObject(data);
                return result;
            } else {
                // error.
                result.setErrors(message);
                return result;
            }

        } catch (JSONException e) {
            result.setErrors("----ApiRequest--list--json数据解析失败----");
            return result;
        }
    }

    /**
     * 请求标识符
     */
    public enum RequestFlag{
        FLAG1, FLAG2, FLAG3, FLAG4, FLAG5
    }
}
