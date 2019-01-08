package com.example.user.test_okhttpclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Connect {
    private ProgressDialog mProgressDialog;
    private OkHttpClient client;
    private Context mContext;
    private Handler mHandler;
    private RequestBody requestBody;
    private Request request;
    private Call call;
    private String mUrl;
    private boolean getpost = false;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public Connect(Context context, Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    public Connect setUrl(String url){
        mUrl = url;
        return this;
    }

    public Connect setRequestBody(RequestBody requestBody){
        this.requestBody = requestBody;
        return this;
    }

    public Connect setRequestBody(RequestBody requestBody, boolean getpost)
    {
        this.requestBody = requestBody;
        this.getpost = getpost;
        return this;
    }


        //-- run1 用來傳出資料 ---//
        public void run1(){
            try{
                client = new OkHttpClient.Builder()
                        .connectTimeout(10,TimeUnit.SECONDS)
                        .readTimeout(10,TimeUnit.SECONDS)
                        .build();
                request = new Request.Builder()
                        .url(mUrl)
                        .post(requestBody)
                        .build();
                call = client.newCall(request);

                mProgressDialog = ProgressDialog.show(mContext, "題示", "載入中...", true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        call.cancel();
                    }
                });

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mProgressDialog.dismiss();
                    Log.d("conn",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mProgressDialog.dismiss();
                    //Log.d("conn",response.body().string());
                    Bundle bundle = new Bundle();
                    bundle.putString("resp",response.body().string());
                    Message message = new Message();
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            });
        }catch (Exception e){
            Log.d("conn",e.getMessage());
            e.printStackTrace();;
        }
    }
    //---- run2 用來取得資料----//
    public void run2(){
        try{
            client = new OkHttpClient.Builder()
                    .connectTimeout(10,TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .build();

            request = new Request.Builder()
                    .url(mUrl)
                    .get()
                    .build();

            call = client.newCall(request);

            mProgressDialog = ProgressDialog.show(mContext, "題示", "載入中...", true, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    call.cancel();
                }
            });

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mProgressDialog.dismiss();
                    Log.d("conn",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mProgressDialog.dismiss();
                    //Log.d("conn",response.body().string());
                    Bundle bundle = new Bundle();
                    bundle.putString("resp",response.body().string());
                    Message message = new Message();
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            });
        }catch (Exception e){
            Log.d("conn",e.getMessage());
            e.printStackTrace();;
        }
    }


    public void run3(){
        try{
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .build();
            if(getpost) {
                request = new Request.Builder()
                        .url(mUrl)
                        .post(requestBody)
                        .build();
            }else{
                request = new Request.Builder()
                        .url(mUrl)
                        .get()
                        .build();
            }
            call = client.newCall(request);
            mProgressDialog = ProgressDialog.show(mContext, "提示", "載入中", true, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.dismiss();
                    call.cancel();
                }
            });
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mProgressDialog.dismiss();
                    Log.d("conn",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mProgressDialog.dismiss();
                    //Log.d("conn",response.body().string());
                    Bundle bundle = new Bundle();
                    bundle.putString("resp",response.body().string());
                    Message message = new Message();
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            });
        }
        catch (Exception e){
            Log.d("conn",e.getMessage());
            e.printStackTrace();
        }
    }

}