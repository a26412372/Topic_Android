package com.example.user.test_okhttpclient;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class restaurant extends AppCompatActivity {
    private Connect mConnect;
    private String[] tabTitle;
    TabLayout mTablayout;
    ViewPager mViewPager;
    ADPages mADPages[];

    private List<PageView> pageList;
    JSONArray jsonArray;
    JSONObject jsonObject;
    private int Flag_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        init();
        submit();
    }
    private void submit()
    {
        final Button btn_restaurant_go=(Button)findViewById(R.id.btn_restaurant_go);
        btn_restaurant_go.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String x=mADPages[Flag_restaurant].GetRestaurant_Latitude();
                String y=mADPages[Flag_restaurant].GetRestaurant_Longitude();

                Intent intent=new Intent(restaurant.this,MapsActivity.class);
                intent.putExtra("x",x);
                intent.putExtra("y",y);
                startActivity(intent);
            }
        });

    }

    private void init() {
        mTablayout = (TabLayout) findViewById(R.id.ADtabs);
        mViewPager = (ViewPager) findViewById(R.id.ADpager);
        pageList = new ArrayList<>();//建物件
        mADPages = new ADPages[500];//

        GetData();
        initListener();
    }

    private void initListener(){
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                Flag_restaurant=tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }

    private void GetData(){
        mConnect = new Connect(this,DataHandler);
        mConnect.setUrl(getString(R.string.tag1)+getString(R.string.url_restaurant)) //"http://10.0.2.2:8080/topic/restaurant.php"
                .run2();
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
                mTablayout.addTab(mTablayout.newTab().setText(jsonObject.getString("Restaurant_Name")));//選單名字
                mADPages[i] = new ADPages(restaurant.this);
                mADPages[i].setRestaurant_Name(jsonObject.getString("Restaurant_Name"))
                        .setRestaurant_Type(jsonObject.getString("Restaurant_Type"))
                        .setRestaurant_Telephone(jsonObject.getString("Restaurant_Telephone"))
                        .setRestaurant_City(jsonObject.getString("Restaurant_City"))
                        .setRestaurant_District(jsonObject.getString("Restaurant_District"))
                        .setRestaurant_Address(jsonObject.getString("Restaurant_Address"))
                        .setRestaurant_Latitude(jsonObject.getString("Restaurant_Latitude"))
                        .setRestaurant_Longitude(jsonObject.getString("Restaurant_Longitude"))
                        .run();
                pageList.add(mADPages[i]);
            }
            mViewPager.setAdapter(new CustomPagerAdapter(pageList));
        }
        catch(JSONException e){
            e.getStackTrace();
            Log.d("exception",e.getMessage());
        }
    }

    public class ADPages extends PageView{
        private String restaurant_Name ,restaurant_Type ,restaurant_Telephone ,restaurant_City ,restaurant_District ,restaurant_Address ,restaurant_Latitude ,restaurant_Longitude;
        View view;
        public ADPages(Context context){
            super(context);
            view = LayoutInflater.from(context).inflate(R.layout.restaurant_page_content,null);
        }

        public ADPages run(){
            TextView restaurant_Name_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Name);
            TextView restaurant_Type_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Type);
            TextView restaurant_Telephone_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Telephone);
            TextView restaurant_City_View = (TextView) view.findViewById(R.id.AD_page_restaurant_City);
            TextView restaurant_District_View = (TextView) view.findViewById(R.id.AD_page_restaurant_District);
            TextView restaurant_Address_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Address);
            TextView restaurant_Latitude_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Latitude);
            TextView restaurant_Longitude_View = (TextView) view.findViewById(R.id.AD_page_restaurant_Longitude);

            restaurant_Name_View.setText(this.restaurant_Name);
            restaurant_Type_View.setText(this.restaurant_Type);
            restaurant_Telephone_View.setText(this.restaurant_Telephone);
            restaurant_City_View.setText(this.restaurant_City);
            restaurant_District_View.setText(this.restaurant_District);
            restaurant_Address_View.setText(this.restaurant_Address);
            restaurant_Latitude_View.setText(this.restaurant_Latitude);
            restaurant_Longitude_View.setText(this.restaurant_Longitude);
            addView(view);
            return this;
        }

        @Override
        public void refreshView() {

        }

        public ADPages setRestaurant_Name(String restaurant_Name){
            this.restaurant_Name = restaurant_Name;
            return this;
        }

        public ADPages setRestaurant_Type(String restaurant_Type) {
            this.restaurant_Type = restaurant_Type;
            return this;
        }

        public ADPages setRestaurant_Telephone(String restaurant_Telephone) {
            this.restaurant_Telephone = restaurant_Telephone;
            return this;
        }

        public ADPages setRestaurant_City(String restaurant_City) {
            this.restaurant_City = restaurant_City;
            return this;
        }

        public ADPages setRestaurant_District(String restaurant_District) {
            this.restaurant_District = restaurant_District;
            return this;
        }

        public ADPages setRestaurant_Address(String restaurant_Address) {
            this.restaurant_Address = restaurant_Address;
            return this;
        }

        public ADPages setRestaurant_Latitude(String restaurant_Latitude) {
            this.restaurant_Latitude = restaurant_Latitude;
            return this;
        }

        public ADPages setRestaurant_Longitude(String restaurant_Longitude) {
            this.restaurant_Longitude = restaurant_Longitude;
            return this;
        }
        public String GetRestaurant_Latitude() {
            return this.restaurant_Latitude;
        }
        public String GetRestaurant_Longitude()
        {
            return this.restaurant_Longitude;
        }



    }
}
