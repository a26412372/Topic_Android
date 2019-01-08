package com.example.user.test_okhttpclient;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleActivity extends AppCompatActivity {
    private Connect mConnect;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Card> mArrayList;
    private ApplicationInfo mApplicationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initialize();
        GetData();
    }

    private void initialize(){
        mApplicationInfo = getApplicationInfo();

        mRecyclerView = (RecyclerView) findViewById(R.id.article_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mArrayList = new ArrayList<Card>();

    }


    private void GetData(){
        mConnect = new Connect(this,DataHandler);
        mConnect.setUrl(getString(R.string.tag2)+getString(R.string.url_ArticleActivity))// //"http://93f09180.ngrok.io/topic/Article.php"
                .run3();
    }

    private Handler DataHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ParseJson(msg.getData().getString("resp"));
            //Log.d("msg",msg.getData().getString("resp"));
        }
    };

    JSONArray jsonArray;
    JSONObject jsonObject;
    int image;
    String id;
    String title;
    String image_url;
    private void ParseJson(String jsonstr){
        try {
            jsonArray = new JSONArray(jsonstr);
            jsonObject = new JSONObject();
            for(int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                //Log.d("title[" + i + "]",jsonObject.getString("title"));
                //Log.d("image_url[" + i + "]",jsonObject.getString("image_url"));
                //Log.d("id[" + i + "]", jsonObject.getString("id"));

                id = jsonObject.getString("id");
                title = jsonObject.getString("title");
                image = getResources().getIdentifier(id,"drawable",mApplicationInfo.packageName);

                mArrayList.add(new Card(id,title,image));
            }
        }
        catch (JSONException e) {
            e.getStackTrace();
            Log.d("exception",e.getMessage());
        }
        mRecyclerAdapter = new RecyclerAdapter(this,mArrayList);
        mRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ArticleActivity.this,ArticleContentActivity.class);
                intent.putExtra("id", mArrayList.get(position).id);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}

