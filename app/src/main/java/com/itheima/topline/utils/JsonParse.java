package com.itheima.topline.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.topline.bean.ConstellationBean;
import com.itheima.topline.bean.NewsBean;
import com.itheima.topline.bean.PythonBean;
import com.itheima.topline.bean.VideoBean;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParse {
    private static JsonParse instance;
    private JsonParse() {
    }
    public static JsonParse getInstance() {
        if (instance == null) {
            instance = new JsonParse();
        }
        return instance;
    }
    public List<NewsBean> getAdList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<NewsBean>>() {
        }.getType();
        //把获取到的信息集合存到adList中
        List<NewsBean> adList = gson.fromJson(json, listType);
        return adList;
    }
    public List<NewsBean> getNewsList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<NewsBean>>() {
        }.getType();
        //把获取到的信息集合存到newsList中
        List<NewsBean> newsList = gson.fromJson(json, listType);
        return newsList;
    }
    public List<PythonBean> getPythonList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<PythonBean>>() {}.getType();
        //把获取到的信息集合存到pythonList中
        List<PythonBean> pythonList = gson.fromJson(json, listType);
        return pythonList;
    }
   public List<VideoBean> getVideoList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<VideoBean>>() {
        }.getType();
        //把获取到的信息集合存到videoList中
        List<VideoBean> videoList = gson.fromJson(json, listType);
        return videoList;
    }
    public List<ConstellationBean> getConstellaList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<ConstellationBean>>() {
        }.getType();
        //把获取到的信息集合存到constellaList中
        List<ConstellationBean> constellaList = gson.fromJson(json, listType);
        return constellaList;
    }

}
