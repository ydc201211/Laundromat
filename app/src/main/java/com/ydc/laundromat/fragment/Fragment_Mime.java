package com.ydc.laundromat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ydc.laundromat.R;
import com.ydc.laundromat.activity.ChangePasswordActivity;
import com.ydc.laundromat.activity.LoginActivity;
import com.ydc.laundromat.activity.PersonActivity;
import com.ydc.laundromat.activity.SuggestActivity;
import com.ydc.laundromat.activity.WalletActivity;
import com.ydc.laundromat.common.Constants;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.widget.ImageView_Circle;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Mime extends Fragment implements View.OnClickListener{

    private View fragment_mine;


    private TextView userName;
    private ImageView_Circle portrait_iv;
    private RelativeLayout toLogin_rl;

    DisplayImageOptions options;
    ImageLoader imageLoader;

    private RelativeLayout mine_info_rl;
    private RelativeLayout setup_rl;
    private RelativeLayout suggest_rl;
    private RelativeLayout wallet_rl;


    public Fragment_Mime() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragment_mine = inflater.inflate(R.layout.fragment_mime, container, false);
        imageLoader=ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        initView();
        initData();
        return  fragment_mine;
    }

    private void initData() {
        if(LocalStorage.getString(getContext(),"user_id") != ""){
            toLogin_rl.setVisibility(View.GONE);
            userName.setText(LocalStorage.getString(getContext(),"user_account"));
            loadPicture(portrait_iv,LocalStorage.getString(getContext(),"user_portraitPath"));
            mine_info_rl.setOnClickListener(this);
        }else {
            toLogin_rl.setVisibility(View.VISIBLE);
            userName.setText("");
            portrait_iv.setImageResource(R.mipmap.nologin);
            mine_info_rl.setClickable(false);
        }
    }

    private void initView() {
        userName = (TextView) fragment_mine.findViewById(R.id.mine_userName);
        portrait_iv = (ImageView_Circle) fragment_mine.findViewById(R.id.imageHead);
        toLogin_rl = (RelativeLayout) fragment_mine.findViewById(R.id.toLogin_rl);

        setup_rl = (RelativeLayout) fragment_mine.findViewById(R.id.setup_ll);
        suggest_rl = (RelativeLayout) fragment_mine.findViewById(R.id.feedback_ll);
        wallet_rl = (RelativeLayout) fragment_mine.findViewById(R.id.wallet_rl);

        setup_rl.setOnClickListener(this);
        suggest_rl.setOnClickListener(this);
        wallet_rl.setOnClickListener(this);
        mine_info_rl = (RelativeLayout) fragment_mine.findViewById(R.id.mine_info_rl);
        toLogin_rl.setOnClickListener(this);

    }


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
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.toLogin_rl:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.mine_info_rl:
                intent = new Intent(getActivity(), PersonActivity.class);
                startActivityForResult(intent,2);
                break;
            case R.id.setup_ll:
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_ll:
                intent = new Intent(getActivity(), SuggestActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_rl:
                intent = new Intent(getActivity(), WalletActivity.class);
                startActivity(intent);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 1:
                if (resultCode == -1) {
                    initData();
                }
                break;
            case 2:
                if(resultCode == -1){
                    initData();
                }else if(resultCode == 1){
                    initData();
                }
            default:
                break;
        }
    }
}
