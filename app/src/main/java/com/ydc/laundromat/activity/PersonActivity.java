package com.ydc.laundromat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.Base64;
import com.ydc.laundromat.common.Constants;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.User;
import com.ydc.laundromat.util.TakePicture;
import com.ydc.laundromat.util.TitleBuilder;
import com.ydc.laundromat.widget.TakePhotoPopWin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener{

    private  TitleBuilder titleBuilder;
    private User user;

    private AppClientDao appClientDao;
    private ImageView pa_portrait_iv;
    private TextView pa_account_tv,
            pa_phone_tv,
            pa_sex_tv,
            pa_school_tv;

    private RelativeLayout sex_rl,school_rl;

    private TextView undoLogin_btn;
    DisplayImageOptions options;
    ImageLoader imageLoader;

    TakePhotoPopWin takePhotoPopWin;

    private byte[] imgByte; //头像数组

    private String imgName = Constants.DefaulActivityPhotoName;

    private static final String IMAGE_UNSPECIFIED = "image/jpg";
    private static final int PHOTO_WITH_DATA = 18; // sd卡获取
    private static final int PHOTO_WITH_CAMERA = 37;// 拍照获取
    private static final int CROP_REQUEST_CODE = 42; //剪裁

    private static final int EDIT_SEX_REQUESTCODE = 1;
    private static final int EDIT_SCHOOL_REQUESTCODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.titlebar_blue));
        }

        imageLoader=ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        appClientDao = new AppClientDao(this);
        initTitle();

        initView();
        initData();
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("个人信息")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);
    }

    private void initData() {
        if(!LocalStorage.getString(this,"user_id").equals("")){
            pa_account_tv.setText(LocalStorage.getString(this,"user_account"));
            String a = LocalStorage.getString(this,"user_phone");
            StringBuffer phone = new StringBuffer();
            for(int i= 0; i < a.length();i++){
                if(i>= 3 && i <= 6){
                    phone.append("*");
                }else{
                    phone.append(a.charAt(i));
                }
            }

            pa_phone_tv.setText(phone.toString());

            pa_sex_tv.setText(LocalStorage.getString(this,"user_sex"));
            pa_school_tv.setText(LocalStorage.getString(this,"user_school"));
            loadPicture(pa_portrait_iv,LocalStorage.getString(this,"user_portraitPath"));
        }
    }

    private void initView() {
        pa_portrait_iv = (ImageView) findViewById(R.id.portraitPath_iv);
        pa_account_tv = (TextView) findViewById(R.id.account_tv);
        pa_phone_tv = (TextView) findViewById(R.id.phone_tv);
        pa_sex_tv = (TextView) findViewById(R.id.sex_tv);
        pa_school_tv = (TextView) findViewById(R.id.school_tv);

        sex_rl = (RelativeLayout) findViewById(R.id.sex_rl);
        school_rl = (RelativeLayout) findViewById(R.id.school_rl);

        undoLogin_btn = (TextView) findViewById(R.id.undoLogin_btn);

        sex_rl.setOnClickListener(this);
        school_rl.setOnClickListener(this);
        undoLogin_btn.setOnClickListener(this);
        pa_portrait_iv.setOnClickListener(this);

    }

    public void showPopFormBottom() {
        takePhotoPopWin = new TakePhotoPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(findViewById(R.id.undoLogin_btn), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    doTakePhoto();

                    break;
                case R.id.btn_pick_photo:
                    doPickPhotoFromGallery();

                    break;
            }
        }
    };

    private void loadPicture(ImageView imageView,String url) {

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        imageLoader.displayImage(Constants.SERVERADDRESS
                + "/file/avatar/get?imageUrl="
                + url,imageView, options);

    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                setResult(-1,null);
                finish();
                break;
            case R.id.sex_rl:
                intent = new Intent(PersonActivity.this,EditPersonActivity.class);
                intent.putExtra("edit_type","性别");
                startActivityForResult(intent,1);
                break;
            case R.id.school_rl:
                intent = new Intent(PersonActivity.this,EditPersonActivity.class);
                intent.putExtra("edit_type","学校");
                startActivityForResult(intent,2);
                break;
            case R.id.undoLogin_btn:
                LocalStorage.saveString(this,"user_id","");
                setResult(1,null);
                finish();
                break;
            case R.id.portraitPath_iv:
                showPopFormBottom();
                break;
        }

    }
    private void doTakePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "image.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent,PHOTO_WITH_CAMERA);

    }

    private void doPickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_WITH_DATA);
    }

    /**
     * 锟斤拷始锟矫硷拷
     *
     * @param uri
     */
    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//
        // aspectX aspectY
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { //
            switch (requestCode) {
                case PHOTO_WITH_CAMERA: {//
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 锟角凤拷锟斤拷SD锟斤拷

                        startCrop(Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg")));
                    } else {
                        Toast.makeText(PersonActivity.this,"没有sd卡",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PHOTO_WITH_DATA: {// 锟斤拷图锟斤拷锟斤拷选锟斤拷图片

                    Uri originalUri = data.getData();
                    startCrop(originalUri);
                    break;

                }
                case CROP_REQUEST_CODE:
                    if (data == null) {
                        // TODO 锟斤拷锟街帮拷院锟斤拷锟斤拷锟斤拷霉锟斤拷锟绞局帮拷锟斤拷玫锟酵计�锟斤拷锟斤拷锟斤拷示默锟较碉拷图片
                        return;
                    }
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = extras.getParcelable("data");
                        imgByte = TakePicture.getBitmapByte(bitmap);
                        imgName = TakePicture.createPhotoFileName();
                        TakePicture.savePicture(PersonActivity.this,imgName, bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                        pa_portrait_iv.setImageBitmap(bitmap);
                        uploadPic();
                    }
                    break;
                case EDIT_SEX_REQUESTCODE:
                    initData();
                    break;
                case EDIT_SCHOOL_REQUESTCODE:
                    initData();
                    break;
            }
        }
    }


    public void uploadPic() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("fileName", imgName));
        params.add(new NameValuePair("fileBody", Base64.encodeBytes(imgByte)));
        appClientDao.uploadPic(mHandler,params,"/file/upload/avatar");

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.UPLOADPIC_SUCCESS:
                    takePhotoPopWin.dismiss();
                    Toast.makeText(PersonActivity.this,"头像上传成功",
                            Toast.LENGTH_SHORT).show();
                    List<NameValuePair> list =new ArrayList<NameValuePair>();
                    list.add(new NameValuePair("user_id",LocalStorage.getString(
                            PersonActivity.this,"user_id")));
                    list.add(new NameValuePair("user_portraitPath",imgName));

                    appClientDao.editUser(mHandler,list,"/user/editPortraitPath");
                    break;
                case ResultMessage.UPLOADPIC_FAIL:
                    takePhotoPopWin.dismiss();
                    Toast.makeText(PersonActivity.this,"头像上传失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.EDIT_USER_SUCCESS:
                    takePhotoPopWin.dismiss();
                    LocalStorage.saveString(PersonActivity.this,"user_portraitPath",imgName);
                    Toast.makeText(PersonActivity.this,"头像保存成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.EDIT_USER_FAIL:
                    takePhotoPopWin.dismiss();
                    Toast.makeText(PersonActivity.this,"头像保存失败",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
