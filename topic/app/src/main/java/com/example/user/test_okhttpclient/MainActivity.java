package com.example.user.test_okhttpclient;
import java.lang.Object;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.view.View;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    private RequestBody RequestBody;
    private Connect mConnect;
    private EditText account, password;
    private Button btn_login,btn_sing;
    private CheckBox cb_keep;
    private int LoginFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        String value=getSharedPreferences("KeepAccount",MODE_PRIVATE)
                .getString("value","");
        account.clearAnimation();
        if(value.length()>0)
        {
            String user_account=getSharedPreferences("USERDATA",MODE_PRIVATE)
                    .getString("ACCOUNT","");
            String user_password=getSharedPreferences("USERDATA",MODE_PRIVATE)
                    .getString("PASSWORD","");
            account.setText(user_account);
            password.setText(user_password);
        }
        else
        {
            //account.setText(null);
            //password.setText(null);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setIcon(R.drawable.bg3)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ClearSharepre_Ferences();
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }



    private void init() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        cb_keep = (CheckBox) findViewById(R.id.cb_keep);
        account = (EditText) findViewById(R.id.text_account);
        password = (EditText) findViewById(R.id.text_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sing= (Button) findViewById(R.id.btn_sing);
        mConnect = new Connect(this, DataHandler);



        SingIn(); //註冊
        PostData(); //登入
        KeepAccount();//記住帳號
    }

    private void KeepAccount()
    {
        String value=getSharedPreferences("KeepAccount",MODE_PRIVATE)
                .getString("value","");

        //Toast.makeText(MainActivity.this, value + "", Toast.LENGTH_SHORT).show();

        cb_keep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked==true)
                KeepAccountSharepreFerences("1");
                else
                KeepAccountSharepreFerences("");

            }
        });
        if(value.length()<=0)
        {
            cb_keep.setChecked(false);
        }
        if(value.length()>0)
        {
            cb_keep.setChecked(true);
        }
    }


    private void KeepAccountSharepreFerences(String value)
    {
        SharedPreferences pref=getSharedPreferences("KeepAccount",MODE_PRIVATE);
        SharedPreferences pref2=getSharedPreferences("USERDATA",MODE_PRIVATE);
        if(value.length()>0)
        {
            pref.edit()
                .putString("value",value)
                .commit();
        }
        else
            {
                pref2.edit()
                        .clear()
                        .commit();
                pref.edit()
                        .clear()
                        .commit();
        }
        //Toast.makeText(MainActivity.this, value + "", Toast.LENGTH_SHORT).show();
    }




    private void PostData() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFlag=1;
                String acc = account.getText().toString().trim(), pwd = password.getText().toString().trim();
                if (acc.length()>0&&pwd.length()>0)
                {
                    RequestBody = new FormBody.Builder()
                            .add("account", acc)
                            .add("password", pwd)
                            .build();
                    mConnect.setUrl(getString(R.string.tag2)+getString(R.string.url_main)) // " ~~~~~~ + /topic/test_login.php "
                            .setRequestBody(RequestBody) //"http://10.0.2.2:8080/topic/test_login.php"
                            .run1();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "請輸入帳號或密碼", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler DataHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("msg", msg.getData().getString("resp"));
            ParseJson(msg.getData().getString("resp"));
        }
    };

    private void ParseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray temp = jsonObject.getJSONArray("DATA");
            if (temp.length() != 0)
            {
                for (int i = 0; i < temp.length(); i++)
                {
                    JSONObject data = temp.getJSONObject(i);
                    HashMap<String, String> item = new HashMap<>();
                    String user_account = data.getString("account");
                    String user_password = data.getString("password");

                    if ( user_account.length()>0 || user_password.length()>0 )
                    {
                        LoginFlag=0;
                        SetSharepre_Ferences(user_account,user_password);
                        LoginSuccess();

                    }
                }
            }

        } catch (JSONException e) {
            Log.d("json", e.getMessage());
        }
        if(LoginFlag==1)
        {
            LoginFail();
        }
    }

    private void SetSharepre_Ferences(String SaveAccount,String SavePasword)
    {
        SharedPreferences pref=getSharedPreferences("USERDATA",MODE_PRIVATE);
        pref.edit()
                .putString("ACCOUNT",SaveAccount)
                .putString("PASSWORD",SavePasword)
                .commit();
    }


    private void ClearSharepre_Ferences()
    {
        SharedPreferences pref=getSharedPreferences("USERDATA",MODE_PRIVATE);
        pref.edit()
                .clear()
                .commit();
    }


    private void SingIn() {

        btn_sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, singin.class);
                startActivity(intent);
            }
        });
    }


    private void LoginFail(){
        Toast.makeText(getApplicationContext(), "帳號或密碼錯誤", Toast.LENGTH_LONG).show();
    }

    private void LoginSuccess() {
        Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_LONG).show();
        finish();
    }





}
