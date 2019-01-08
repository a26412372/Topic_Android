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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class aids extends AppCompatActivity {
    private Connect mConnect;
    private RequestBody RequestBody;
    private String[] tabTitle;

    private List<PageView> pageList;
    private ArrayAdapter<String> listAdapter;
    TabLayout mTablayout;
    ViewPager mViewPager;
    ADPages mADPages[];
    JSONArray jsonArray;
    JSONObject jsonObject;
    private ListView list_view_aids;
    private  String[] aids_list={"基隆市政府","嘉義市政府","臺中市政府","高雄市政府"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aids);
        init();
        initListener();
        list();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar
        mTablayout = (TabLayout) findViewById(R.id.ADtabs);
        mViewPager = (ViewPager) findViewById(R.id.ADpager);
        pageList = new ArrayList<>();//建物件
        mADPages = new ADPages[11];//11筆資料
        mConnect = new Connect(this,DataHandler);
        PostData(1);
    }

    private void list()
    {
        list_view_aids=(ListView)findViewById(R.id.list_view_aids);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aids_list);
        list_view_aids.setAdapter(listAdapter);
        list_view_aids.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String organ=aids_list[pos];
                Toast.makeText(getApplicationContext(),   aids_list[pos]+"，左右滑動查看", Toast.LENGTH_SHORT).show();
                PostData(pos);
            }
        });
    }

    private void PostData(int list) {
                String organ=aids_list[list];
                RequestBody = new FormBody.Builder()
                        .add("organ",organ)
                        .build();
                mConnect.setUrl(getString(R.string.tag1)+getString(R.string.url_aids)) //"http://10.0.2.2:8080/topic/aids.php"
                        .setRequestBody(RequestBody)
                        .run1();
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
            for(int i=0;i<jsonArray.length();i++) {
                jsonObject = jsonArray.getJSONObject(i);

                mTablayout.addTab(mTablayout.newTab().setText(jsonObject.getString("Aids_unit")));//選單名字

                mADPages[i] = new ADPages(aids.this);
                mADPages[i].setAids_nature(jsonObject.getString("Aids_nature"))
                        .setAids_organ(jsonObject.getString("Aids_organ"))
                        .setAids_unit(jsonObject.getString("Aids_unit"))
                        .setAids_processing_unit(jsonObject.getString("Aids_processing_unit"))
                        .setAids_phone(jsonObject.getString("Aids_phone"))
                        .setAids_fax(jsonObject.getString("Aids_fax"))
                        .setAids_address(jsonObject.getString("Aids_address"))
                        .setAids_X(jsonObject.getString("Aids_X"))
                        .setAids_Y(jsonObject.getString("Aids_Y"))
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
        private String aids_nature, aids_organ,aids_unit,aids_processing_unit,aids_phone,aids_fax,aids_address,aids_X,aids_Y;
        View view;
        public ADPages(Context context){
            super(context);
            view = LayoutInflater.from(context).inflate(R.layout.aids_page_content,null);
        }

        public ADPages run(){
            TextView aids_nature_View = (TextView) view.findViewById(R.id.AD_page_aids_nature);
            TextView aids_organ_View = (TextView) view.findViewById(R.id.AD_page_aids_organ);
            TextView aids_unit_View = (TextView) view.findViewById(R.id.AD_page_aids_unit);
            TextView aids_processing_unit_View = (TextView) view.findViewById(R.id.AD_page_aids_processing_unit);
            TextView aids_phone_View = (TextView) view.findViewById(R.id.AD_page_aids_phone);
            TextView aids_fax_View = (TextView) view.findViewById(R.id.AD_page_aids_fax);
            TextView aids_address_View = (TextView) view.findViewById(R.id.AD_page_aids_address);
            TextView aids_X_View = (TextView) view.findViewById(R.id.AD_page_aids_X);
            TextView aids_Y_View = (TextView) view.findViewById(R.id.AD_page_aids_Y);

            aids_nature_View.setText(this.aids_nature);
            aids_organ_View.setText(this. aids_organ);
            aids_unit_View.setText(this.aids_unit);
            aids_processing_unit_View.setText(this.aids_processing_unit);
            aids_phone_View.setText(" 電話: "+this.aids_phone);
            aids_fax_View.setText (" 傳真: "+this.aids_fax);
            aids_address_View.setText(this.aids_address);
            //aids_X_View.setText(this.aids_X);
            //aids_Y_View.setText(this.aids_Y);
            addView(view);
            return this;
        }

        @Override
        public void refreshView() {

        }

        public ADPages setAids_nature(String aids_nature){
            this.aids_nature = aids_nature;
            return this;
        }

        public ADPages setAids_organ(String aids_organ) {
            this.aids_organ = aids_organ;
            return this;
        }

        public ADPages setAids_unit(String aids_unit) {
            this.aids_unit = aids_unit;
            return this;
        }

        public ADPages setAids_processing_unit(String aids_processing_unit) {
            this.aids_processing_unit = aids_processing_unit;
            return this;
        }

        public ADPages setAids_phone(String aids_phone) {
            this.aids_phone = aids_phone;
            return this;
        }

        public ADPages setAids_fax(String aids_fax) {
            this.aids_fax = aids_fax;
            return this;
        }

        public ADPages setAids_address(String aids_address) {
            this.aids_address = aids_address;
            return this;
        }

        public ADPages setAids_X(String aids_X) {
            this.aids_X = aids_X;
            return this;
        }

        public ADPages setAids_Y(String aids_Y) {
            this.aids_Y = aids_Y;
            return this;
        }


    }
}
