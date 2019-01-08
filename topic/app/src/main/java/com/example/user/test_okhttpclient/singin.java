package com.example.user.test_okhttpclient;

        import android.content.Intent;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import android.widget.Toast;
        import okhttp3.FormBody;
        import okhttp3.RequestBody;
        import java.lang.Object;
        import android.view.ViewGroup;
        import android.widget.FrameLayout;
        import android.widget.ScrollView;

public class singin extends AppCompatActivity {
    private RequestBody RequestBody;
    private Connect mConncet;
    private Button bt_submit,btn_gotesstwo;
    private TextView text_account,text_password,text_name,text_birthday,text_address,text_contact,text_check_date,text_recheck_date,text_icf_code,text_icd_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin2);
        init();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        PostData();
        gotesstwo();

    }

    private void init(){
        mConncet = new Connect(this,DataHandler);
        btn_gotesstwo=(Button)findViewById(R.id.btn_gotesstwo) ;
        bt_submit = (Button) findViewById(R.id.bt_submit);

        text_account=(TextView)findViewById(R.id.text_account);
        text_password=(TextView)findViewById(R.id.text_password);
        text_name=(TextView)findViewById(R.id.text_name);
        text_birthday =(TextView)findViewById(R.id.text_birthday);
        text_address=(TextView)findViewById(R.id.text_address);
        text_contact=(TextView)findViewById(R.id.text_contact);
        text_check_date=(TextView)findViewById(R.id.text_check_date);
        text_recheck_date=(TextView)findViewById(R.id.text_recheck_date);
        text_icd_code=(TextView)findViewById(R.id.text_icd_code);
        text_icf_code=(TextView)findViewById(R.id.text_icf_code);
    }



    Intent intent = getIntent();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode)
        {
            case 2:
                String result1 = data.getExtras().getString("dataArray1");
                String result2 = data.getExtras().getString("dataArray2");
                String result3 = data.getExtras().getString("dataArray3");
                String result4 = data.getExtras().getString("dataArray4");
                String result5 = data.getExtras().getString("dataArray5");
                String result6 = data.getExtras().getString("dataArray6");
                String result7 = data.getExtras().getString("dataArray7");
                text_account.setText(result1);
                text_name.setText(result2);
                text_birthday.setText(result3);
                text_address.setText(result4);
                text_contact.setText(result5);
                text_check_date.setText(result6);
                text_recheck_date.setText(result7);

                break;
        }
    }





    private void gotesstwo()
    {
        Button btn_gotesstwo = (Button)findViewById(R.id.btn_gotesstwo);
        btn_gotesstwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(singin.this,TestTwo.class);
                startActivityForResult(intent,2);
            }
        });
    }

    private void ParseJson(String result){
        List<Map<String,String>> listData = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray temp = jsonObject.getJSONArray("DATA");
            if(temp.length()!=0){
                for(int i=0;i<temp.length();i++){
                    JSONObject data  = temp.getJSONObject(i);
                    HashMap<String,String> item = new HashMap<>();

                    item.put("account",data.getString("account"));
                    item.put("password",data.getString("password"));
                    item.put("name",data.getString("name"));
                    item.put("birthday",data.getString("birthday"));
                    item.put("address",data.getString("address"));
                    item.put("contact",data.getString("contact"));
                    item.put("check_date",data.getString("check_date"));
                    item.put("recheck_date",data.getString("recheck_date"));
                    item.put("text_icf_code",data.getString("text_icf_code"));
                    item.put("text_icd_code",data.getString("text_icd_code"));
                    listData.add(item);
                }
            }
           /* CustomAdapter adapter = new CustomAdapter(this,listData);
            mListView.setAdapter(adapter);*/
        }catch (JSONException e){
            Log.d("json",e.getMessage());
        }
    }

    private void PostData(){
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText et2=(EditText)findViewById(R.id.text_account);
                String acc=et2.getText().toString();
                EditText et3=(EditText)findViewById(R.id.text_password);
                String pwd=et3.getText().toString();
                EditText et4=(EditText)findViewById(R.id.text_name);
                String name=et4.getText().toString();
                EditText et5=(EditText)findViewById(R.id.text_birthday);
                String birthday=et5.getText().toString();
                EditText et6=(EditText)findViewById(R.id.text_address);
                String address=et6.getText().toString();
                EditText et7=(EditText)findViewById(R.id.text_contact);
                String contact=et7.getText().toString();
                EditText et8=(EditText)findViewById(R.id.text_check_date);
                String check_date=et8.getText().toString();
                EditText et9=(EditText)findViewById(R.id.text_recheck_date);
                String recheck_date=et9.getText().toString();

                EditText et10=(EditText)findViewById(R.id.text_icf_code);
                String icf_code=et10.getText().toString();

                EditText et11=(EditText)findViewById(R.id.text_icd_code);
                String icd_code=et11.getText().toString();

                if(acc.length()==10&&pwd.length()!=0&&name.length()!=0&&birthday.length()!=0&&address.length()!=0&&contact.length()!=0&&check_date.length()!=0&&recheck_date.length()!=0)
                {
                    RequestBody = new FormBody.Builder()
                            .add("account", acc)
                            .add("password", pwd)
                            .add("name", name)
                            .add("birthday", birthday)
                            .add("address", address)
                            .add("contact", contact)
                            .add("check_date", check_date)
                            .add("recheck_date", recheck_date)
                            .add("icf_code", icf_code)
                            .add("icd_code", icd_code)
                            .build();
                    mConncet.setUrl(getString(R.string.tag1) + getString(R.string.url_singin)) // "http://10.0.2.2:8080/topic/test_singin.php"
                            .setRequestBody(RequestBody)
                            .run1();

                    Toast.makeText(getApplicationContext(), "註冊完成", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "請確認資料是否正確", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler DataHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.d("msg",msg.getData().getString("resp"));
            ParseJson(msg.getData().getString("resp"));
        }
    };





}
