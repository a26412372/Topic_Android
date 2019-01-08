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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssistiveDeviceActivity extends AppCompatActivity {
    private Connect mConnect;
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
        setContentView(R.layout.activity_assistive_device);
        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar
        mTablayout = (TabLayout) findViewById(R.id.ADtabs);
        mViewPager = (ViewPager) findViewById(R.id.ADpager);
        pageList = new ArrayList<>();//建物件
        mADPages = new ADPages[17];//17筆資料
        GetData();
        initListener();
    }

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

    private void GetData(){
        mConnect = new Connect(this,DataHandler);
        mConnect.setUrl(getString(R.string.tag1)+getString(R.string.url_AssistiveDeviceActivity)) // "http://10.0.2.2:8080/topic/AssistiveDevice.php"
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
                mTablayout.addTab(mTablayout.newTab().setText(jsonObject.getString("Subsidy_Project")));//選單名字
                mADPages[i] = new ADPages(AssistiveDeviceActivity.this);
                mADPages[i].setType(jsonObject.getString("type"))
                        .setSubsidy_project(jsonObject.getString("Subsidy_Project"))
                        .setObject1(jsonObject.getString("Object1"))
                        .setObject2(jsonObject.getString("Object2"))
                        .setObject3(jsonObject.getString("Object3"))
                        .setAPL(jsonObject.getInt("Assistance_Payments_Low-income"))
                        .setAPLAM(jsonObject.getInt("Assistance_Payments_Low_and_Middle-income"))
                        .setAPG(jsonObject.getInt("Assistance_Payments-General"))
                        .setSubsidy_content(jsonObject.getString("Subsidy_Content"))
                        .setOrganizer(jsonObject.getString("Organizer"))
                        .setTelephone(jsonObject.getString("Telephone"))
                        .setApply1(jsonObject.getString("Apply1"))
                        .setApply2(jsonObject.getString("Apply2"))
                        .setApplication_document1(jsonObject.getString("Application_Document1"))
                        .setApplication_document2(jsonObject.getString("Application_Document2"))
                        .setApplication_document3(jsonObject.getString("Application_Document3"))
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
        private String type,subsidy_project,object1,object2,object3,subsidy_content,organizer,telephone,apply1,apply2,application_document1,application_document2,application_document3;
        private int APL,APLAM,APG;
        View view;
        public ADPages(Context context){
            super(context);
            view = LayoutInflater.from(context).inflate(R.layout.assitive_page_content,null);
        }

        public ADPages run(){
            TextView type_View = (TextView) view.findViewById(R.id.AD_page_type);
            TextView subsidy_project_View = (TextView) view.findViewById(R.id.AD_page_subsidy_project);
            TextView object1_View = (TextView) view.findViewById(R.id.AD_page_object1);
            TextView object2_View = (TextView) view.findViewById(R.id.AD_page_object2);
            TextView object3_View = (TextView) view.findViewById(R.id.AD_page_object3);
            TextView APL_View = (TextView) view.findViewById(R.id.AD_page_APL);
            TextView APLAM_View = (TextView) view.findViewById(R.id.AD_page_APLAM);
            TextView APG_View = (TextView) view.findViewById(R.id.AD_page_APG);
            TextView subsidy_content_View = (TextView) view.findViewById(R.id.AD_page_subsidy_content);
            TextView organizer_View = (TextView) view.findViewById(R.id.AD_page_organizer);
            TextView telephone_View = (TextView) view.findViewById(R.id.AD_page_telephone);
            TextView apply1_View = (TextView) view.findViewById(R.id.AD_page_apply1);
            TextView apply2_View = (TextView) view.findViewById(R.id.AD_page_apply2);
            TextView application_document1_View = (TextView) view.findViewById(R.id.AD_page_application_document1);
            TextView application_document2_View = (TextView) view.findViewById(R.id.AD_page_application_document2);
            TextView application_document3_View = (TextView) view.findViewById(R.id.AD_page_application_document3);
            type_View.setText(this.type);
            subsidy_project_View.setText(this.subsidy_project);
            object1_View.setText(this.object1);
            object2_View.setText(this.object2);
            object3_View.setText(this.object3);
            APL_View.setText(String.valueOf("  低收入戶 : "+this.APL));
            APLAM_View.setText(String.valueOf("中低收入戶 : "+this.APLAM));
            APG_View.setText(String.valueOf("    一般戶 : "+this.APG));
            subsidy_content_View.setText(this.subsidy_content);
            organizer_View.setText(this.organizer);
            telephone_View.setText(this.telephone);
            apply1_View.setText(this.apply1);
            apply2_View.setText(this.apply2);
            application_document1_View.setText(this.application_document1);
            application_document2_View.setText(this.application_document2);
            application_document3_View.setText(this.application_document3);
            addView(view);
            return this;
        }

        @Override
        public void refreshView() {

        }

        public ADPages setType(String type){
            this.type = type;
            return this;
        }

        public ADPages setSubsidy_project(String subsidy_project) {
            this.subsidy_project = subsidy_project;
            return this;
        }

        public ADPages setObject1(String object1) {
            this.object1 = object1;
            return this;
        }

        public ADPages setObject2(String object2) {
            this.object2 = object2;
            return this;
        }

        public ADPages setObject3(String object3) {
            this.object3 = object3;
            return this;
        }

        public ADPages setAPL(int APL) {
            this.APL = APL;
            return this;
        }

        public ADPages setAPLAM(int APLAM) {
            this.APLAM = APLAM;
            return this;
        }

        public ADPages setAPG(int APG) {
            this.APG = APG;
            return this;
        }

        public ADPages setSubsidy_content(String subsidy_content) {
            this.subsidy_content = subsidy_content;
            return this;
        }

        public ADPages setOrganizer(String organizer) {
            this.organizer = organizer;
            return this;
        }

        public ADPages setTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public ADPages setApply1(String apply1) {
            this.apply1 = apply1;
            return this;
        }

        public ADPages setApply2(String apply2) {
            this.apply2 = apply2;
            return this;
        }

        public ADPages setApplication_document1(String application_document1) {
            this.application_document1 = application_document1;
            return this;
        }

        public ADPages setApplication_document2(String application_document2) {
            this.application_document2 = application_document2;
            return this;
        }

        public ADPages setApplication_document3(String application_document3) {
            this.application_document3 = application_document3;
            return this;
        }
    }
}
