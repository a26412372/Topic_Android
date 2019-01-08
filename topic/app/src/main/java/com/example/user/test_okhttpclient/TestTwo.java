package com.example.user.test_okhttpclient;

import android.graphics.Matrix;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.content.Intent;
import java.io.FileNotFoundException;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTwo extends AppCompatActivity {
    public static final String TESS_DATA = "/tessdata";
    private static final String TAG = TestTwo.class.getSimpleName();
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Tess";
    private Resources res;
    private TessBaseAPI tessBaseAPI;
    private Uri photoURI;
    private String mCurrentPhotoPath;
    private String[] datas,datas2;
    private Button btn_GetPicture,btn_startOCR1,btn_clean;
    private TextView tv_tesstwo,tv_tesstwo2;
    private ListView lv_tesstwo;
    private ImageView imageView;
    private Bitmap bitmap1;
    private Dialog dialog;
    private ArrayAdapter<String> listAdapter;
    String[] dataArray=new String[30];
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        final Activity activity = this;
        checkPermission();
        inti();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//去掉TitleBar

        GetPicture();


        CleanData();


        this.findViewById(R.id.btn_TakePicture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                checkPermission();
                dispatchTakePictureIntent();
            }
        });

        Finish_Submit();
    }


    private void inti()
    {
        tv_tesstwo = (TextView) findViewById(R.id.tv_tesstwo);
        tv_tesstwo2 = (TextView) findViewById(R.id.tv_tesstwo2);
        lv_tesstwo=(ListView)findViewById(R.id.lv_tesstwo);
        btn_GetPicture=(Button)findViewById(R.id.btn_GetPicture);
        btn_startOCR1=(Button)findViewById(R.id.btn_startOCR1);
        btn_clean=(Button)findViewById(R.id.btn_clean);
        imageView = (ImageView) findViewById(R.id.img_OCRPicture);

        Toast.makeText(getApplicationContext(), "請先拍照或選擇照片，再點選-開始辨識-", Toast.LENGTH_LONG).show();
    }
    /**************************************************************************************************************************************************************************/
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 120);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 121);
        }
    }

    /**************************************************************************************************************************************************************************/
    private void prepareTessData() {                                                                        /** 準備影像辨識資料 **/
        try {
            File dir = getExternalFilesDir(TESS_DATA);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String fileList[] = getAssets().list("");
            for (String fileName : fileList) {
                String pathToDataFile = dir + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte[] buff = new byte[128];
                    int len;
                    while ((len = in.read(buff)) > 0) {
                        out.write(buff, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String getText(Bitmap bitmap)
    {
        try{
            tessBaseAPI = new TessBaseAPI();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        tessBaseAPI.init(dataPath, "chi_tra");
        tessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try{
            retStr = tessBaseAPI.getUTF8Text();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        tessBaseAPI.end();
        return retStr;
    }
    /************************************************************************************************************************************************************************/
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);                            /** 建立圖檔 **/
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, 1000);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "test",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

//**************************************************************************************************************************************************************************/

    private void GetPicture()
    {
        btn_GetPicture.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");
                //使用Intent.ACTION_GET_CONTENT這個Action                                            //會開啟選取圖檔視窗讓您選取手機內圖檔
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //取得相片後返回本畫面
                startActivityForResult(intent, 1001);
            }
        });
    }


    /*****************  i n t e n t   回 來 後 的 動 作 ****************************************************************************************************************/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK&&requestCode==1000)
        {
            tv_tesstwo2.setText("使用剛拍的圖");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 4;

            //Resources res=getResources();                                       //1  這兩行
            //bitmap1=BitmapFactory.decodeResource(res, R.drawable.testcard11);      //2  是直接用存好的圖片testcard11測試
            bitmap1 = BitmapFactory.decodeFile(mCurrentPhotoPath, options);         // 這行是使用拍的照片送去辨識

            imageView.setImageBitmap(bitmap1);
            this.findViewById(R.id.btn_startOCR1).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    prepareTessData();
                    startOCR(bitmap1);//(outputFileDir)
                }
            });
        }
        else if (resultCode == RESULT_OK&&requestCode==1001)
        {
            tv_tesstwo2.setText("使用選擇的圖片");
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try
            {
                final Bitmap bitmap2 = BitmapFactory.decodeStream(cr.openInputStream(uri));
                imageView.setImageBitmap(bitmap2);
                this.findViewById(R.id.btn_startOCR1).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        prepareTessData();
                        startOCR(bitmap2);
                    }
                });
            }
            catch (FileNotFoundException e)
            {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Result canceled.", Toast.LENGTH_SHORT).show();
        }
    }
    /********************* 影 像 辨 識 **********************************************************************************************************/
    private void startOCR(final Bitmap bitmap){


            //＿________＿________＿________   // 圖片旋轉90
          /* Matrix matrix = new Matrix();
            matrix.reset();
            matrix.postRotate(90);
            Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
            String result = this.getText(bMapRotate);*/
            //＿________＿________＿________
            String result = getText(bitmap);
            /*＿________＿________＿________＿________＿________＿*/

            StrSplit(result);   // 切割字串


    }



    /****************************** 字 串 切 割 **********************************************************/
    private void StrSplit(String result)
    {
        Toast.makeText(getApplicationContext(), "請依序選取符合的資料", Toast.LENGTH_LONG).show();
        datas=result.split(" |\n");
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        lv_tesstwo.setAdapter(listAdapter);

        lv_tesstwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(getApplicationContext(), datas[pos], Toast.LENGTH_SHORT).show();
                KeepDataArray(datas[pos]);
            }
        });
    }

    private void KeepDataArray(String data)
    {
        dataArray[i]=data;
        tv_tesstwo.setText(tv_tesstwo.getText()+"  "+dataArray[i]+"\n");
        i++;
    }

    /******************** 清 除 選 取 *************************************************************************/

    private void CleanData()
    {
        btn_clean.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                i=0;
                for(int x=0;x<dataArray.length;x++)
                {
                    dataArray[x]="";
                }

                tv_tesstwo.setText("");
            }
        });
    }

    /***************************  資 料 回 傳  *************************************************************************/

    private void Finish_Submit()
    {
        this.findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(dataArray.length>0)
                {
                    Intent intent=getIntent();
                    Bundle bundle=new Bundle();
                    bundle.putString("dataArray1",dataArray[0]);
                    bundle.putString("dataArray2",dataArray[1]);
                    bundle.putString("dataArray3",dataArray[2]);
                    bundle.putString("dataArray4",dataArray[3]);
                    bundle.putString("dataArray5",dataArray[4]);
                    bundle.putString("dataArray6",dataArray[5]);
                    bundle.putString("dataArray7",dataArray[6]);
                    intent.putExtras(bundle);
                    setResult(2,intent);
                    TestTwo.this.finish();
                }
            }
        });
    }

    /****************************************************************************************************/


}


