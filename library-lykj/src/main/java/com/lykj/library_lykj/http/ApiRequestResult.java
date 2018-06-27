package com.lykj.library_lykj.http;

import java.util.HashMap;
import java.util.List;


public class ApiRequestResult {

    // API返回结果
    private int status;

    // API返回对象
    private Object object;

    // API返回字符串
    private String ret;

    // API返回MAP类型
    private HashMap<String,String> mapObject;

    // API返回LIST类型
    private List<HashMap<String,String>> listMapObject;

    // API返回源JSON
    private String jsonString;

    // API返回错误码
    private String errors;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public HashMap<String, String> getMapObject() {
        return mapObject;
    }

    public void setMapObject(HashMap<String, String> mapObject) {
        this.mapObject = mapObject;
    }

    public List<HashMap<String, String>> getListMapObject() {
        return listMapObject;
    }

    public void setListMapObject(List<HashMap<String, String>> listMapObject) {
        this.listMapObject = listMapObject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}
