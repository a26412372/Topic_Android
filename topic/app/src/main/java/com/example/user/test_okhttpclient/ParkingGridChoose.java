package com.example.user.test_okhttpclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import android.view.View;

public class ParkingGridChoose extends AppCompatActivity {
    String[] strArray=new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_grid_choose);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        submit();
        spinner1();
    }

    private void submit()
    {
        Button submit = (Button)findViewById(R.id.submit_parkingGrid);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ParkingGridChoose.this,ParkingGrid.class);
                intent.putExtra("str0",strArray[0]);
                intent.putExtra("str1",strArray[1]);
                intent.putExtra("str2",strArray[2]);
                startActivity(intent);
            }
        });
    }

    private void spinner1()
    {
        final String[] CityArr = {"新北市", "臺北市", "臺南市"};
        final Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> CityList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, CityArr);
        spinner1.setAdapter(CityList);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id)
            {
                spinner2(position1);
                strArray[0]=CityArr[position1];
                    spinner3(position1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void spinner2(int city) {
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final String[] NewTaipei = {
                "板橋區", "三重區", "中和區", "永和區", "新莊區", "新店區", "土城區", "蘆洲區", "樹林區", "汐止區", "鶯歌區", "三峽區", "淡水區", "瑞芳區",
                "五股區", "泰山區", "林口區", "深坑區", "石碇區", "坪林區", "三芝區", "石門區", "八里區", "平溪區", "雙溪區", "貢寮區", "金山區", "萬里區", "烏來區"};
        final String[] Taipei = {
                "北投", "士林", "大同", "中山", "松山", "內湖",
                "萬華", "中正", "大安", "信義", "南港", "文山"};

        final String[] Tainan = {"中西區", "北區", "南區", "安南區", "安平區", "東區"};

        if (city == 0)
        {
            ArrayAdapter<String> TownList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, NewTaipei);
            spinner2.setAdapter(TownList);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    strArray[1]=NewTaipei[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        }
        else if (city == 1)
            {
                ArrayAdapter<String> TownList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                spinner2.setAdapter(TownList);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        strArray[1]=Taipei[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }
        else
            {
                ArrayAdapter<String> TownList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                spinner2.setAdapter(TownList);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        strArray[1]=Tainan[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }


    }


    private void spinner3(int x)
    {
        final String[] Vehicle1 = {"汽車","機車"};
        final String[] Vehicle2 = {"汽車"};

        final Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        if(x!=2)
        {
            ArrayAdapter<String> VehicleList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, Vehicle1);
            spinner3.setAdapter(VehicleList);
        }
        else
        {
            ArrayAdapter<String> VehicleList = new ArrayAdapter<>(ParkingGridChoose.this, android.R.layout.simple_spinner_dropdown_item, Vehicle2);
            spinner3.setAdapter(VehicleList);
        }

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position3, long id)
            {
                strArray[2]=Vehicle1[position3];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
