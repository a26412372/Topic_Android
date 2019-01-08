package com.example.user.test_okhttpclient;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.content.Intent;


public class ParkingGrid extends AppCompatActivity {

    private Connect mConnect;
    private RequestBody RequestBody;
    private ArrayAdapter<String> listAdapter;
    private ListView list_view_parking;
    String[] strArray=new String[3];
    private String[] addList=new String[500];
    private TextView tv_parking_Add,tv_parking_Show;
    ViewPager mViewPager;
    JSONArray jsonArray;
    JSONObject jsonObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_grid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        init();
        GetIntentData();
        PostData();
    }

    private void init() {
        tv_parking_Add=(TextView)findViewById(R.id.tv_parking_Add);
        tv_parking_Show=(TextView)findViewById(R.id.tv_parking_Show);
        mViewPager = (ViewPager) findViewById(R.id.ADpager);
        list_view_parking=(ListView)findViewById(R.id.list_view_parking);
        mConnect = new Connect(this,DataHandler);
    }


    private void GetIntentData()
    {
        Intent intent=getIntent();
        strArray[0]=intent.getStringExtra("str0") ;
        strArray[1]=intent.getStringExtra("str1") ;
        strArray[2]=intent.getStringExtra("str2") ;
        tv_parking_Show.setText(strArray[0]+" "+strArray[1]+" "+strArray[2]);
    }

    private void PostData() {
        RequestBody = new FormBody.Builder()
                .add("Parking_city",strArray[0])
                .add("Parking_zone",strArray[1])
                .add("Parking_classification",strArray[2])
                .build();
        mConnect.setUrl(getString(R.string.tag1)+getString(R.string.url_ParkingGrid)) // "http://10.0.2.2:8080/topic/parking.php"
                .setRequestBody(RequestBody)
                .run1();
    }


    private Handler DataHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ParseJson(msg.getData().getString("resp"));
            Log.d("msg",msg.getData().getString("resp"));
        }
    };

    private void ParseJson(String jsonstr){
        try{
            jsonArray = new JSONArray(jsonstr);
            jsonObject = new JSONObject();

            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                String str=(jsonObject.getString("Parking_Address"));
                addList[i]=str;
                tv_parking_Add.setText(tv_parking_Add.getText()+"\n"+addList[i]);
            }
            String data=tv_parking_Add.getText().toString();
            StrSplit(data);
            tv_parking_Add.setText("");
        }
        catch(JSONException e){
            e.getStackTrace();
            Log.d("exception",e.getMessage());
        }
    }


    private void StrSplit(String result)
    {
        String[] datalist=result.split("\n");
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datalist);
        list_view_parking.setAdapter(listAdapter);
    }




}
