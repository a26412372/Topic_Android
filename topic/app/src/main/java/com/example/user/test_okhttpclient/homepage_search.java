package com.example.user.test_okhttpclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homepage_search extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_search);
        inti();
        restaurant();//餐廳
        aids();//機構
        assistive();//輔具
        ShelterWork();//工作查詢
        ParkingGrid();//停車格
    }

    private Button btn_parking,btn_ShelterWork,btn_assistive,btn_aids,btn_restaurant;
    private void inti()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        btn_parking=(Button) findViewById(R.id.btn_parking);
         btn_ShelterWork=(Button)findViewById(R.id.btn_ShelterWork);
         btn_assistive = (Button)findViewById(R.id.btn_assistive);
         btn_aids = (Button)findViewById(R.id.btn_aids);
         btn_restaurant = (Button)findViewById(R.id.btn_restaurant);
    }


    private void ParkingGrid(){

        btn_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(homepage_search.this,ParkingGridChoose.class));
            }
        });
    }


    private void ShelterWork(){

        btn_ShelterWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(homepage_search.this,ShelterWork.class));
            }
        });
    }

    private void assistive() {

        btn_assistive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent().setClass(homepage_search.this,AssistiveDeviceActivity.class));
            }
        });
    }


    private void aids() {

        btn_aids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent().setClass(homepage_search.this,aids.class));
            }
        });
    }



    private void restaurant() {

        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent().setClass(homepage_search.this,restaurant.class));
            }
        });
    }


}
