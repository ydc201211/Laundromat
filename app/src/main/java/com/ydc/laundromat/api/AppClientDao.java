package com.ydc.laundromat.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ydc.laundromat.common.Constants;
import com.ydc.laundromat.common.NameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ydc on 2016/9/8.
 */
public class AppClientDao {
    private Context context;
    private static OkHttpClient mOkHttpClient=new OkHttpClient();
    public AppClientDao(Context context){
        this.context = context;
    }
    public AppClientDao(){
    }

    public void getSchools(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

       /* for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }*/
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_SCHOOLS_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_SCHOOLS_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void getPointByParentId(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_POINT_BY_PARENTID_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_POINT_BY_PARENTID_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void getWMByParentId(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_WM_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_WM_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void login(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.LOGIN_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.LOGIN_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void addOrder(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.ADD_ORDER_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.ADD_ORDER_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void editOrder(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.EDIT_ORDER_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.EDIT_ORDER_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void editOrderOnService(List<NameValuePair> params,String url) {
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
            }
        });
    }


    public void getOrders(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_ORDERS_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_ORDERS_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }



    public void getOrderByOrderNo(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_ORDER_BY_NO_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_ORDER_BY_NO_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void deleteOrder(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.DELETE_ORDER_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.DELETE_ORDER_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void editWM(final Handler mHandler,List<NameValuePair> params,String url) {

        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.EDIT_WM_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.EDIT_WM_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void uploadPic(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.UPLOADPIC_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.UPLOADPIC_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }



    public void editUser(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.EDIT_USER_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.EDIT_USER_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void registerUser(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.REGISTER_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.REGISTER_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void getOrderByStatusAndUserId(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_ORDER_STATUS_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_ORDER_STATUS_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }



    public void payOrder(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.PAY_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.PAY_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void changePassword(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.CHANGE_PASSWORD_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.CHANGE_PASSWORD_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void changeWallet(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.CHANGE_WALLET_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.CHANGE_WALLET_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    public void getWallet(final Handler mHandler,List<NameValuePair> params,String url){
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < params.size(); i++) {
            builder.add(params.get(i).getName(), params.get(i).getValue());
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.SERVERADDRESS + url)
                .header("User-Agent", "OkHttp Headers.java")
                .post(formBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    int resultCode =  jsonObject.getInt("resultCode");
                    String resultInfo = jsonObject.getString("resultInfo");

                    if (resultCode == 111)
                        mHandler.obtainMessage(ResultMessage.GET_WALLET_SUCCESS,
                                str).sendToTarget();
                    else
                        mHandler.obtainMessage(ResultMessage.GET_WALLET_FAIL,
                                resultInfo).sendToTarget();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }
}
