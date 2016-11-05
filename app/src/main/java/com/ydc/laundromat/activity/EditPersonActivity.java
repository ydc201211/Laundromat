package com.ydc.laundromat.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.util.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class EditPersonActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup edit_person_rg_tab;
    private LinearLayout edit_school_ll;
    private RadioButton rb_edit_male,rb_edit_famale;
    private EditText edit_school_et;
    private TitleBuilder  titleBuilder;
    private String layout_type;

    private String sex;
    private String school;
    private AppClientDao appClientDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

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

        appClientDao = new AppClientDao(this);
        initSomeView();
        layout_type = getIntent().getStringExtra("edit_type");
        if(layout_type.equals("学校")){
            edit_school_ll.setVisibility(View.VISIBLE);
            edit_person_rg_tab.setVisibility(View.GONE);
            initTitle("学校");
            edit_school_et = (EditText) findViewById(R.id.edit_school_et);

        }else{
            edit_school_ll.setVisibility(View.GONE);
            edit_person_rg_tab.setVisibility(View.VISIBLE);
            initTitle("性别");
            rb_edit_male = (RadioButton) findViewById(R.id.rb_edit_male);
            rb_edit_famale = (RadioButton) findViewById(R.id.rb_edit_famale);
            rb_edit_famale.setChecked(true);
            sex = "女";
            edit_person_rg_tab.setOnCheckedChangeListener(this);
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.EDIT_USER_SUCCESS:
                    if (layout_type.equals("学校")){
                        LocalStorage.saveString(EditPersonActivity.this,"user_school",school);
                        Toast.makeText(EditPersonActivity.this,"保存学校成功",Toast.LENGTH_SHORT).show();


                    }else{
                        LocalStorage.saveString(EditPersonActivity.this,"user_sex",sex);
                        Toast.makeText(EditPersonActivity.this,"保存性别成功",Toast.LENGTH_SHORT).show();
                    }
                    setResult(-1,null);
                    finish();
                    break;
                case ResultMessage.EDIT_USER_FAIL:
                    Toast.makeText(EditPersonActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    private void initSomeView() {
        edit_person_rg_tab = (RadioGroup) findViewById(R.id.edit_person_rg_tab);
        edit_school_ll = (LinearLayout) findViewById(R.id.edit_school_ll);
    }


    private void initTitle(String titleName) {
        titleBuilder = new TitleBuilder(this)
                .setTitleText(titleName)
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this)
                .setRightText("确定").setRightOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.titlebar_tv_right:
                if(layout_type.equals("学校")){
                    school = edit_school_et.getText().toString().trim();
                    if(school.equals("")){
                        Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new NameValuePair("user_id", LocalStorage.getString(this,"user_id")));
                        params.add(new NameValuePair("user_school",school));
                        appClientDao.editUser(mHandler,params,"/user/editInfo");
                    }
                }else {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new NameValuePair("user_id", LocalStorage.getString(this,"user_id")));
                    params.add(new NameValuePair("user_sex",sex));

                    appClientDao.editUser(mHandler,params,"/user/editInfo");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rb_edit_famale:
                sex= "女";
                break;
            case R.id.rb_edit_male:
                sex = "男";
                break;
        }
    }
}
