package com.skysoul.skysoulsdk;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.skysoul.networkokhttplibrary.demo4encapsulate.SSConstantsInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MAcitivityJava extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doRequest();
        setContentView(R.layout.mact);
        findViewById(R.id.btnrequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });
    }
    public void get(View view) {
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url("http://www.jianshu.com/u/9df45b87cfdf").build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("sqc","onfail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.d("sqc","onfail${response?.code()}");
            }
        });
    }

    void doRequest(){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        HashMap<String,String> requestParamsMap = new HashMap<>();
        requestParamsMap.put("count",""+12);
        requestParamsMap.put("gender",""+1);
        StringBuffer strRequestUrlParm = new StringBuffer();
        for (String key : requestParamsMap.keySet()) {
            strRequestUrlParm.append("&");
            strRequestUrlParm.append(key);
            strRequestUrlParm.append("=");
            strRequestUrlParm.append(requestParamsMap.get(key));
        }
        if (strRequestUrlParm.length() > 0) {
            strRequestUrlParm.deleteCharAt(0);
        }
        String requestStr = strRequestUrlParm.toString();
        byte[] bytes=null;
        try {
             bytes = requestStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Request request = new Request.Builder().url(SSConstantsInfo.Resource.INSTANCE.getRandomCaricature())
                .post(RequestBody.create(mediaType,bytes))
                .header("Platform","android:3.0:mojipop")
                .addHeader("Lang","zh")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("sqc","onfail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("sqc","onfail${response?.code()}");
            }
        });

    }
}
