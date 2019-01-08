package com.example.user.test_okhttpclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;

import java.io.File;
import java.util.Random;



public class homepage extends AppCompatActivity {
    private TextView tv_homepage;
    private Button btn_search,btn_navigation,btn_article;
    private ImageView img_homepage;
    private int picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        inti();
        ClearSharepre_Ferences();//清除資料
        Intent intent = new Intent(homepage.this,MainActivity.class);
        startActivityForResult(intent,1);

    }


    Intent intent = getIntent();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("LoginCheck","checking");
                LoginCheck();
    }

    private void inti()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        Random ran = new Random();
        img_homepage= (ImageView) findViewById(R.id.img_homepage);
        btn_navigation = (Button)findViewById(R.id.btn_navigation);
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_article = (Button)findViewById(R.id.btn_article);
        tv_homepage=(TextView)findViewById(R.id.tv_homepage);
        navigation();//導航
        article();//文章
        search();
        int[] ImageArray=new int[]{R.drawable.image1,R.drawable.image7,R.drawable.image9,};

        picture=ran.nextInt(3);
        img_homepage.setImageResource(ImageArray[picture]);

    }

    private void LoginCheck()
    {
        String user_account=getSharedPreferences("USERDATA",MODE_PRIVATE)
                .getString("ACCOUNT"," ");
        if(user_account==" ")
        {
            this.finish();
        }
        else
        {
            tv_homepage.setText("ID : "+user_account.subSequence(6,10));
        }
    }

    private void search(){

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(homepage.this,homepage_search.class));
            }
        });
    }


    private void navigation() {

        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(homepage.this,navigation.class));
                Toast.makeText(getApplicationContext(), "尚未開放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void article() {

        btn_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(homepage.this,ArticleActivity.class));
            }
        });
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(homepage.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setIcon(R.drawable.bg3)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).show();
        }
        return true;
    }

    private void ClearSharepre_Ferences()
    {
        Button btn_logout=(Button)findViewById(R.id.btn_logout);

        String value=getSharedPreferences("KeepAccount",MODE_PRIVATE)
                .getString("value","");
        if(value.length()<=0)
        {
            SharedPreferences pref=getSharedPreferences("USERDATA",MODE_PRIVATE);
            pref.edit()
                    .clear()
                    .commit();
        }


        btn_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String keepacc=getSharedPreferences("KeepAccount",MODE_PRIVATE)
                        .getString("value","");


                    /*SharedPreferences pref=getSharedPreferences("USERDATA",MODE_PRIVATE);
                    pref.edit()
                            .clear()
                           .commit();*/


                Toast.makeText(getApplicationContext(), "已登出", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(homepage.this,MainActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }




}