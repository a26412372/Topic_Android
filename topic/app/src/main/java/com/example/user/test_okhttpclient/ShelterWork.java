package com.example.user.test_okhttpclient;


import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ShelterWork extends AppCompatActivity {
    private Connect mConnect;
    private RequestBody RequestBody;
    private String[] tabTitle;
    TabLayout mTablayout;
    ViewPager mViewPager;
    ADPages mADPages[];
    private List<PageView> pageList;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_work);
        init();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        SpinnerWork();
        initListener();
    }

    private void init() {
        mTablayout = (TabLayout) findViewById(R.id.ADtabs);
        mViewPager = (ViewPager) findViewById(R.id.ADpager);
        pageList = new ArrayList<>();//建物件
        mADPages = new ADPages[500];//25筆資料
        mConnect = new Connect(this,DataHandler);
    }

    private void SpinnerWork()
    {
        final String[] CityArr = {
                "基隆市","嘉義市","臺北市","嘉義縣","新北市",
                "臺南市","桃園縣","高雄市","新竹市","屏東縣",
                "新竹縣","臺東縣","苗栗縣","花蓮縣","臺中市",
                "宜蘭縣","彰化縣","澎湖縣","南投縣","金門縣",
                "雲林縣"};
        final Spinner spinner1 = (Spinner)findViewById(R.id.SpinnerWork);
        ArrayAdapter<String> CityList = new ArrayAdapter<>(ShelterWork.this, android.R.layout.simple_spinner_dropdown_item, CityArr);
        spinner1.setAdapter(CityList);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(ShelterWork.this, "你選的是" + CityArr[position], Toast.LENGTH_SHORT).show();
                PostData(CityArr[position]);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void PostData(String strCity) {

        RequestBody = new FormBody.Builder()
                .add("Shelter_work_city",strCity)
                .build();
        mConnect.setUrl(getString(R.string.tag1)+getString(R.string.url_ShelterWork)) // "http://10.0.2.2:8080/topic/shelter_work.php"
                .setRequestBody(RequestBody)
                .run1();
    }
    /*private void GetData(){
        mConnect = new Connect(this,DataHandler);
        mConnect.setUrl("http://10.0.2.2:8080/topic/shelter_work.php")
                .run2();
    }*/

    private void initListener(){
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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
            mADPages[0]=null;
            mTablayout.removeAllTabs();
            pageList.clear();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                mTablayout.addTab(mTablayout.newTab().setText(jsonObject.getString("Shelter_work_name")));//選單名字
                mADPages[i] = new ADPages(ShelterWork.this);
                mADPages[i].setShelter_work_city(jsonObject.getString("Shelter_work_city"))
                        .setShelter_work_organizer(jsonObject.getString("Shelter_work_organizer"))
                        .setShelter_work_name(jsonObject.getString("Shelter_work_name"))
                        .setShelter_work_type(jsonObject.getString("Shelter_work_type"))
                        .setShelter_work_items(jsonObject.getString("Shelter_work_items"))
                        .setShelter_work_offer(jsonObject.getString("Shelter_work_offer"))
                        .setShelter_work_people(jsonObject.getString("Shelter_work_people"))
                        .setShelter_work_need(jsonObject.getString("Shelter_work_need"))
                        .setShelter_work_ContactPerson(jsonObject.getString("Shelter_work_ContactPerson"))
                        .setShelter_work_phone(jsonObject.getString("Shelter_work_phone"))
                        .setShelter_work_address(jsonObject.getString("Shelter_work_address"))
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
        private String shelter_work_city, shelter_work_organizer, shelter_work_name, shelter_work_type, shelter_work_items, shelter_work_offer, shelter_work_people, shelter_work_need, shelter_work_ContactPerson, shelter_work_phone, shelter_work_address;
        View view;
        public ADPages(Context context){
            super(context);
            view = LayoutInflater.from(context).inflate(R.layout.shelterwork_page_content,null);
        }

        public ADPages run(){
            TextView shelter_work_city_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_city);
            TextView shelter_work_organizer_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_organizer);
            TextView shelter_work_name_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_name);
            TextView shelter_work_type_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_type);
            TextView shelter_work_items_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_items);
            TextView shelter_work_offer_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_offer);
            TextView shelter_work_people_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_people);
            TextView shelter_work_need_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_need);
            TextView shelter_work_ContactPerson_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_ContactPerson);
            TextView shelter_work_phone_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_phone);
            TextView shelter_work_address_View = (TextView) view.findViewById(R.id.AD_page_shelter_work_address);

            shelter_work_city_View.setText(this.shelter_work_city);
            shelter_work_organizer_View.setText(this.shelter_work_organizer);
            shelter_work_name_View.setText(this.shelter_work_name);
            shelter_work_type_View.setText(this.shelter_work_type);
            shelter_work_items_View.setText(this.shelter_work_items);
            shelter_work_offer_View.setText(" 職位總數: "+this.shelter_work_offer);
            shelter_work_people_View.setText(" 在職人數: "+this.shelter_work_people);
            shelter_work_need_View.setText(" 缺額: "+this.shelter_work_need);
            shelter_work_ContactPerson_View.setText("聯絡人: "+this.shelter_work_ContactPerson);
            shelter_work_phone_View.setText(this.shelter_work_phone);
            shelter_work_address_View.setText(this.shelter_work_address);
            addView(view);
            return this;
        }

        @Override
        public void refreshView() {

        }

        public ADPages setShelter_work_city(String shelter_work_city){
            this.shelter_work_city = shelter_work_city;
            return this;
        }

        public ADPages setShelter_work_organizer(String shelter_work_organizer) {
            this.shelter_work_organizer = shelter_work_organizer;
            return this;
        }

        public ADPages setShelter_work_name(String shelter_work_name) {
            this.shelter_work_name = shelter_work_name;
            return this;
        }

        public ADPages setShelter_work_type(String shelter_work_type) {
            this.shelter_work_type = shelter_work_type;
            return this;
        }

        public ADPages setShelter_work_items(String shelter_work_items) {
            this.shelter_work_items = shelter_work_items;
            return this;
        }

        public ADPages setShelter_work_offer(String shelter_work_offer) {
            this.shelter_work_offer = shelter_work_offer;
            return this;
        }

        public ADPages setShelter_work_people(String shelter_work_people) {
            this.shelter_work_people = shelter_work_people;
            return this;
        }

        public ADPages setShelter_work_need(String shelter_work_need) {
            this.shelter_work_need = shelter_work_need;
            return this;
        }

        public ADPages setShelter_work_ContactPerson(String shelter_work_ContactPerson) {
            this.shelter_work_ContactPerson = shelter_work_ContactPerson;
            return this;
        }

        public ADPages setShelter_work_phone(String shelter_work_phone) {
            this.shelter_work_phone = shelter_work_phone;
            return this;
        }

        public ADPages setShelter_work_address(String shelter_work_address) {
            this.shelter_work_address = shelter_work_address;
            return this;
        }

    }
}
