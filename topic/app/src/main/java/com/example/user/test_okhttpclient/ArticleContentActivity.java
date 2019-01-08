package com.example.user.test_okhttpclient;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ArticleContentActivity extends AppCompatActivity {

    private Connect mConnect;
    private RequestBody mRequestBody;
    private TextView tvTitle,tvContent;
    private ImageView ivPhoto;
    private ApplicationInfo mApplicationInfo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initialize();
        PostData();
    }

    private void initialize(){
        mApplicationInfo = getApplicationInfo();

        tvTitle = (TextView) findViewById(R.id.tv_AC_Title);
        ivPhoto = (ImageView) findViewById(R.id.iv_AC_Photo);
        tvContent = (TextView) findViewById(R.id.tv_AC_Content);

        intent = getIntent();
    }

    private void PostData(){
        Log.d("image",intent.getStringExtra("id"));
        mRequestBody = new FormBody.Builder()
                .add("id",intent.getStringExtra("id"))
                .build();
        mConnect = new Connect(this,DataHandler);
        mConnect.setUrl(getString(R.string.tag2)+getString(R.string.url_ArticleContentActivity))// //"http://93f09180.ngrok.io/topic/ArticleContent.php"
                .setRequestBody(mRequestBody,true)
                .run3();
    }

    private Handler DataHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ParseJson(msg.getData().getString("resp"));
            Log.d("msg",msg.getData().getString("resp"));
        }
    };

    JSONArray jsonArray;
    JSONObject jsonObject;
    String title, content;
    int image;

    private void ParseJson(String jsonstr){
        try {
            jsonArray = new JSONArray(jsonstr);
            jsonObject = new JSONObject();
            //for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(0);
            Log.d("title[0]",jsonObject.getString("title"));
            Log.d("id[0]", jsonObject.getString("id"));
            title = jsonObject.getString("title");
            image = getResources().getIdentifier(jsonObject.getString("id"),"drawable",mApplicationInfo.packageName);
            content = jsonObject.getString("content");
            //}
        }
        catch (JSONException e) {
            e.getStackTrace();
            Log.d("exception",e.getMessage());
        }
        Log.d("title",title);
        Log.d("id", String.valueOf(image));
        Log.d("content", content);
        tvTitle.setText(title);
        ivPhoto.setImageResource(image);
        tvContent.setText(content);
    }
}
